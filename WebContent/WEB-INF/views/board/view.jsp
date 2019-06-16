<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>



<c:import url="/WEB-INF/views/layout/header.jsp" />

<script type="text/javascript"
src="http://code.jquery.com/jquery-2.2.4.min.js"></script>


<!------------------------------------------------------------------------------------------------------------>



<script type="text/javascript">
$(document).ready(function() {
	
	//목록버튼 동작
	$("#btnList").click(function() {
		$(location).attr("href","/board/list");
	});

	//수정버튼 동작
	$("#btnUpdate").click(function() {
		$(location).attr("href","/board/update?boardno=${viewBoard.boardno}");
	});

	//삭제버튼 동작
	$("#btnDelete").click(function() {
		$(location).attr("href","/board/delete?boardno=${viewBoard.boardno}");
	});
	
	
	//댓글 입력
	$("#btnReplyInsert").click(function(){
		
		console.log($("#replyWriter").val());
		
		$form=$("<form>").attr({
			action:"/reply/insert",
			method: "post"
		}).append(
				$("<input>").attr({
					type:"hidden",
					name:"boardno",
					value:"${viewBoard.boardno}"
				})
				
		).append (
				$("<input>").attr({
					type:"hidden",
					name:"nickname",
					value:"${sessionScope.nickname}"
				})
		
		).append(
				$("<textarea>").attr("name","recontent")
				.css("display","none").text($("#reContent").val()) //입력칸에 퍼렇게 색 뜸
				);
		
		$(document.body).append($form);
		$form.submit();
	});
});	

	//댓글삭제
	function deleteReply(replyno) {
		$.ajax({
			type: "post"
			,url:"/reply/delete"
			,dataType: "json"
			, data: {
				replyno: replyno
			}
		,success: function(data){
			if(data.success) {
				$("[data-replyno='"+replyno+"']").remove();
			} else {
				alert("댓글 삭제를 실패하였습니다.");
			}
		}
		
		, error: function() {
			console.log("error");
		}
		});
	
	
	}

	//회원아이콘 클릭 리스트
	$(document).ready(function(){
		  $('.more1').click(function(){
		    if($('.more1').hasClass('more1')){
		       $('.more1').addClass('close').removeClass('more1');
		       $('.viewnick').css('visibility', 'visible');
		    }else if($('.close').hasClass('close')){
		       $('.close').addClass('more1').removeClass('close');  
		       $('.viewnick').css('visibility', 'hidden');
		    }
		  });
		});

	
	
	
	
	
</script>

<!------------------------------------------------------------------------------------------------------------>

<style>

.maching {
	text-align: center;
}

.contents {
	width: 1200px;
	overflow:auto; /*자동스크롤*/
/* 	height: 660px; */
}

table, tr{
	border: 1px;
	width: 1000px;
	text-align: center;
	
}

.clearfix {
	width: 1000px;
	margin-left: auto;
	margin-right: auto;
	
}

#vbtn {
  	position:absolute;  
  	top: 800px;
	text-align: center;
	left: 450px;
}


#modal {
  display: none;
  position:relative;
  width:100%;
  height:100%;
  z-index:1;
}

#modal h2 {
  margin:0;   
}

#modal button {
  display:inline-block;
  width:100px;
  margin-left:calc(100% - 100px - 10px);
}

#modal .modal_content {
  width:300px;
  margin:100px auto;
  padding:20px 10px;
  background:#fff;
  border:2px solid #666;
}

#modal .modal_layer {
  position:fixed;
  top:0;
  left:0;
  width:100%;
  height:100%;
  background:rgba(0, 0, 0, 0.5);
  z-index:-1;
}  

#modal_table {
	display:table;
	position:relative;
	width:100%;
	height:200px;
	border:2px solid #666;
} 



#reply {
	width: 1200px;
/* 	position: fixed; */
}

#replylist{
	width: 1200px;
	text-align: center;
}

.viewnick {
  font-size:13px;
  position:absolute; 
  top:130px;
  left: 780px;
  width:150px; 
  height:80px; 
  background: #dcdcdc;
  visibility:hidden;
}

.more1 {
	display: inline;
	width: 80px;
	hight: 20px;
	background-position: 80px -78px;
	
}

.more1:hover, .close:hover {
  cursor:pointer;
}

</style>

<!------------------------------------------------------------------------------------------------------------>

<div class = "contents">

<div><h1 class="maching">직관 매칭 게시판</h1></div>


<!------------------------------------------------------------------------------------------------------------>

<div class="clearfix">


<div id="modal">
   
    <div class="modal_content">
        <h2 style="text-align: center">신고하기</h2>
        <hr>
        
        <table border="1" id="modal_table">
        <tr><td>
        <select>
        <option>---신고사유선택---</option>
        <option>욕설난무</option>
        <option>타인비방</option>
        <option>이상한말</option>        
        </select>
        </td></tr>
        
       
        <tr><td><input type="text" value="제목"></td></tr>
        
       <tr><td>
       <textarea placeholder="내용을 입력해 주세요" style="text-align: center" >내용을 입력해 주세요 </textarea>
       </td></tr>
        
       </table>
       
       <br><br>
       <div>
       	<input type="button" id="modal_write_btn" style="text-align: center" value="작성"/>
       	 &nbsp;&nbsp;&nbsp;
       	<input type="button" id="modal_close_btn" style="text-align: center" value="취소"/>
	</div>
    </div>
   
 	   <div class="modal_layer"></div>
    
    
