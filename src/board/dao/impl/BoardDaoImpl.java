package board.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import board.dao.face.BoardDao;
import dbutil.DBConn;
import dto.Board;
import dto.Icon;
import dto.Schedule;
import util.Paging;

public class BoardDaoImpl implements BoardDao {

	// DB 관련 객체 
	private Connection conn = DBConn.getConnection();
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	@Override
	public List selectAll(Paging paging) {
		
		String sql = "";
		sql += "SELECT * FROM (";
		sql += " SELECT rownum rnum, B.* FROM (";
		sql += " 	SELECT boardno, nickname, title, content, scheduleno, team, insertdate, hit FROM board";
		sql += " 	ORDER BY boardno DESC";
		sql += " )B";
		sql += " ORDER BY rnum";
		sql += " )";
		sql += " WHERE rnum BETWEEN ? AND ?";
		
		List list = new ArrayList();
		
		try {
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, paging.getStartNo()); 
			ps.setInt(2, paging.getEndNo()); 
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Board board = new Board();
				
				board.setBoardno(rs.getInt("boardno"));
				board.setNickname(rs.getString("nickname"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setScheduleno(rs.getInt("scheduleno"));
				board.setTeam(rs.getString("team"));
				board.setInsertdate(rs.getDate("insertdate"));
				board.setHit(rs.getInt("hit"));
				
				list.add(board);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null)
				try {
					if(rs!=null)	rs.close();
					if(ps!=null)	ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			
		}
		return list;
	}

	//게시글 수 조회
	@Override
	public int selectCntAll() {
		
		String sql = "";
		sql += "SELECT count(*)";
		sql += " FROM board";
		
		int totalCount = 0;
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				totalCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null)	rs.close();
				if(ps!=null) 	ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalCount;
		
	}
	
	
	//게시글 조회수 +1
	@Override 
	public void updateHit(Board viewBoard) {
		
		//파일업로드 기록 조회쿼리
		String sql = "";
		sql += "UPDATE board";
		sql += " SET hit = hit +1";
		sql += " WHERE boardno = ?";
		
		try {
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, viewBoard.getBoardno());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps!=null)	ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	
	//게시글 상세보기
	@Override
	public Board selectBoardByBoardno(Board viewBoard) {
		
		//게시글 조회
		String sql = "";
		sql += "SELECT boardno, nickname, title, content, scheduleno, team, insertdate, hit FROM board";
		sql += " WHERE boardno = ?";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, viewBoard.getBoardno());
		
			rs = ps.executeQuery();
			
			while (rs.next()) {
				viewBoard.setBoardno(rs.getInt("boardno"));
				viewBoard.setNickname(rs.getString("nickname"));
				viewBoard.setTitle(rs.getString("title"));
				viewBoard.setContent(rs.getString("content"));
				viewBoard.setScheduleno(rs.getInt("scheduleno"));
				viewBoard.setTeam(rs.getString("team"));
				viewBoard.setInsertdate(rs.getDate("insertdate"));
				viewBoard.setHit(rs.getInt("hit"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			try {
				if(rs!=null)	rs.close();
				if(ps!=null)	ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return viewBoard;
	}


	// 게시글 조회 
	@Override
	public int selectBoardno() {
		
		//다음 게시글 번호 조회
		String sql ="";
		sql += "SELECT board_seq.nextval";
		sql += " FROM dual";
		
		//게시글 번호
		int boardno = 0;
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()){
				boardno = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null)	rs.close();
				if(ps!=null)	ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//게시글 번호 반환
		return boardno;
		
	}
	
	
	//게시글 입력
	@Override
	public void insert(Board board) {
		
		
		
		String sql = "";
		sql += "INSERT INTO board(BOARDNO, NICKNAME, TITLE, CONTENT, scheduleno, insertdate, TEAM, HIT)";
		sql += " VALUES(board_seq.nextval,?,?,?,?,sysdate,?,0)";
		
		
		
		try {
			ps=conn.prepareStatement(sql);
			
			ps.setString(1, board.getNickname());
			ps.setString(2, board.getTitle());
			ps.setString(3, board.getContent());
			ps.setInt(4, board.getScheduleno());
			ps.setString(5, board.getTeam());
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps!=null)	ps.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}


	//게시글 수정
	@Override
	public void update(Board board) {
		
		String sql ="";
		sql += "UPDATE board";
		sql += " SET title = ?,";
		sql += " content = ?";
		sql += " WHERE boardno = ?";
		
		//DB객체
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, board.getTitle());
			ps.setString(2, board.getContent());
			ps.setInt(3, board.getBoardno());
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps!=null)	ps.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}


