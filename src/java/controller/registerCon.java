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
public class registerCon extends HttpServlet {

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
        request.getRequestDispatcher("register.jsp").forward(request, response);
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
        try {
            //get information in form and check if valid or not
            
            String username;
            String password;
            String email;
            String type;
            userDAO uDAO = new userDAO();
            username = request.getParameter("username").trim();
            password = request.getParameter("password").trim();
            email = request.getParameter("email").trim();
            type = request.getParameter("userType");
            boolean isExist = false;
            try {
                isExist = uDAO.getUserExist(username);
            } catch (Exception ex) {
                throw ex;
            }
            int err = 0;
            String mess = "Error :";
            if (username.equals("")) {
                err = 1;
                mess += "username cannot be empty!!";
            } else if (password.equals("")) {
                err = 2;
                mess += "password cannot be empty!!";
            } else if (email.equals("")) {
                err = 3;
                mess += "email cannot be empty!!";
            } else if (!email.toUpperCase().matches("[A-Z0-9]{1,}+@[A-Z0-9]{1,}+[(\\.+[A-Z]{1,})]{1,}")) {
                err = 4;
                mess += "email is not valid format!!";
            } else if (isExist) {
                err = 5;
                mess += "user name already exist!!";
            }
            
            //if no get error send to register form again.
            if (err != 0) {
                request.setAttribute("username", username);
                //request.setAttribute("password", password);
                request.setAttribute("email", email);
                request.setAttribute("userType", type);
                request.setAttribute("mess", mess);
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else {
                boolean isTeacher = false;
                if (type.equals("1")) {
                    isTeacher = true;
                }
                User user = new User(username, password, isTeacher, email) ;
                uDAO.insertUser(user);
                 request.getSession().setAttribute("user", user);
            response.sendRedirect("takeQuiz");
            }
        } catch (Exception ex) {
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
