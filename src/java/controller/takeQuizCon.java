/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dal.quizDAO;
import entity.Question;
import entity.User;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author anhnb
 */
public class takeQuizCon extends baseAuthentication {

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
        request.getRequestDispatcher("takeQuiz.jsp").forward(request, response);

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
        //check if the user submitted it or not
        String isSubmit = request.getParameter("isSubmit");
        //If not, then post question
        if (isSubmit == null) {
            quizDAO qDAO = new quizDAO();
            try {
                String raw_numOfQuestion = request.getParameter("number");
                //the number of quiz must to smaller than total quiz or lager than 0
                int numOfQuestion = 0;
                int err = 0;
                String mess = "Err: ";
                //check in put of nummber of question

                if (raw_numOfQuestion == null || raw_numOfQuestion.equals("")) {
                    err = 1;
                    mess += " number is empty!!!";
                    request.setAttribute("mess", mess);
                } else {
                    try {
                        numOfQuestion = Integer.parseUnsignedInt(raw_numOfQuestion);
                    } catch (Exception ex) {
                        err = 1;
                        mess += " must to lager than 0!!!";
                        request.setAttribute("mess", mess);
                    }
                }

                int real_NumOfQuestion = 0;
                //get number of question in database to compare
                try {
                    real_NumOfQuestion = qDAO.getNumOfQuestion();
                } catch (Exception ex) {
                    request.setAttribute("messData", " can not load data from sql!!!");
                }
                //compare between number of question
                //that user input and the real number
                if (numOfQuestion > real_NumOfQuestion || numOfQuestion == 0) {
                    err = 2;
                    mess += "  not valid number of question!!!";
                    request.setAttribute("mess", mess);
                }

                if (err != 0) {
                    request.getRequestDispatcher("takeQuiz.jsp").forward(request, response);
                } else {
                    request.setAttribute("numOfQuestion", numOfQuestion);
                    //user takeQuizAnser to display quiz to the take quiz answer page
                    takeQuizAnser(request, response);
                }
            } catch (Exception ex) {
                response.sendRedirect("error.jsp");
            }
        } else {
            //if user is have been submit
            //perfomed funtion submit answer
            submitAnser(request, response);
        }

    }
    
    //get current time
    protected long getTimeNowInMillies() {
        return Calendar.getInstance().getTimeInMillis();
    }
    
    //get time after do quiz that user have to submit
    protected long getTimeAfterAdd(int min, int sec) {
        int ONE_MINUTE_IN_MILLIS = 60000;//millisecs
        int ONE_SECOND_IN_MILLIS = 1000;
        int time_delay = 3000;
        return getTimeNowInMillies() + (min * ONE_MINUTE_IN_MILLIS) + (sec * ONE_SECOND_IN_MILLIS) + time_delay;
    }

    protected void takeQuizAnser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset = UTF-8");
        Context context;
        int timePerQ = 0;

        try {
            //get time per quiz in context
            try {
                InitialContext initial = new InitialContext();
                context = (Context) initial.lookup("java:comp/env");
                timePerQ = Integer.parseInt(context.lookup("TPerQ").toString());
            } catch (Exception ex) {
                request.setAttribute("mess", "can not find time per seconds");
            }
            //caculated time 
            int num = 0;
            quizDAO qAO = new quizDAO();
            int raw_num = (int) request.getAttribute("numOfQuestion");
            int min = raw_num * timePerQ / 60;
            int sec = raw_num * timePerQ - min * 60;

            
            ArrayList<Question> questions;
            questions = qAO.getQuestion(raw_num);
            if (questions.isEmpty()) {
                request.setAttribute("mess", "canot load data!!!");
            } else {
                //get arraylist real answer
                ArrayList<String> answerList = new ArrayList<>();
                for (Question q : questions) {
                    answerList.add(q.getAnswer());
                }
                //declare real anser and time to submit
                request.getServletContext().setAttribute("answerList", answerList);
                request.getServletContext().setAttribute("timeToSubmit", getTimeAfterAdd(min, sec));

                request.setAttribute("totalQ", raw_num);
                request.setAttribute("min", min);
                request.setAttribute("sec", sec);
                request.setAttribute("questions", questions);
            }
            request.getRequestDispatcher("takeQuizAnser.jsp").forward(request, response);
        } catch (Exception ex) {
            response.sendRedirect("error.jsp");
        }

    }
    
    //format number 
     protected String formatNum(float x){
        if(0-x==0) return "0";
        DecimalFormat d = new DecimalFormat("#.00");
        String a = d.format(x);
        if(x - Math.floor(x)==0){
            String[] b = a.split("\\.");
            return b[0];
        } else if(a.endsWith("0"))return a.substring(0, a.length()-1);
        return a;
    }
    
    
    protected void submitAnser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset = UTF-8");
        long submitTime = getTimeNowInMillies();
        long timeToSubmit = (long) request.getServletContext().getAttribute("timeToSubmit");
        
         float point1=0;
        
        
        if (timeToSubmit < submitTime) {
            request.setAttribute("message", "Over Time!!!");
        } else {
            //get the list of checkbox answer
            String[] answer_in = request.getParameterValues("answer");
            //if the answer not null caculated point 
            //else point =0
            if (answer_in != null) {
                //get the truth answer list of question
                ArrayList<String> answerList = (ArrayList<String>) request.getServletContext().getAttribute("answerList");

                //ArrayList<Question> questions = (ArrayList<Question>) request.getAttribute("questions");
                ArrayList<String> answer_out;
                answer_out = new ArrayList<>();
                String a = "";
                int raw_point = 0;
                //get the list of question that check by user
                for (int i = 0; i < answerList.size(); i++) {
                    a = "";
                    for (String x : answer_in) {
                        if (x.startsWith("" + i)) {
                            a += x.charAt(1);
                        }
                    }
                    answer_out.add(a);
                }
                //compare between the truth answer and user answer
                for (int i = 0; i < answerList.size(); i++) {
                    if (answerList.get(i).equals(answer_out.get(i))) {
                        raw_point++;
                    }
                }
               
                 point1 = ((float) raw_point) / answerList.size() * 10;
                float pointP1 = ((float) raw_point) / answerList.size() * 100;
                
                String point = formatNum(point1);
                String pointP = formatNum(pointP1);
                
                request.setAttribute("point", point);
                request.setAttribute("pointP", pointP);

            } else {
                request.setAttribute("point", 0);
                request.setAttribute("pointP", 0);
            }
        }
        HttpSession session = request.getSession();
        
        quizDAO qDAO = new quizDAO();
        User user = new User();
        user = (User) session.getAttribute("user");
        try {
            qDAO.insertHistory(user.getId(),(float) point1);
        } catch (Exception ex) {
            request.setAttribute("message", "can not point to database");
        }
        
        
        request.getRequestDispatcher("takeQuizPass.jsp").forward(request, response);
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
