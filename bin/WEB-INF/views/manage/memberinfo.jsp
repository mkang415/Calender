<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:import url="/WEB-INF/views/layout/header.jsp" />

<script type="text/javascript"
src="http://code.jquery.com/jquery-2.2.4.min.js"></script>

<script type="text/javascript">
$(document).ready(function() {
   //목록버튼 동작
   $("#btnList").click(function() {
      $(location).attr("href","/manage/member");
   });
   
   
   
});

</script>

<style>

#modal {
  display: none;
  position:fixed;
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
  margin:auto;
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
   width:auto;
   height:auto;
   border:2px solid #666;
} 



</style>

<div class = "contents">

<div><h1>회원 정보</h1></div>
<div class="clearfix">


<div id="modal">
   
    <div class="modal_content">
    <form action="/manage/getout" method="post">
        <h2 style="text-align: center">${infoMember.userid }님의  제재 사유</h2>
        <hr>
        
        <table border="1" id="modal_table">
        <tr><td>
            <input type="hidden" name="getoutid" value="${infoMember.userid}">
           <select name="reason">
              <option>------------------신고사유선택------------------</option>
              <option value="욕설난무">욕설난무</option>
              <option value="타인비방">타인비방</option>
              <option value="이상한말">이상한말</option>        
             </select>
      
        
        </td></tr>
        
        
       <tr><td>
       <textarea name="explain" rows="4" cols="35" placeholder="내용을 입력해 주세요" style="text-align: center">내용을 입력해 주세요 </textarea>
       </td></tr>
        
       </table>
       
       <br><br>
       <div>
          <input type="submit" style="text-align: center" value="제재"/>
           &nbsp;&nbsp;&nbsp;
          <input type="button" id="modal_close_btn" style="text-align: center" value="취소"/>
    
      </div>
   </form>
    </div>
   
       <div class="modal_layer"></div>
    
    
</div>



<table class="table table-bordered">

<tr>
   <td><label for="userid">아이디 : </label>${infoMember.userid }</td>
</tr>

<tr>
   <td><label for="age">나이 : </label>${infoMember.age }</td>
</tr>

<tr>
   <td><label for="gender">성별 : </label>${infoMember.gender }</td>
</tr>

<tr>
   <td><label for="nickname">닉네임 : </label>${infoMember.nickname }</td>
   <td><label for="joindate">가입일 : </label><fmt:formatDate value="${infoMember.joindate }" pattern="yyyy-MM-dd" /></td>
</tr>

<tr>
   <td><label for="teamname">좋아하는 팀 : </label>${infoMember.teamname }</td>
   <td><label for="writing">활동내역 : </label> 게시글 = ${cntBoard } , 댓글수 = ${cntReply }</td>
</tr>

<tr>
   <td><label for="introduce">자기 소개 : </label>${infoMember.introduce }</td>
</tr>

</table>
</div>
</div>


<div id="vbtn">   
   <span><button id="btnList" class="btn btn-primary">목록</button></span>
   
   <span><button id="modal_open_btn" class="btn btn-warning">영구 탈퇴</button></span>

</div>

<script>
$("#modal_open_btn").click(function(){
    $("#modal").attr("style", "display:block");
});

 $("#modal_close_btn").click(function(){
    $("#modal").attr("style", "display:none");
});     
</script>


<c:import url="/WEB-INF/views/layout/footer.jsp" />
    