package auth.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import beans.Action;
import beans.Member;

public class Authentication {
	private Action action;
	private Member member;
	private DataAccessObject dao;
	private HttpSession session;

	public Authentication() {

	}

	public Action backController(int ServiceCode, HttpServletRequest req) {

		switch(ServiceCode) {
		case 1: // 로그인
			this.logInCtl(req);
			break;
		case -1: //로그아웃
			this.logOutCtl(req);
			break;
		case 3: //중복검사
			this.dupCheckCtl(req);
			break;
		case 2: //회원가입
			this.joinCtl(req);
			break;
		}
		return action;
	}

	private void logOutCtl(HttpServletRequest req) {
		action = new Action();

		HttpSession session = req.getSession();
		session.invalidate();
		action.setPage("access.jsp");
		action.setRedirect(true);
	}
	
	private void logInCtl(HttpServletRequest req) {
		action = new Action();
		action.setPage("access.jsp");
		action.setRedirect(false);
		
		String message = "아이디나 패스워드를 확인해 주세요!";

		// bean에 데이터 저장 :: Member Bean
		member = new Member();
		member.setAccessType(req.getParameter("accessType"));
		member.setMemberId(req.getParameter("uCode"));
		member.setMemberPassword(req.getParameter("aCode"));
		
		dao = new DataAccessObject();
		dao.dbOpen();

		if(this.isUserId()) {
			if(this.isAccess()) {
				// history table >> 로그인 정보 저장
				// HttpSession 처리
				session = req.getSession(true);
				session.setAttribute("user", member.getMemberId());
				
				member.setMemberPassword(null);
				this.getUserInfo();
				req.setAttribute("info", this.member);
				action.setPage((member.getAccessType().equals("G"))? "cMain.jsp" : "DashBoard");				
			}else {
				req.setAttribute("message", message);
			}
		}else {
			req.setAttribute("message", message);
		}
		
		dao.dbClose();
	}
	
	private boolean isUserId() {
		return this.convertData(member.getAccessType().equals("G")? this.dao.isUserId(member) : this.dao.isRestaurantCode(member));
	}
	private boolean isAccess() {
		return this.convertData(member.getAccessType().equals("G")? this.dao.isAccess(member) : this.dao.isRestaurantAccess(member));
	}
	private void getUserInfo() {
		if(member.getAccessType().equals("G")) {
			this.dao.getUserInfo(member);
		}else {
			this.dao.getRestaurantInfo(member);
		}
	}

	private boolean convertData(int data) {
		return (data==1)? true: false; 
	}

	private void dupCheckCtl(HttpServletRequest req) {
		action = new Action();
		action.setPage("join.jsp");
		action.setRedirect(false);
		String message = "사용 불가능한 아이디입니다.";
		
		dao = new DataAccessObject();
		dao.dbOpen();

		// Member Bean에 id injection
		member = new Member();
		member.setAccessType(req.getParameter("accessType"));
		member.setMemberId(req.getParameter("uCode"));

		// isUserId(Member)  : true >> 중복   false >> 사용가능 아이디
		if(!this.isUserId()) {
			// BackEnd 응답
			message = "사용 가능한 아이디입니다.";
			req.setAttribute("uCode", member.getMemberId());	
		}

		dao.dbClose();
		req.setAttribute("message", message);
	}

	private void joinCtl(HttpServletRequest req) {
		action = new Action();
		action.setPage("join.jsp");
		action.setRedirect(false);
		
		// Memeber Bean에 Client로부터 전송된 데이터 담기
		member = new Member();
		member.setAccessType(req.getParameter("accessType"));
		member.setMemberId(req.getParameter("uCode"));
		member.setMemberPassword(req.getParameter("aCode"));
		member.setMemberName(req.getParameter("uName")); // uName, restaurantName
		member.setMemberEtc(req.getParameter("uPhone")); // uPhone, ceoName
		if(member.getAccessType().equals("R")) {
			member.setCategoryCode(req.getParameter("rType"));
			member.setLocation(req.getParameter("location"));
		}

		// DataAccessObject 활성화
		dao = new DataAccessObject();
		dao.dbOpen();
		// setNewMember() << 일반 회원 가입
		if(this.setNewMember()) {
			action.setPage("access.jsp");
			action.setRedirect(true);
		}
		dao.dbClose();
	}

	private boolean setNewMember() {
		int result;
		if(member.getAccessType().equals("G")) {
			result = dao.setNewMember(member);
		}else {
			result = dao.setNewRestaurantMember(member);
		}
		return this.convertData(result);
	}

}



/* 원활한 데이터의 흐름 및 관리를 위해 데이터의 패턴화 
 * VO(value object) ~ DTO(data transfer object) 
 * 			--> 여러개의 분리된 데이터를 하나의 공간에 모아 저장하는 기술
 *          --> Bean : 타입이 다른 여러개의 데이터를 하나의 클래스에 저장하고 활용하는 기술
 * 
 * 활용 
 *   ~Ctl() : bean 생성 --> 클라이언트로 부터 전달 받은 데이터를 저장
 *                     --> 해당 잡의 모든 프로세스에서 필요한 데이터는 bean을 이용함.
 * */

