/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author anhnb
 */
public class Question {
    private int id;
    private String content;
    private String answer;
    private List<String> opt;
    private Date dateCr;

    public Question(String content, String answer, List<String> opt, Date dateCr) {
        this.content = content;
        this.answer = answer;
        this.opt = opt;
        this.dateCr = dateCr;
    }

    public Question() {
        this.opt = new ArrayList<String>();
    }
    public void setID(int id){
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getOpt() {
        return opt;
    }

    public void setOpt(List<String> opt) {
        this.opt = opt;
    }

    public Date getDateCr() {
        return dateCr;
    }

    public void setDateCr(Date dateCr) {
        this.dateCr = dateCr;
    }
    
        public String getDateFormat() {
        return new SimpleDateFormat("dd-MMM-yyyy").format(this.dateCr);
    }
    @Override
    public String toString() {
        return "Question{" + "id=" + id + ", content=" + content + ", answer=" + answer + ", dateCr=" + dateCr + '}';
    }
    
}
