<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="UTF-8" isELIgnored="false"%> <%@ taglib prefix ="fmt" uri
="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix ="c" uri
="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Document</title>
    <link rel="stylesheet" href="${contextPath}/css/sellerForm.css" />
  </head>
  <body>
    <div class="div-p">
      <p class="textsize-2 text-left textcolor-black textbold">상품</p>
      <div class="div-sib textsize-1">
        <form
          method="post"
          action="${contextPath}/admin/goods/adminSellerGoodsList.do"
        >
          <select name="goods_search_type">
            <option value="all">전체</option>
            <option value="memberNo">사업자번호</option>
            <option value="name">상품명</option>
            <option value="seller_id">아이디</option>
            <option value="creDate">등록일</option>
          </select>
          <input type="text" name="goods_search_word" placeholder="search.." />
          <button
            class="btn-1 bg-lightgreen textcolor-white border-0"
            type="submit"
          >
            검색
          </button>
        </form>
      </div>
    </div>
    <table border="0" class="textcolor-black textsize-1">
      <tr>
        <th>상품 번호</th>
        <th>사업자번호</th>
        <th>상품명</th>
        <th>아이디</th>
        <th>작성일</th>
        <th style="text-align: right">삭제</th>
      </tr>

      <c:forEach var="goods" items="${goodsList}">
        <tr>
          <td>${goods.goodsNo}</td>
          <td>${goods.memberNo}</td>
          <td>
            <a
              href="${contextPath}/goods/goodsDetail.do?goodsNo=${goods.goodsNo}"
            >
              ${goods.name}</a
            >
          </td>
          <td>${goods.seller_id}</td>
          <td>${goods.creDate}</td>
          <td style="text-align: right">
            <a
              href="javascript:void(0)"
              onclick='fn_openalert("상품을 삭제하시겠습니까?","${contextPath }/admin/goods/deleteAdminSellerGoods.do?goodsNo=${goods.goodsNo}")'
              >삭제</a
            >
          </td>
        </tr>
      </c:forEach>
    </table>
    <div>
      <ul class="ul-li">
        <c:if test="${section>1}">
          <li class="li-btn">
            <button
              class="btn-2 btn-square bg-white btn-border"
              onclick="location.href='${contextPath}/admin/goods/adminSellerGoodsList.do?&category=${category}&section=${section-1}&pageNum=1'"
            >
              <img
                width="20px"
                height="20px"
                src="${contextPath}/img/icon/prev.png"
                alt="prev"
              />
            </button>
          </li>
        </c:if>
        <!-- 페이징을 위한 변수. 페이지 버튼이 어디서 끝나야 하는지. -->
        <c:set var="endPage" value="1" />
        <c:if test="${not empty totalGoodsNum && totalGoodsNum>0}">
          <!-- 레시피는 한 페이지당 6개, 섹션당 60개이므로 이런 식이 된다.-->
          <c:set
            var="result"
            value="${(totalGoodsNum - (section-1)*100) div 10}"
          />
          <c:set var="endPage" value="${Math.ceil(result)}" />
        </c:if>
        <c:forEach begin="1" end="${endPage}" var="i">
          <li class="li-btn">
            <button
              class="btn-2 btn-square bg-white btn-border"
              onclick="location.href='${contextPath}/admin/goods/adminSellerGoodsList.do?category=${category}&section=${section}&pageNum=${i}'"
            >
              ${(section-1)*10+i}
            </button>
          </li>
        </c:forEach>
        <c:if test="${totalGoodsNum > (section)*100}">
          <li class="li-btn">
            <button
              class="btn-2 btn-square bg-white btn-border"
              onclick="location.href='${contextPath}/admin/goods/adminSellerGoodsList.do?category=${category}&section=${section+1}&pageNum=1'"
            >
              <img
                width="20px"
                height="20px"
                src="${contextPath}/img/icon/next.png"
                alt="next"
              />
            </button>
          </li>
        </c:if>
      </ul>
    </div>
  </body>
</html>
