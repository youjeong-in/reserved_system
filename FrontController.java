package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.services.Authentication;
import beans.Action;
import jobs.services.RestaurantService;
import jobs.services.UserService;

@WebServlet({"/LogIn", "/DupCheck", "/Join", "/LogOut", "/JoinForm", "/LogInForm", "/Search", "/DashBoard", "/Waiting", "/TodayReserved", "/ConfirmReserve", "/Reserve","/Orders"})
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Authentication auth;
	private UserService user;
	private RestaurantService rService;
	
    public FrontController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Action action = null;
		String jobCode = 
				request.getRequestURI().substring(request.getContextPath().length()+1);
		
		if(jobCode.equals("JoinForm")) {
			action = new Action();
			action.setPage("join.jsp");
			action.setRedirect(true);
		}else if(jobCode.equals("LogInForm")) {
			action = new Action();
			action.setPage("access.jsp");
			action.setRedirect(true);
		}else if(jobCode.equals("Search")) {
			user = new UserService(); 
			action = user.backController(1, request);
			
		}else{
			action = new Action();
			action.setPage("index.html");
			action.setRedirect(true);
		}
		
		if(action.isRedirect()) {
			response.sendRedirect(action.getPage());
		}else {
			RequestDispatcher rd = request.getRequestDispatcher(action.getPage());
			rd.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		Action action = null;
		String jobCode = 
				request.getRequestURI().substring(request.getContextPath().length()+1);
		
		if(jobCode.equals("LogIn")){
			auth = new Authentication();
			action = auth.backController(1, request);
			//session.setAttribute("user", request.getParameter("uCode"));
		}else if(jobCode.equals("DupCheck")) {
			auth = new Authentication();
			action = auth.backController(3, request);
		}else if(jobCode.equals("Join")) {
			auth = new Authentication();
			action = auth.backController(2, request);
		}else if(jobCode.equals("LogOut")) {
			auth = new Authentication();
			action = auth.backController(-1, request);
		}else if(jobCode.equals("DashBoard")) {
			rService = new RestaurantService();
			action = rService.backController(1, request);
		}else if(jobCode.equals("Waiting")){
			rService = new RestaurantService();
			action = rService.backController(2, request);
		}else if(jobCode.equals("TodayReserved")){
			rService = new RestaurantService();
			action = rService.backController(3, request);
		}else if(jobCode.equals("ConfirmReserve")){
			rService = new RestaurantService();
			action = rService.backController(4, request);
		}else if(jobCode.equals("Reserve")) {
			user = new UserService();
			action = user.backController(2, request);
			//System.out.println(request.getParameter("reCode"));
		}else if(jobCode.equals("Orders")) {
			user = new UserService();
			 action=user.backController(3, request);
		}else if(jobCode.equals("Search")) {
			user = new UserService(); 
			action = user.backController(1, request);
			
		}
		else {}
		
		if(action.isRedirect()) {
			response.sendRedirect(action.getPage());
		}else {
			RequestDispatcher rd = request.getRequestDispatcher(action.getPage());
			rd.forward(request, response);
		}
		
	}

}