	//게시글 삭제
	@Override
	public void delete(Board board) {
		
		String sql = "";
		sql += "DELETE board";
		sql += " WHERE boardno = ?";
		
		//DB객체
		PreparedStatement ps = null;
		
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(1, board.getBoardno());
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		
		} finally {
			try {
				if(ps!=null)	ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	//---------------------------------------------
	@Override
	public List getList(String event, String team, String region) {
		
		
		
		return null;
	}

	

//----------------------------------------------------------------------------------------------	
	// 내가 쓴 글 보기 
	@Override
	public void myBoard(Board board) {
		
		String sql = "";
		sql += "SELECT  * FROM (";
		sql += " SELECT boardno, nickname, title, content, scheduleno, inserdate, hit FROM board";
		sql += " ) ORDER BY insertdate";
		sql += " WHERE userid = ?";
		
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, board.getNickname());
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		
		} finally {
			try {
				if(ps!=null)	ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

//-----------------------------------------------------------------------
	//응원하는팀 
	@Override
	public int scheduleno(String team, String gamedate) {
		
		int scheduleno = 0;
		
		String sql = "";

		sql += "SELECT scheduleno FROM schedule";
		sql += " WHERE gamedate=?";
		sql += " And (hometeam=? or awayteam=?)";
		
		try {
			ps=conn.prepareStatement(sql);
			
			ps.setString(1, gamedate);
			ps.setString(2, team);
			ps.setString(3, team);
			
			rs = ps.executeQuery();
			

			while(rs.next()){
				scheduleno = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return scheduleno;
	}



	@Override
	public List getScheduleno(String event, String team, String region) {
		
		List<Integer> searchList = new ArrayList();
		try {
			if("all".equals(team)) {
				if("all".equals(region)) {
					if("1".equals(event)) {	//	야구 모든 팀 모든 지역 검색
						String sql="";	
						sql += "select scheduleno from schedule";
						sql += " where hometeam in(";
						sql += " select teamname from team ";
						sql += " where event = '1') and gamedate > sysdate";
					
						ps = conn.prepareStatement(sql);
						rs = ps.executeQuery();
						
						while (rs.next()) {
							searchList.add(rs.getInt("scheduleno"));
						}
						
					} else {	//	축구 모든 팀 모든 지역 검색
						String sql="";
						sql += "select scheduleno from schedule";
						sql += " where hometeam in(";
						sql += " select teamname from team";
						sql += " where event = '2') and gamedate > sysdate";
					
						ps = conn.prepareStatement(sql);
						rs = ps.executeQuery();
						
						while (rs.next()) {
							searchList.add(rs.getInt("scheduleno"));
						}
					}
				} else {
					if("1".equals(event)) {	//	야구 모든팀 선택 지역
						String sql="";
						sql += "select scheduleno from schedule";
						sql += " where hometeam in(";
						sql += " select teamname from team";
						sql += " where event = '1' and region = ?) and gamedate > sysdate";
						
						ps = conn.prepareStatement(sql);
						ps.setString(1, region);
						rs = ps.executeQuery();
						
						while (rs.next()) {
							searchList.add(rs.getInt("scheduleno"));
						}
					} else {	//	축구 모든팀 선택 지역
						String sql="";
						sql += "select scheduleno from schedule";
						sql += " where hometeam in(";
						sql += " select teamname from team";
						sql += " where event = '2' and region = ?) and gamedate > sysdate";
						
						ps = conn.prepareStatement(sql);
						ps.setString(1, region);
						rs = ps.executeQuery();
						
						while (rs.next()) {
							Schedule schedule = new Schedule();
							searchList.add(rs.getInt("scheduleno"));
						}
					}
				}
			} else {	
				if("all".equals(region)) {	//	선택팀 모든 지역
					String sql="";
					sql += "select scheduleno from schedule";
					sql += " where (hometeam = ? or awayteam = ?) and gamedate > sysdate";
					
					ps = conn.prepareStatement(sql);
					ps.setString(1, team);
					ps.setString(2, team);
					rs = ps.executeQuery();
					
					while (rs.next()) {
						Schedule schedule = new Schedule();
						searchList.add(rs.getInt("scheduleno"));
					}
				} else {	//	선택팀 선택 지역
					String sql="";
					sql += "select scheduleno from schedule";
					sql += " where hometeam in(select teamname from team";
					sql += "  where region = ?) and";
					sql += " (hometeam = ? or awayteam = ?) and gamedate > sysdate";
					
					ps = conn.prepareStatement(sql);
					ps.setString(1, region);
					ps.setString(2, team);
					ps.setString(3, team);
					rs = ps.executeQuery();
					
					while (rs.next()) {
						searchList.add(rs.getInt("scheduleno"));
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return searchList;
	}
	
	@Override
	public int getSelectCntAll(int scheduleno, String team) {
		int totalCount = 0;
		
		try {
		if("all".equals(team)) {
			String sql = "";
			sql += "SELECT count(*)";
			sql += " FROM board";
			sql += " where scheduleno = ?";

			ps = conn.prepareStatement(sql);
			ps.setInt(1, scheduleno);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				totalCount = rs.getInt(1);
			}
		} else {
			String sql = "";
			sql += "SELECT count(*)";
			sql += " FROM board";
			sql += " where scheduleno = ? and team = ?";

			ps = conn.prepareStatement(sql);
			ps.setInt(1, scheduleno);
			ps.setString(2, team);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				totalCount = rs.getInt(1);
			}
		}
		
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null)	rs.close();
				if(ps!=null) 	ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalCount;
				
	}

	@Override
	public List searchBoard(Paging paging, String event, String team, String region) {
		
		List<Board> searchBoard = new ArrayList();
		
		try {
			if("all".equals(team)) {
				if("all".equals(region)) {
					if("1".equals(event)) {	//	야구 모든 팀 모든 지역 검색
						String sql="";
						sql += "SELECT * FROM (";
						sql += " SELECT rownum rnum, B.* FROM (";
						sql += " 	SELECT boardno, nickname, title, content, scheduleno, team, insertdate, hit FROM board";
						sql += "	where scheduleno in(";
						sql += "	select scheduleno from schedule where team in(";
						sql += "	select teamname from team ";
						sql += "	where event = 1) and gamedate > sysdate)";
						sql += " 	ORDER BY boardno DESC)B";
						sql += " ORDER BY rnum)";
						sql += " WHERE rnum BETWEEN ? AND ?";
						
						ps = conn.prepareStatement(sql);
						ps.setInt(1, paging.getStartNo()); 
						ps.setInt(2, paging.getEndNo()); 
						rs = ps.executeQuery();
						
						while (rs.next()) {
							Board board = new Board();
							
							board.setBoardno(rs.getInt("boardno"));
							board.setNickname(rs.getString("nickname"));
							board.setTitle(rs.getString("title"));
							board.setContent(rs.getString("content"));
							board.setScheduleno(rs.getInt("scheduleno"));
							board.setTeam(rs.getString("team"));
							board.setInsertdate(rs.getDate("insertdate"));
							board.setHit(rs.getInt("hit"));
							
							searchBoard.add(board);
						}
					} else {	//	축구 모든 팀 모든 지역 검색
						String sql="";
						sql += "SELECT * FROM (";
						sql += " SELECT rownum rnum, B.* FROM (";
						sql += " 	SELECT boardno, nickname, title, content, scheduleno, team, insertdate, hit FROM board";
						sql += "	where scheduleno in(";
						sql += "	select scheduleno from schedule where team in(";
						sql += "	select teamname from team ";
						sql += "	where event = 2) and gamedate > sysdate)";
						sql += " 	ORDER BY boardno DESC)B";
						sql += " ORDER BY rnum)";
						sql += " WHERE rnum BETWEEN ? AND ?";
						
						ps = conn.prepareStatement(sql);
						ps.setInt(1, paging.getStartNo()); 
						ps.setInt(2, paging.getEndNo()); 
						rs = ps.executeQuery();
						
						while (rs.next()) {
							Board board = new Board();
							
							board.setBoardno(rs.getInt("boardno"));
							board.setNickname(rs.getString("nickname"));
							board.setTitle(rs.getString("title"));
							board.setContent(rs.getString("content"));
							board.setScheduleno(rs.getInt("scheduleno"));
							board.setTeam(rs.getString("team"));
							board.setInsertdate(rs.getDate("insertdate"));
							board.setHit(rs.getInt("hit"));
							
							searchBoard.add(board);
						}
					}
				} else {
					if("1".equals(event)) {	//	야구 모든 팀 선택 지역 검색
						String sql="";
						sql += "SELECT * FROM (";
						sql += " SELECT rownum rnum, B.* FROM (";
						sql += " 	SELECT boardno, nickname, title, content, scheduleno, team, insertdate, hit FROM board";
						sql += "	where scheduleno in(";
						sql += "	select scheduleno from schedule ";
						sql += "	where hometeam in (";
						sql	+= "	select teamname from team where region = ?";
						sql += "	and event = 1)and gamedate > sysdate)";
						sql += " 	ORDER BY boardno DESC)B";
						sql += " ORDER BY rnum)";
						sql += " WHERE rnum BETWEEN ? AND ?";
						
						ps = conn.prepareStatement(sql);
						ps.setString(1, region);
						ps.setInt(2, paging.getStartNo()); 
						ps.setInt(3, paging.getEndNo()); 
						rs = ps.executeQuery();
						
						while (rs.next()) {
							Board board = new Board();
							
							board.setBoardno(rs.getInt("boardno"));
							board.setNickname(rs.getString("nickname"));
							board.setTitle(rs.getString("title"));
							board.setContent(rs.getString("content"));
							board.setScheduleno(rs.getInt("scheduleno"));
							board.setTeam(rs.getString("team"));
							board.setInsertdate(rs.getDate("insertdate"));
							board.setHit(rs.getInt("hit"));
							
							searchBoard.add(board);
						}
					} else {	//	축구 모든 팀 선택 지역 검색
						String sql="";
						sql += "SELECT * FROM (";
						sql += " SELECT rownum rnum, B.* FROM (";
						sql += " 	SELECT boardno, nickname, title, content, scheduleno, team, insertdate, hit FROM board";
						sql += "	where scheduleno in(";
						sql += "	select scheduleno from schedule ";
						sql += "	where hometeam in (";
						sql	+= "	select teamname from team where region = ?";
						sql += "	and event = 2)and gamedate > sysdate)";
						sql += " 	ORDER BY boardno DESC)B";
						sql += " ORDER BY rnum)";
						sql += " WHERE rnum BETWEEN ? AND ?";
						
						ps = conn.prepareStatement(sql);
						ps.setString(1, region);
						ps.setInt(2, paging.getStartNo()); 
						ps.setInt(3, paging.getEndNo()); 
						rs = ps.executeQuery();
						
						while (rs.next()) {
							Board board = new Board();
							
							board.setBoardno(rs.getInt("boardno"));
							board.setNickname(rs.getString("nickname"));
							board.setTitle(rs.getString("title"));
							board.setContent(rs.getString("content"));
							board.setScheduleno(rs.getInt("scheduleno"));
							board.setTeam(rs.getString("team"));
							board.setInsertdate(rs.getDate("insertdate"));
							board.setHit(rs.getInt("hit"));
							
							searchBoard.add(board);
						}
					}
				}
			} else {
				if("all".equals(region)) { //	선택팀 모든 지역
					String sql="";
					sql += "SELECT * FROM (";
					sql += " SELECT rownum rnum, B.* FROM (";
					sql += " 	SELECT boardno, nickname, title, content, scheduleno, team, insertdate, hit FROM board";
					sql += "	where scheduleno in(";
					sql += "	select scheduleno from schedule where";
					sql += "	(hometeam=? or awayteam=?) and gamedate > sysdate) and team=?";
					sql += " 	ORDER BY boardno DESC)B";
					sql += " ORDER BY rnum)";
					sql += " WHERE rnum BETWEEN ? AND ?";
					
					ps = conn.prepareStatement(sql);
					ps.setString(1, team);
					ps.setString(2, team);
					ps.setString(3, team);
					ps.setInt(4, paging.getStartNo()); 
					ps.setInt(5, paging.getEndNo()); 
					rs = ps.executeQuery();
					
					while (rs.next()) {
						Board board = new Board();
						
						board.setBoardno(rs.getInt("boardno"));
						board.setNickname(rs.getString("nickname"));
						board.setTitle(rs.getString("title"));
						board.setContent(rs.getString("content"));
						board.setScheduleno(rs.getInt("scheduleno"));
						board.setTeam(rs.getString("team"));
						board.setInsertdate(rs.getDate("insertdate"));
						board.setHit(rs.getInt("hit"));
						
						searchBoard.add(board);
					}
				} else {	//	선택팀 선택 지역
					String sql="";
					sql += "SELECT * FROM (";
					sql += " SELECT rownum rnum, B.* FROM (";
					sql += " 	SELECT boardno, nickname, title, content, scheduleno, team, insertdate, hit FROM board";
					sql += "	where scheduleno in(";
					sql += "	select scheduleno from schedule where";
					sql += "	hometeam in (select teamname from team where region = ?)";
					sql	+= "	and gamedate > sysdate) and team=?";
					sql += " 	ORDER BY boardno DESC)B";
					sql += " ORDER BY rnum)";
					sql += " WHERE rnum BETWEEN ? AND ?";
					
					ps = conn.prepareStatement(sql);
					ps.setString(1, region);
					ps.setString(2, team);
					ps.setInt(3, paging.getStartNo()); 
					ps.setInt(4, paging.getEndNo()); 
					rs = ps.executeQuery();
					
					while (rs.next()) {
						Board board = new Board();
						
						board.setBoardno(rs.getInt("boardno"));
						board.setNickname(rs.getString("nickname"));
						board.setTitle(rs.getString("title"));
						board.setContent(rs.getString("content"));
						board.setScheduleno(rs.getInt("scheduleno"));
						board.setTeam(rs.getString("team"));
						board.setInsertdate(rs.getDate("insertdate"));
						board.setHit(rs.getInt("hit"));
						
						searchBoard.add(board);
					}
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null)	rs.close();
				if(ps!=null) 	ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return searchBoard;
	}

	@Override
	public List selectBoardByScheNo(Paging paging, int sno) {
		String sql = "";
		sql += "SELECT * FROM (";
		sql += " SELECT rownum rnum, B.* FROM (";
		sql += " 	SELECT boardno, nickname, title, content, scheduleno, team, insertdate, hit FROM board";
		sql += "	where scheduleno = ?";
		sql += " 	ORDER BY boardno DESC)B";
		sql += " ORDER BY rnum)";
		sql += " WHERE rnum BETWEEN ? AND ?";
		
		List list = new ArrayList();
		
		try {
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, sno); 
			ps.setInt(2, paging.getStartNo()); 
			ps.setInt(3, paging.getEndNo()); 
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Board board = new Board();
				
				board.setBoardno(rs.getInt("boardno"));
				board.setNickname(rs.getString("nickname"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setScheduleno(rs.getInt("scheduleno"));
				board.setTeam(rs.getString("team"));
				board.setInsertdate(rs.getDate("insertdate"));
				board.setHit(rs.getInt("hit"));
				
				list.add(board);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null)
				try {
					if(rs!=null)	rs.close();
					if(ps!=null)	ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			
		}
		return list;
	}

	@Override
	public int getSelectbyScheNo(int sno) {
		
		String sql = "";
		sql += "SELECT count(*)";
		sql += " FROM board where scheduleno = ?";
		
		int totalCount = 0;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, sno);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				totalCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null)	rs.close();
				if(ps!=null) 	ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalCount;
	}

	@Override
	public List getSchedule() {
		
		String sql = "";
		sql += "SELECT scheduleno, hometeam, awayteam, gamedate";
		sql += " FROM schedule order by scheduleno";
		
		List list = new ArrayList();
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setScheduleno(rs.getInt("scheduleno"));
				schedule.setHometeam(rs.getString("hometeam"));
				schedule.setAwayteam(rs.getString("awayteam"));
				schedule.setGamedate(rs.getDate("gamedate"));
				
				list.add(schedule);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}