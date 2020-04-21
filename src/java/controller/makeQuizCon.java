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
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author anhnb
 */
public class makeQuizCon extends baseAuthentication {

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
        request.getRequestDispatcher("makeQuiz.jsp").forward(request, response);
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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset = UTF-8");

        quizDAO quizDAO = new quizDAO();
        Question question;
        //get parameter of content
        String content = request.getParameter("content");

        //option
        ArrayList<String> optS = new ArrayList<>();

        optS.add(request.getParameter("opt1"));
        optS.add(request.getParameter("opt2"));
        optS.add(request.getParameter("opt3"));
        optS.add(request.getParameter("opt4"));

        //answers
        String ans = "";
        for (int i = 1; i <= 4; i++) {
            if (request.getParameter("ans" + i) != null) {
                ans += String.valueOf(i);
            }
        }

        int err = 0;
        //check the input value empty or not
        if (content.trim().equals("")) {
            err = 1;
        } else if (ans.equals("") || ans.equals("1234")) {
            err = 2;
        } else if (optS.get(0).trim().equals("") || optS.get(1).trim().equals("")
                || optS.get(2).trim().equals("") || optS.get(3).trim().equals("")) {
            err = 3;
        }
        //if empty set,send mess and the user have to remake quiz
        if (err != 0) {
            if (err == 1) {
                request.setAttribute("mess", "Content can not be empty!!!");
            }
            if (err == 2) {
                request.setAttribute("mess", "Answer can not be empty or choose them all!!!");
            }
            if (err == 3) {
                request.setAttribute("mess", "Options must be fill full");
            }
            request.setAttribute("content", content);

            if (ans.contains("1")) {
                request.setAttribute("ans1", "checked");
            }
            if (ans.contains("2")) {
                request.setAttribute("ans2", "checked");
            }
            if (ans.contains("3")) {
                request.setAttribute("ans3", "checked");
            }
            if (ans.contains("4")) {
                request.setAttribute("ans4", "checked");
            }

            request.setAttribute("opt1", optS.get(0));
            request.setAttribute("opt2", optS.get(1));
            request.setAttribute("opt3", optS.get(2));
            request.setAttribute("opt4", optS.get(3));
            request.getRequestDispatcher("makeQuiz.jsp").forward(request, response);
        } else {
            //if not error quiz will be add to database
            try {
                question = new Question(content, ans, optS, new Date());
                if (quizDAO.insertQuiz(question) == true) {
                    request.getRequestDispatcher("makeQuizSucc.jsp").forward(request, response);
                }

            } catch (Exception ex) {
                response.sendRedirect("error.jsp");
            }
        }
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

    private boolean noChar(String content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
