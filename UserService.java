package jobs.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import beans.Action;
import beans.ReservedInfo;
import beans.RestaurantInfo;

public class UserService {
	private DataAccessObject dao;

	public UserService() {
		this.dao = null;
	}

	public Action backController(int serviceCode, HttpServletRequest req) {
		Action action = null;

		switch(serviceCode) {
		case 1: 
			action = searchCtl(req);
			break;

		case  2 : 
			action = this.availableDayCtl(req);
			break;
		default:
		}

		return action;
	}
	
	public String backController(char serviceCode, HttpServletRequest req) { //오버로딩해줌
		String jsonData = null;
		
		switch(serviceCode) {
		case 'A': 
			jsonData = step2Ctl(req);
			break;

		case  'B' : 
			jsonData = step3Ctl(req);
			break;
			
		default:
		}

		return jsonData;
	}
	//특정가게의 메뉴리스트가져오기
	private String step2Ctl(HttpServletRequest req) {
		String jsonData = null;
		ReservedInfo ri = new ReservedInfo();
		ri.setReCode(req.getParameter("reCode"));
		ri.setrDate(req.getParameter("day"));
		
		dao = new DataAccessObject();
		dao.dbOpen();
		
		 jsonData = this.pStep2(ri);
		
		dao.dbClose();
		
		return jsonData;
	}
	
	private String pStep2(ReservedInfo ri){//메뉴리스트를 가져오는 메서드 
		String jsonData = null;
		Gson gson = new Gson();
		jsonData = gson.toJson(dao.getMenu(ri));
		//System.out.println(jsonData);
		return jsonData;
		
	}

	private String step3Ctl(HttpServletRequest req) {
		String jsonData = null;
		
		return jsonData;
	}


	//예약가능날짜
	private Action availableDayCtl(HttpServletRequest req) {
		ArrayList<String> dayList = null;
		ReservedInfo ri = null;
		HttpSession session = req.getSession();

		Action action = new Action();
		action.setPage("index.html");
		action.setRedirect(true);

		
		if(!session.isNew() && session.getAttribute("user")!=null) {//session.isNew true&false
			ri = new ReservedInfo();
			ri.setReCode(req.getParameter("reCode"));
			ri.setProcess("C");
			
			dao = new DataAccessObject();
			dao.dbOpen();
			dayList = this.getDay(7);
			this.compareDate(dayList, this.getReservedList(ri));
			//System.out.println(dayList.size());
			dao.dbClose();

			req.setAttribute("dayList", this.makeHtmlDayList(dayList,ri.getReCode()));
			action.setPage("cStep1.jsp");
			action.setRedirect(false);
		}
		return action;
	}

	private ArrayList<String> getReservedList(ReservedInfo ri) {

		return dao.getReservedList(ri);
	}
	
	//정책에 따른 날짜가져오기 0624
		private ArrayList<String> getDay(int days){
			ArrayList<String> dayList = new ArrayList<String>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

			for(int day=1; day <=days; day++) {
				Calendar calendar = Calendar.getInstance(); //윈도우상의 오늘날짜를 불러옴 요일까지 
				calendar.add(Calendar.DAY_OF_MONTH, day);
				dayList.add(sdf.format(calendar.getTime()));//date타입으로 바꿔줌
				
			}
			return dayList;
		}

		//0624 역으로 올라오면서 데이터 비교하기
		private void compareDate(ArrayList<String> dayList, ArrayList<String> reserved) {
			if(reserved.size() > 0) {
				for(int listIndex = (dayList.size()-1); listIndex>=0; listIndex--) {//listIndex가 db에서 가져온애
					for(int resIndex=0; resIndex < reserved.size(); resIndex++) {
						if(dayList.get(listIndex).equals(reserved.get(resIndex))) {
							dayList.remove(listIndex);
							break;
						}
					}
				}
			}	
		}
	
	private String makeHtmlDayList(ArrayList<String> list, String reCode) {
		StringBuffer sb = new StringBuffer();

		sb.append("<div id=\"dayList\">");
		sb.append("<div id=\"step\">DAY LIST</div>");
		for(String day : list) {
			sb.append("<div class=\"col\" onClick=\"callMenu(\'" + reCode +"\' , \'" + day + "\')\">"+day+"</div>");

		}
		sb.append("</div>");

		return sb.toString();
	}

	private Action searchCtl(HttpServletRequest req) {
		RestaurantInfo ri = new RestaurantInfo();
		ri.setWord(req.getParameter("word"));

		Action action = new Action();

		dao = new DataAccessObject();
		dao.dbOpen();
		req.setAttribute("list", this.makeHtml(this.search(ri)));
		dao.dbClose();

		action.setPage("cMain.jsp");
		action.setRedirect(false);

		return action;
	}

	private ArrayList<RestaurantInfo> search(RestaurantInfo ri) {
		return dao.getRestaurantInfo(ri);
	}


	private String makeHtml(ArrayList<RestaurantInfo> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table>");
		sb.append("<tr><th>레스토랑</th><th>분류</th><th>위치</th><th>메뉴</th><th>가격</th><th>평점</th><th>주문수</th><th>비고</th></tr>");
		for(RestaurantInfo ri : list) {
			sb.append("<tr onClick=\'reserve("+ri.getReCode()+")\'>");
			sb.append("<td>" + ri.getRestaurant() + "<input type=\'hidden\' name=\'rCode\' value=\'"+ ri.getReCode() +"\'</td>");
			sb.append("<td>" + ri.getCatagory() + "</td>");
			sb.append("<td>" + ri.getLocation() + "</td>");
			sb.append("<td>" + ri.getMenu() + "</td>");
			sb.append("<td>" + ri.getPrice() + "</td>");
			sb.append("<td>" + (ri.getGpa()/10.0) + "</td>");
			sb.append("<td>" + ri.getCount() + "</td>");
			sb.append("<td>" + ri.getComments() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		return sb.toString();
	}

	

}
