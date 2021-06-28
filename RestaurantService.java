package jobs.services;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import beans.Action;
import beans.ReservedInfo;

public class RestaurantService {
	DataAccessObject dao;
	
	public RestaurantService() {
		this.dao = null;
	}
	
	public Action backController(int serviceCode, HttpServletRequest req) {
		Action action = null;
		
		switch(serviceCode) {
		case 1:
			action = this.restaurantMainServicesCtl(req);
			break;
		case 2:
			action = this.restaurantMainServicesCtl(req);
			break;
		case 3:
			action = this.restaurantMainServicesCtl(req);
			break;
		case 4:
			action = this.confirmReserveCtl(req);
			break;
		default:
		}
		
		return action;
	}
	
	private Action restaurantMainServicesCtl(HttpServletRequest req) {
		Action action = new Action();
		// request --> bean :: ReservedInfo
		ReservedInfo ri = new ReservedInfo();
		ri.setReCode(req.getParameter("uCode"));
		ri.setProcess("W");
		
		dao = new DataAccessObject();
		dao.dbOpen();
		req.setAttribute("WaitingList", this.makeHtml(this.getWaitingInfo(ri), true));
		req.setAttribute("TodayReservedList", this.makeHtml(this.getTodayReservedInfo(ri), false));
		
		dao.dbClose();
		
		action.setPage("rMain.jsp");
		action.setRedirect(false);
		
		return action;
	}
	
	private Action confirmReserveCtl(HttpServletRequest req) {
		Action action = new Action();
		String message = null;
		boolean tran = false;
		
		ReservedInfo ri = new ReservedInfo();
		ri.setReCode(req.getParameter("reCode"));
		ri.setCuId(req.getParameter("cuCode"));
		ri.setDbDate(req.getParameter("dbDate"));
		ri.setProcess("W");
		
		dao = new DataAccessObject();
		dao.dbOpen();
		dao.setTranConf(tran);
		if(this.confirmReserve(ri)) {
			message = ri.getCuId() + "님의 예약이 확정되었습니다.";
			tran = true;
		}else {
			message = "다시 시도해 주세요";
		}
		dao.setTran(tran);
		
		req.setAttribute("message", message);
		req.setAttribute("WaitingList", this.makeHtml(this.getWaitingInfo(ri), true));
		req.setAttribute("TodayReservedList", this.makeHtml(this.getTodayReservedInfo(ri), false));
		
		
		dao.dbClose();
	
		action.setPage("rMain.jsp");
		action.setRedirect(false);
		
		return action;
	}
	
	private boolean confirmReserve(ReservedInfo ri) {
		return this.convertData(this.dao.confirmReserve(ri));
	}
	
	private boolean convertData(int data) {
		return (data==1)? true: false; 
	}
	
	private Action waitingInfoCtl(HttpServletRequest req) {
		Action action = new Action();
		// request --> bean :: ReservedInfo
		ReservedInfo ri = new ReservedInfo();
		ri.setReCode(req.getParameter("uCode"));
		ri.setProcess("W");
		
		dao = new DataAccessObject();
		dao.dbOpen();
		req.setAttribute("WaitingList", this.makeHtml(this.getWaitingInfo(ri), true));
		dao.dbClose();
		
		action.setPage(null);
		action.setRedirect(false);
				
		return action;
	}
	
	private Action todayReservedCtl(HttpServletRequest req) {
		Action action = new Action();
		// request --> bean :: ReservedInfo
		ReservedInfo ri = new ReservedInfo();
		ri.setReCode(req.getParameter("uCode"));
		
		dao = new DataAccessObject();
		dao.dbOpen();
		req.setAttribute("TodayReservedList", this.makeHtml(this.getTodayReservedInfo(ri), false));
		dao.dbClose();
		
		action.setPage(null);
		action.setRedirect(false);
		return action;
	} 
	
	private ArrayList<ReservedInfo> getWaitingInfo(ReservedInfo ri) {		
		return dao.getWaitingInfo(ri);
	}
	
	private ArrayList<ReservedInfo> getTodayReservedInfo(ReservedInfo ri) {
		return dao.getTodayReservedInfo(ri);
	}
	
	private String makeHtml(ArrayList<ReservedInfo> ri, boolean type) {
		System.out.println(ri.size());
		StringBuffer sb = new StringBuffer();
		sb.append("<div name=\'list\' class=\'info\'>");
		sb.append("<div class=\'func\'>");
		sb.append("<input type=\'button\' class=\'big-btn\' value=\'예약대기현황\' onClick=\'showDiv(true)\'/>");
		sb.append("<input type=\'button\' class=\'big-btn\' value=\'금일예약현황\' onClick=\'showDiv(false)\'/>");
		sb.append("</div>");
		sb.append("<div class=\'header\'>");
		sb.append("<div class=\'col\' style=\'width:20%\'>레스토랑</div>");
		sb.append("<div class=\'col\' style=\'width:25%\'>예약자명</div>");
		if(type) {
			sb.append("<div class=\'col\' style=\'width:40%\'>예약일정</div>");
			sb.append("<div class=\'col\' style=\'width:15%\'>예약인원</div>");
		}else {
			sb.append("<div class=\'col\' style=\'width:20%\'>전화번호</div>");
			sb.append("<div class=\'col\' style=\'width:25%\'>예약메뉴</div>");
			sb.append("<div class=\'col\' style=\'width:10%\'>수량</div>");
		}
		
		sb.append("</div>");
		for(ReservedInfo record : ri) {
			if(type) {
				sb.append("<div class=\'record\' onClick=\"selectRestaurant(true , \'"+ record.getReCode() +"\' , \'"+ record.getCuId()+ "\', \'"+ record.getDbDate() + "\')\">");
			}else {
				sb.append("<div class=\'record\' onClick=\"selectRestaurant(false , \'"+ record.getReCode() +"\' , \'"+ record.getCuId()+ "\', \'"+ record.getDbDate() + "\', \'"+ record.getmCode() +"\')\">");
			}
			sb.append("<div class=\'col\' style=\'width:20%\'>" + record.getReName() + "</div>");
			sb.append("<div class=\'col\' style=\'width:25%\'>" + record.getCuName() + "(" + record.getCuId() + ")"+"</div>");
			if(type) {
				sb.append("<div class=\'col\' style=\'width:40%\'>" + record.getrDate() + "</div>");
				sb.append("<div class=\'col\' style=\'width:15%\'>" + record.getmCount() + "</div>");
			}else {
				sb.append("<div class=\'col\' style=\'width:20%\'>" + record.getPhone() + "</div>");
				sb.append("<div class=\'col\' style=\'width:25%\'>" + record.getmName() + "</div>");
				sb.append("<div class=\'col\' style=\'width:10%\'>" + record.getQuantity() + "</div>");
			}
			
			sb.append("</div>");
		}
		sb.append("</div>");
		return sb.toString(); 
	}
}
