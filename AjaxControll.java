package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jobs.services.UserService;


@WebServlet({"/Step2","/Step3"})
public class AjaxControll extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserService us = null;
 
    public AjaxControll() {
        super();
      
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println(request.getParameter("reCode") + " : " + request.getParameter("day"));
		//response.setContentType("text/html; charset=UTF-8");
		//PrintWriter out = response.getWriter();
		//out.print("서버에서 응답");
		//out.close();
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//System.out.println(request.getParameter("reCode") + " : " + request.getParameter("day"));
		String jsonData = null;
		
		String jobCode = request.getRequestURI().substring(request.getContextPath().length()+1);
		
		if(jobCode.equals("Step2")) {
			us = new UserService();
			jsonData = us.backController('A', request);
			
		}else if(jobCode.equals("Step3")) {
			us = new UserService();
			jsonData = us.backController('B', request);
		}else {
			
		}
		
		
		//응답해줄 페이지
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonData);
		out.close();
	}

}
