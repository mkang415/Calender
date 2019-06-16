package board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.service.face.BoardService;
import board.service.impl.BoardServiceImpl;
import dto.Icon;
import schedule.service.face.ScheduleService;
import schedule.service.impl.ScheduleServiceImpl;
import util.Paging;

@WebServlet("/board/list")
public class BoardListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// BoardService 객체
	private BoardService boardService = new BoardServiceImpl();
	
	// ScheduleService 객체
	private ScheduleService scheduleService = new ScheduleServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		
//		System.out.println(paging);

		// 상세 조회
		String event = req.getParameter("event");
		String team = null; // 전달받은 팀 이름
		String region = null; // 전달받은 지역

		int chkEvent = 0; // 야구인지 축구인지 체크

		if (event != null) { // 아래 조건식 사용위해 string형 event 변수 int형으로 변환해서 저장할 변수 생성
			chkEvent = Integer.parseInt(event); // 종목 값 숫자로 변환하여 변수에 저장 (1: 야구, 2: 축구)
		}

		if (chkEvent == 1) { // 야구팀일 경우 지역 저장
			team = (String) req.getParameter("baseballTeam");
			region = (String) req.getParameter("BBregion");
		} else if (chkEvent == 2) { // 축구팀일 경우 지역 저장
			team = (String) req.getParameter("soccerTeam");
			region = (String) req.getParameter("SCregion");
		} else { // 아무것도 선택하지 않았을 경우 전체 조회
			team = "all";
			region = "all";
		}

		//	경기 일정 페이지에서 넘어왔을 경우.
		int sno = 0;
		if ((String)req.getParameter("schno")!=null) {
			sno = Integer.parseInt((String)req.getParameter("schno"));
		}
		
		// 요청 파라미터에서 curPage 얻어오기
		Paging paging;
		
		if(event!=null) {	//	상세검색을 했을 경우
			paging = boardService.getSelectCurPage(req, event, team, region);
		} else {	//	상세검색 안했을 경우 원래대로 출력
			if(sno!=0) {
				paging = boardService.getSelectbyScheNo(req, sno);
			} else {
				paging =  boardService.getCurPage(req);
			}
			
		}
		
		// 게시판 목록 조회
		List list;
		if(event!=null) {	//	상세검색을 했을 경우
			list = boardService.selectBoardByTeamRegion(paging, event, team, region);
		} else {	//	상세검색 안했을 경우 원래대로 출력
			
			if(sno!=0) {
				list = boardService.selectBoardByScheNo(paging, sno);
			} else {
				list = boardService.getList(paging);
			}
			
		}

		// model로 Paging 객체 넣기
		req.setAttribute("paging", paging);
		
		// MODEL로 결과 넣기
		req.setAttribute("list", list);

		if(chkEvent==0) {
			req.setAttribute("chkEvent", chkEvent);
			
		} else {
			req.setAttribute("chkEvent", chkEvent);
			req.setAttribute("team", team);
			req.setAttribute("region", region);
		}
		
		//아이콘 DB값 iconList에 저장
		List<Icon> iconList = scheduleService.iconList();
		 Map<String, String> icon = new HashMap<>(); 
		  for(int i=0; i<iconList.size(); i++) {
			icon.put(iconList.get(i).getIconname(), iconList.get(i).getStorename());
		}

		//값전달
		req.setAttribute("icon", icon);
		
		// view 지정
		req.getRequestDispatcher("/WEB-INF/views/board/list.jsp").forward(req, resp);

	}
}
