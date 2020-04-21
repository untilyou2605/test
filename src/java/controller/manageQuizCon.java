/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dal.quizDAO;
import entity.Question;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author anhnb
 */
public class manageQuizCon extends baseAuthentication {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         super.doGet(request, response);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset = UTF-8");

       
        quizDAO qDAO = new quizDAO();
        //the number of quiz per one page
        int pagesize = 3;
        //the space of paging
        int pagegap = 2;
        //the current page index
        int pageindex = 0;
        //check page index
        String pageIndexErr ="";
        String raw_pageindex = request.getParameter("page");
        if (raw_pageindex == null) {
            raw_pageindex = "1";
        }
        if (raw_pageindex.matches("[0-9]+") == false) {
            request.setAttribute("pageIndexErr",pageIndexErr+="Enter page index again index\n");
        } else {
            pageindex = Integer.parseInt(raw_pageindex);
        }
        //count the number of quiz
        int count = 0;
        try {
            count = qDAO.quizCount();
        } catch (Exception ex) {
            request.setAttribute("mess", "can not load data from database");
        }
        //caculated the number of page to pagging
        int pagecount = (count % pagesize == 0) ? count / pagesize : count / pagesize + 1;
        
        ArrayList<Question> qList = null;
        try {
            qList = qDAO.getAllQuiz(pageindex, pagesize);
        } catch (Exception ex) {
            request.setAttribute("mess", "can not load data from database");
        }
        
        if(qList.isEmpty()){
            request.setAttribute("pageIndexErr", pageIndexErr+=" can not found any article!!");
        } 
        
        request.setAttribute("pagegap", pagegap);
        request.setAttribute("pagecount", pagecount);
        request.setAttribute("pageindex", pageindex);
        request.setAttribute("qList", qList);
        request.setAttribute("totalQ", count );
       
        request.getRequestDispatcher("manageQuiz.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //check condition to delete quiz, if condition is valid then delete
        quizDAO qDAO = new quizDAO();
         int id=0;
        String raw_Id = request.getParameter("qid");
         
         if (raw_Id.matches("[0-9]+") == false|| raw_Id ==null) {
            request.setAttribute("mess","Question ID invalid!!");
        } else {
          id = Integer.parseInt(raw_Id);
        }
        
        try {
            qDAO.deleteQuiz(id);
        } catch (Exception ex) {
            request.setAttribute("mess", "can not delete !!!");
        }
     
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