</div>

<!------------------------------------------------------------------------------------------------------------>


<table class="table table-bordered" id="view">
<tr>
<td class="success" style="text-align: center">글번호</td><td colspan="2">${viewBoard.boardno }</td>
<td class="success" style="text-align: center">
닉네임</td><td colspan="2">${viewBoard.nickname }

<span style="float:right;" class="more1">
	<span class="blind">
	<img src="/logo/semi_default.png" width="30px" height="20px">
	</span>
	</span>
	
	<div class="viewnick">
  	<ul class="list">
  	<li> 프로필 </li>
  	</ul>
	</div>


<!------------------------------------------------------------------------------------------------------------>



</td>
<td class="success" style="text-align: center">응원하는팀</td><td colspan="2">${viewBoard.team }</td>

</tr>


<tr>
<td class="success" style="text-align: center">조회수</td><td colspan="2">${viewBoard.hit }</td>
<td class="success" style="text-align: center">작성일</td><td colspan="2">${viewBoard.insertdate }</td>
<td class="success" style="text-align: center">스케줄</td><td colspan="2">${viewBoard.scheduleno}</td>
</tr>


<tr>
<td class="success" style="text-align: center">제목</td><td colspan="5">${viewBoard.title }</td>
<td class="success"	style="text-align: center">경기하는팀</td><td colspan="2">여기에경기팀</td>
</tr>

<tr><td class="success" style="text-align: center" colspan="8" >본문</td></tr>
<tr><td colspan="8">${viewBoard.content }</td></tr>


</table>
</div>
</div>
<br><br><br>

<!---------------------댓글--------------------------------------------------->
<div>
<hr>
<!-- 로그인 안한상태 -->
<c:if test="${not login }">

&nbsp&nbsp&nbsp<strong>로그인이 필요합니다</strong><br>
&nbsp&nbsp&nbsp<button onclick='location.href="/login";'>로그인</button>
&nbsp&nbsp&nbsp<button onclick='location.href="/signup";'>회원가입</button>

</c:if>

<!-- 로그인했을때 -->
<c:if test="${login}">



<!-- 댓글입력 -->
<div class="form-inline text-center"  id="reply">

	<input type="text" size="10" class="form-control" id="replyWriter" value="${sessionScope.nickname }" readonly="readonly"/>
	<textarea rows="2" cols="100" class="form-control" id="reContent" ></textarea>
	<button id="btnReplyInsert" class="btn btn-warning" >입력</button>
</div>	<!-- 댓글 입력 end -->
</c:if>


<br><br><br><br><br>


<!-- 댓글 리스트 -->
<table class="table table-striped table-hover table-condensed table-bordered table-fixed
" id="replylist">
<thead>
<tr>
	<th style="width: 5%;">번호</th>
	<th style="width: 10%;">닉네임</th>
	<th style="width: 65%;">댓글</th>
	<th style="width: 20%;">작성일</th>
</tr>
</thead>

<tbody id="replyBody">

<c:forEach items="${replyList }" var="reply">

<tr data-replyno="${reply.replyno }">

	<td>${reply.rnum }</td>
	<td>${reply.nickname }<img src="/logo/semi_default.png" width="30px" height="20px"></td>
	<td>${reply.recontent }</td>
	<td><fmt:formatDate value="${reply.insertdate }" /></td>
	
	<td>	<c:if test="${sessionScope.nickname eq reply.nickname}">
		<button class="btn btn-danger btn-sm" onclick="deleteReply(${reply.replyno });">삭제</button>
		</c:if>
	</td>
</tr>

</c:forEach>
</tbody>
</table>	<!-- 댓글 리스트 end -->

</div>  <!-- 댓글처리 end -->

<br><br><br><br>

<!-- -----------------------------버튼들--------------------------------------------------------- -->

<div id="vbtn">	
	<span><button id="btnList" class="btn btn-primary">목록</button></span>
	<c:if test="${userid eq viewBoard.nickname }">
	</c:if>
	<span id="root">
	<button type="button" id="modal_open_btn" class="btn btn-warning">신고</button>

	</span>
<!-- 	 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; -->
	<span>
	<button type="button" id="btnUpdate" class="btn btn-info">글 수정</button></span>
	<span>
	<button type="button" id="btnDelete" class="btn btn-danger">글 삭제</button></span>
</div>



<script>
$(document).ready(function() {


$("#modal_open_btn").click(function(){
    $("#modal").attr("style", "display:block");
});

 $("#modal_close_btn").click(function(){
    $("#modal").attr("style", "display:none");
});  
	});  
</script>













<br><br><br><br>


<c:import url="/WEB-INF/views/layout/footer.jsp" />



















