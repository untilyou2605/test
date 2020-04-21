/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dal.userDAO;
import entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author anhnb
 */
public class loginCon extends HttpServlet {

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
        request.getRequestDispatcher("login.jsp").forward(request, response);

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
        userDAO userDAO = new userDAO();
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        
        
        try {
        int err = 0;
        String mess = "";
        if(userName==null || password==null){
            err=1;
            request.setAttribute("mess", mess+="user name  or password can not be null!! ");
        }
        
        if (userName.equals("")) {
            err = 1;
            request.setAttribute("mess", mess+="user name can not be empty!! ");
        }
        if (password.equals("")) {
            err = 2;
            request.setAttribute("mess",mess+="  password can not be empty!!");
        }
        if (err != 0) {
            request.setAttribute("username", userName);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        //get user name from data base 
        User user = null;
        try {
            user = userDAO.getUser(userName, password);
        } catch (Exception ex) {
            request.setAttribute("mess", "can not load data!!!");
        }
        //check if get user or not
        if (user != null) {
            request.getSession().setAttribute("user", user);
            response.sendRedirect("takeQuiz");
        }
        else{
           request.setAttribute("mess", "username or password is wrong");
           request.getRequestDispatcher("login.jsp").forward(request, response);
        }
            } catch (Exception e) {
               response.sendRedirect("error.jsp");
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

}
