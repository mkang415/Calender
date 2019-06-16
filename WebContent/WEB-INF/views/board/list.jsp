<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page import="dto.Icon"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="dto.Schedule"%>

<c:import url="/WEB-INF/views/layout/header.jsp" />

<script type="text/javascript">

//라디오버튼클릭시 option 바뀜
//v=1 : 야구, 2 : 축구
//idB = 야구 팀, Brg = 야구 팀 지역
//idS = 축구 팀, Srg = 축구 팀 지역
function setDisplay(v,idB,idS,Brg,Srg) {
		if(v=="1") {
			document.getElementById(idB).style.display="block";
			document.getElementById(idS).style.display="none";
			document.getElementById(Brg).style.display="block";
			document.getElementById(Srg).style.display="none";
		}else if(v=="2") {
			document.getElementById(idB).style.display="none";
			document.getElementById(idS).style.display="block";
			document.getElementById(Brg).style.display="none";
			document.getElementById(Srg).style.display="block";
		}
};

$(document).ready(function(){
	//글작성 버튼 누르면 이동
	$("#btnWrite").click(function(){
		location.href="/board/write";
	});
	
});


$(document).ready(function(){
	  $('.more2').click(function(){
	    if($('.more2').hasClass('more2')){
	       $('.more2').addClass('close').removeClass('more2');
	       $('.listnick').css('visibility', 'visible');
	    }else if($('.close').hasClass('close')){
	       $('.close').addClass('more2').removeClass('close');  
	       $('.listnick').css('visibility', 'hidden');
	    }
	  });
});


</script>

<style type="text/css">
table, th{
	text-align: center;
	
}

.maching { /* 매칭게시판 가운데 정렬 */
 	text-align: center;
 	width: 1200px;
}

#pagingBox {
	pasition: relative;
}

#btnBox {
	position: fiexd;
	top: 700px;
	botton: 0px;
	right: 800px;
	height: 40px;
	margin: auto;
	text-align: center;
}

/* #listtable { */
/* 	margin-left: auto; */
/* 	margin-right: auto; */
/* } 

/* 본문 내용 몇글자만 보이고 가리기 */


 .listnick {
  font-size:13px;
  position:absolute; 
  top:230px;
  left: 450px; 
/*   right: 320px; */
   width:150px; 
  height:80px; 
  background: #dcdcdc;
  visibility:hidden;
} 

.more2 {
	display: inline;
	width: 80px;
	hight: 20px;
	background-position: 80px -78px;
	
}

.more2:hover, .close:hover {
  cursor:pointer;
}

</style>

<!-- icon -->
<% List<Icon> iconList = (List)request.getAttribute("iconList"); %>

<!-- icon 사용을 위해 hashmap 사용. 키는 아이콘이름=팀이름. 값은 아이콘 파일명 -->
<% Map<String, String> icon = new HashMap<>(); %>
<%  for(int i=0; i<iconList.size(); i++) {
	icon.put(iconList.get(i).getIconname(), iconList.get(i).getStorename());
}%>



<!-- 사이드바 -->
<div style="width:200px; background: #AAEBAA; float:left; height:660px;"> 
<!-- 상세 검색 조건 유지를 위해 값 전달받음 -->
<!-- 선택한 종목 = chkEvent, 선택한 팀 = team, 선택한 지역 = region -->
<%	int chkEvent = (int)request.getAttribute("chkEvent"); %>
<%	String team = null; %>
<%	String region = null; %>
<%	if(chkEvent != 0) {
	team = (String)request.getAttribute("team");
	region = (String)request.getAttribute("region");
}%>

