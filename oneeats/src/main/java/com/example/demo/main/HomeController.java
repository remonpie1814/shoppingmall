package com.example.demo.main;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	

	@RequestMapping(value = "/home.do")
	public ModelAndView home(HttpServletRequest request) {
		ModelAndView mav = new ModelAndVi
		System.out.println(m
		return mav;
	}
}
