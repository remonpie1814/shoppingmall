package com.example.demo.member.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.common.alert.Alert;
import com.example.demo.common.file.GeneralFileUploader;
import com.example.demo.member.service.MemberService;
import com.example.demo.vo.MemberVO;

@Controller("memberController")
public class MemberControllerImpl implements MemberController {

	@Autowired
	private MemberService memberService;

	@Override
	@RequestMapping(value = "/member/login.do", method = RequestMethod.POST)
	public ModelAndView login(@RequestParam Map<String, String> loginMap, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		System.out.println(loginMap);
		MemberVO memberVO = memberService.login(loginMap);
		System.out.println("memberVO = " + memberVO);

		if (memberVO != null && memberVO.getId() != null) {
			HttpSession session = request.getSession();
			session = request.getSession();
			session.setAttribute("isLogOn", true);
			session.setAttribute("memberInfo", memberVO);
			mav.setViewName("redirect:/main/mainPage.do");

		} else {
			System.out.println("로그인 X");
			mav = Alert.alertAndRedirect("아이디나 비밀번호가 틀립니다. 다시 시도해 주세요",
					request.getContextPath() + "/member/loginForm.do");
		}
		return mav;
	}

	@Override
	@RequestMapping(value = "/member/logout.do", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		session.setAttribute("isLogOn", false);
		session.removeAttribute("memberInfo");
		mav.setViewName("redirect:/main/mainPage.do");
		return mav;
	}

	@Override
	@RequestMapping(value = "/member/register.do", method = RequestMethod.POST)
	public ModelAndView Register(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("utf-8");
		ModelAndView mav = new ModelAndView();
		try {
			int memberNo = memberService.registerInfoNo(); // 새로운 No
			Map memberMap = GeneralFileUploader.getParameterMap(request);
			memberMap.put("memberNo", memberNo);
			String _birth = (String) memberMap.get("birth");
			String sms_agreement = (String) memberMap.get("sms_agreement");
			String email_agreement = (String) memberMap.get("email_agreement");
			String zipCode = (String) memberMap.get("zipCode");

			if (_birth == null || _birth.trim().length() < 1) {
				memberMap.put("birth", null);
			}
			if (email_agreement == null || email_agreement.trim().length() < 1) {
				memberMap.put("email_agreement", "no");
			}
			if (sms_agreement == null || sms_agreement.trim().length() < 1) {
				memberMap.put("sms_agreement", "no");
			}

			if (zipCode == null || zipCode.trim().length() < 1) {
				memberMap.put("zipCode", 0);
			}

			System.out.println(memberMap);
			memberService.insertMemberWithMap(memberMap);
			mav = Alert.alertAndRedirect("회원가입이 완료되었습니다.", request.getContextPath() + "/member/loginForm.do");

		} catch (Exception e) {
			mav = Alert.alertAndRedirect("오류가 일어나 가입하지 못 했습니다.",
					request.getContextPath() + "/member/registerTypeSelect.do");
		}

		return mav;
	}

	// 민아 아이디 찾기
	@Override
	@RequestMapping(value = "/member/idSearch.do", method = RequestMethod.POST)
	public ModelAndView idSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {

		System.out.println("idSearch Controller");
		ModelAndView mav = new ModelAndView();
		MemberVO memberVO = new MemberVO();
		String phone = request.getParameter("phone");
		
		phone = phone.replaceAll("-", "");
		String name = request.getParameter("name");
		memberVO.setName(name);
		memberVO.setPhone(phone);

		List<String> idList = memberService.selectIdList(memberVO);

		if (idList == null || idList.size()<1) {
			mav = Alert.alertAndRedirect("해당하는 아이디가 존재하지 않습니다.", request.getContextPath() + "/member/idSearchForm.do");
			return mav;
		}
		System.out.println("idList = " + idList);
		mav.addObject("idList", idList);
		mav.setViewName("/member/idForm");
		return mav;
	}

	// 민아 비밀번호 찾기
	@Override
	@RequestMapping(value = "/member/pwdSearch.do", method = RequestMethod.POST)
	public ModelAndView PwdSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ModelAndView mav = new ModelAndView();

		MemberVO memberVO = new MemberVO();
		String phone = request.getParameter("phone");
		String name = request.getParameter("name");
		String id = request.getParameter("id");

		memberVO.setPhone(phone);
		memberVO.setName(name);
		memberVO.setId(id);

		try {
			MemberVO member = memberService.pwSearch(memberVO);
			HttpSession session = request.getSession();
			// 세션에 회원정보 보관
			session.setAttribute("member", member);
			mav.setViewName("redirect:/member/newPwSearchForm.do");
		} catch (NullPointerException e) {
			System.out.println("비밀번호 찾기 - 정보 X");
			mav = Alert.alertAndRedirect("해당하는 비밀번호가 없습니다.", request.getContextPath() + "/member/idSearchForm.do");
			e.printStackTrace();
		}
		return mav;
	}

	// 민아 비밀번호 - 업데이트
	@Override
	@RequestMapping(value = "/member/updatePw.do", method = RequestMethod.POST)
	public ModelAndView updatePw(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("updatePw Controller");
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		String pwd = request.getParameter("pwd");
		MemberVO member = (MemberVO) session.getAttribute("member");
		member.setPwd(pwd);
		System.out.println("member = " + member);
		int result = memberService.updatePw(member);
		ModelAndView mav = new ModelAndView("redirect:/member/loginForm.do");
		return mav;

	}

	
	@Override
	@RequestMapping(value = "/member/sellerRegister.do", method = RequestMethod.POST)
	public ModelAndView sellerRegister(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		ModelAndView mav = new ModelAndView();
		Map memberMap = GeneralFileUploader.getParameterMap(request);
		try {
			String sms_agreement = request.getParameter("sms_agreement");
			String email_agreement = request.getParameter("email_agreement");
			String birth = request.getParameter("birth");
			String zipCode = request.getParameter("zipCode");
			
			
			if (birth == null || birth.trim().length() < 1) {
				memberMap.put("birth", null);
			}
			if (email_agreement == null || email_agreement.trim().length() < 1) {
				memberMap.put("email_agreement", "no");
			}
			if (sms_agreement == null || sms_agreement.trim().length() < 1) {
				memberMap.put("sms_agreement", "no");
			}
			if (zipCode == null || zipCode.trim().length() < 1) {
				memberMap.put("zipCode", 0);
			}
			
			
			
			memberService.insertSellerMemberWithMap(memberMap);
			mav.addObject("registerResult","success");
			mav.setViewName("/member/sellerRegisterForm");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addAllObjects(memberMap);
			mav = Alert.alertAndRedirect("오류가 일어나 가입하지 못했습니다.", request.getContextPath()+"/member/sellerRegisterForm.do");
		}
		
		
		
		return mav;
	}

	@ResponseBody
	@RequestMapping("/checkDuplicatedId.do")
	public String checkDuplicatedId(HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
		String result = "fail";

		MemberVO checkMem = memberService.selectMemberById(id);
		System.out.println("checkMem : " + checkMem);
		if (checkMem == null) {
			result = "success";
		}
		return result;
	}

	@ResponseBody
	@RequestMapping("/phoneInzung.do")
	public String phoneInzung(HttpServletRequest request) {
		String result = "success";

		return result;
	}

	@ResponseBody
	@RequestMapping("/busInzung.do")
	public String busInzung(HttpServletRequest request) {
		String result = "success";

		return result;
	}

}