<div style="width:200px; background: #b4b4b4; float:left; height:660px;"> 
<br><br><br>
<form action="/board/list" method="get">
<fieldset>
<legend style="font-size: 18px;">상 세 검 색</legend>
<div style="width:200px; text-align: center;">
	<div>
		<input type="hidden" name="mno" value="${month}">	<!-- 선택 월 전달 -->
		<strong style="font-size: 15px;"><br>종&nbsp;&nbsp;&nbsp;목</strong>
		<br>
		<br>
	</div>
	<div>	
			<!-- 야구, 축구 종목 라디오 버튼으로 선택 -->
		<% if(chkEvent!=2) { %>
		<input type="radio" id = "bb" name = "event" checked="checked" value = "1" 
			onclick="setDisplay(this.value,'selBB','selSC','BBregion','SCregion')"/>
		<label for="bb">야구 </label>&nbsp;&nbsp;&nbsp;
		<input type="radio" id = "sc" name = "event" value = "2"
			onclick="setDisplay(this.value,'selBB','selSC','BBregion','SCregion')"/>
		<label for="sc">축구</label>
		<% } else {%>
		<input type="radio" id = "bb" name = "event" value = "1"
			onclick="setDisplay(this.value,'selBB','selSC','BBregion','SCregion')"/>
		<label for="bb">야구 </label>&nbsp;&nbsp;&nbsp;
		<input type="radio" id = "sc" name = "event" checked="checked" value = "2"
			onclick="setDisplay(this.value,'selBB','selSC','BBregion','SCregion')"/>
		<label for="sc">축구</label>
		<% } %>
	</div>
	<div>
		<Strong style="font-size: 15px;"><br><br>팀&nbsp;&nbsp;선&nbsp;&nbsp;택</Strong>
		<br>
		<br>
	</div>
	<div id="selBB" <% if(chkEvent==2) { %>style="display: none"<% } %>>	
		<select name="baseballTeam">	<!-- 야구 팀 선택 -->
			<option value="all"<% if("all".equals(team)) { %>selected<% } %>>--- 야구팀 ---</option>
			<option value="KT"<% if("KT".equals(team)) { %>selected<% } %>>KT 위즈</option>
			<option value="LG"<% if("LG".equals(team)) { %>selected<% } %>>LG 트윈스</option>
			<option value="NC"<% if("NC".equals(team)) { %>selected<% } %>>NC 다이노스</option>
			<option value="SK"<% if("SK".equals(team)) { %>selected<% } %>>SK 와이번스</option>
			<option value="기아"<% if("기아".equals(team)) { %>selected<% } %>>기아 타이거즈</option>
			<option value="두산"<% if("두산".equals(team)) { %>selected<% } %>>두산 베어스</option>
			<option value="롯데"<% if("롯데".equals(team)) { %>selected<% } %>>롯데 자이언츠</option>
			<option value="삼성"<% if("삼성".equals(team)) { %>selected<% } %>>삼성 라이온즈</option>
			<option value="키움"<% if("키움".equals(team)) { %>selected<% } %>>키움 히어로즈</option>
			<option value="한화"<% if("한화".equals(team)) { %>selected<% } %>>한화 이글스</option>
		</select>
	</div>

	<div id="selSC" <% if(chkEvent!=2) { %>style="display: none"<% } %>>
		<select name="soccerTeam">	<!-- 축구 팀 선택 -->
			<option value="all"<% if("all".equals(team)) { %>selected<% } %>>--- 축구팀 ---</option>
			<option value="서울"<% if("서울".equals(team)) { %>selected<% } %>>FC 서울</option>
			<option value="강원"<% if("강원".equals(team)) { %>selected<% } %>>강원 FC</option>
			<option value="경남"<% if("경남".equals(team)) { %>selected<% } %>>경남 FC</option>
			<option value="대구"<% if("대구".equals(team)) { %>selected<% } %>>대구 FC</option>
			<option value="상주"<% if("상주".equals(team)) { %>selected<% } %>>상주 FC</option>
			<option value="성남"<% if("성남".equals(team)) { %>selected<% } %>>성남 FC</option>
			<option value="수원"<% if("수원".equals(team)) { %>selected<% } %>>수원 삼성</option>
			<option value="울산"<% if("울산".equals(team)) { %>selected<% } %>>울산 현대</option>
			<option value="인천"<% if("인천".equals(team)) { %>selected<% } %>>인천 유나이티드</option>
			<option value="전북"<% if("전북".equals(team)) { %>selected<% } %>>전북 현대모터스</option>
			<option value="제주"<% if("제주".equals(team)) { %>selected<% } %>>제주 유나이티드</option>
			<option value="포항"<% if("포항".equals(team)) { %>selected<% } %>>포항 스틸러스</option>
		</select>
	</div>
	
	<div>
		<Strong style="font-size: 15px;"><br><br>지&nbsp;&nbsp;&nbsp;역</Strong>
		<br>
		<br>
	</div>
		<!-- 야구 팀 지역 선택 -->
	<div id="BBregion" <% if(chkEvent==2) { %>style="display: none"<% } %>>	
		<select name="BBregion">
			<option value="all"<% if("all".equals(region)) { %>selected<% } %>>--- 전국 ---</option>
			<option value="서울"<% if("서울".equals(region)) { %>selected<% } %>>서울</option>
			<option value="인천"<% if("인천".equals(region)) { %>selected<% } %>>인천</option>
			<option value="수원"<% if("수원".equals(region)) { %>selected<% } %>>수원</option>
			<option value="대전"<% if("대전".equals(region)) { %>selected<% } %>>대전</option>
			<option value="대구"<% if("대구".equals(region)) { %>selected<% } %>>대구</option>
			<option value="광주"<% if("광주".equals(region)) { %>selected<% } %>>광주</option>
			<option value="부산"<% if("부산".equals(region)) { %>selected<% } %>>부산</option>
			<option value="창원"<% if("창원".equals(region)) { %>selected<% } %>>창원</option>
		</select>
	</div>
	
	<div id="SCregion" <% if(chkEvent!=2) { %>style="display: none"<% } %>>
		<select name="SCregion">	<!-- 축구 팀 지역 선택 -->
			<option value="all"<% if("all".equals(region)) { %>selected<% } %>>--- 전국 ---</option>
			<option value="서울"<% if("서울".equals(region)) { %>selected<% } %>>서울</option>
			<option value="춘천"<% if("춘천".equals(region)) { %>selected<% } %>>춘천</option>
			<option value="인천"<% if("인천".equals(region)) { %>selected<% } %>>인천</option>
			<option value="성남"<% if("성남".equals(region)) { %>selected<% } %>>성남</option>
			<option value="수원"<% if("수원".equals(region)) { %>selected<% } %>>수원</option>
			<option value="상주"<% if("상주".equals(region)) { %>selected<% } %>>상주</option>
			<option value="포항"<% if("포항".equals(region)) { %>selected<% } %>>포항</option>
			<option value="전주"<% if("전주".equals(region)) { %>selected<% } %>>전주</option>
			<option value="대구"<% if("대구".equals(region)) { %>selected<% } %>>대구</option>
			<option value="울산"<% if("울산".equals(region)) { %>selected<% } %>>울산</option>
			<option value="창원"<% if("창원".equals(region)) { %>selected<% } %>>창원</option>
			<option value="제주"<% if("제주".equals(region)) { %>selected<% } %>>제주</option>
		</select>
	</div>
	<div>
		<br><br><br>
	<input type="submit" value="일정검색"/>	<!-- 값 전달 버튼 -->
	</div>
</div>
</fieldset>
</form>
</div>

<br>
	<div style="text-align:center">
	
	<input type="button" value="검색"/>
	<!-- <button type="submit" id="btnSearch" class="btn">검색</button> -->
	</div>

 

</div>




<div class="center" style="width:1200px; height: 660px;">

<br>
<div class="maching"><h1>직관 매칭 게시판</h1></div>
<hr>

<table border="1" style="margin-left: auto;
	margin-right: auto;" style="table-layout:fixed;">
<thead>
	<tr>
		<th style="width: 5%;">번호</th>
		<th style="width: 20%;">닉네임</th>
		<th style="width: 40%;">제목</th>
<!-- 		<th style="width: 35%;">내용</th> -->
		<th style="width: 5%;">스케줄</th>
		<th style="width: 10%;">응원하는팀</th>
		<th style="width: 15%;">작성일</th>
		<th style="width: 5%;">조회수</th>
	</tr>
</thead>


<c:forEach items="${list}" var="i">
 <tr>
 
 	<td>${i.boardno }</td>
 	<td>${i.nickname }
 	<span style="float:right;" class="more2">
	<span class="blind">
	<img src="/logo/semi_default.png" width="30px" height="20px">
	</span>
	</span>
	
	<div class="listnick">
  	<ul class="list">
  	<li> 프로필 </li>
  	</ul>
	</div>
 	</td>
 	<td><a href="/board/view?boardno=${i.boardno}">${i.title }</a></td>
 	<td>${i.scheduleno }</td>
 	
 	
 	<!-- 응원하는 팀 icon -->
 	<td>${i.team }</td>
<!--  	<td>홈팀 이름으로 아이콘 주소 값 가져와서 이미지 불러옴 -->
<%-- 		<img src="/logo/<%= icon.get(${i.team})%>" --%>
<!-- 		style="width: 21px; height: 21px;"/> -->
<%-- 		<%= (team) %> --%>
<!-- 		<br> -->
<!--  	</td> -->
 	
 	
 	
 	
 	<td><fmt:formatDate value="${i.insertdate }" pattern="yyyy-MM-dd"/></td>
 	<td>${i.hit }</td>
 </tr>
 
 </c:forEach>
 
 </table>
 
 <div id="pagingBox">
 <c:import url="/WEB-INF/views/layout/paging.jsp"/>
 
 <br><br><br>
 <div id="btnBox">
 <button id="btnWrite" class="btn btn-primary">글작성</button>
   
 </div>
 
 </div>
 
</div>



<c:import url="/WEB-INF/views/layout/footer.jsp" />



