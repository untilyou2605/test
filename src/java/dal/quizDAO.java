package dal;

import entity.Question;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
/**
 *
 * @author anhnb
 */
public class quizDAO {

    public boolean insertQuiz(Question q) throws Exception {
        DBContext db = new DBContext();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO Question "
                    + "(Content,answer,date,opt1,opt2,opt3,opt4)\n"
                    + "VALUES (?,?,GETDATE(),?,?,?,?);";
            ps = conn.prepareStatement(sql);
            ps.setString(1, q.getContent());
            ps.setString(2, q.getAnswer());
            ps.setString(3, q.getOpt().get(0));
            ps.setString(4, q.getOpt().get(1));
            ps.setString(5, q.getOpt().get(2));
            ps.setString(6, q.getOpt().get(3));


            ps.executeUpdate();
            conn.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            db.closeConnection(rs, ps, conn);
        }
        return true;
    }
    public boolean insertHistory(int userID,float point) throws Exception{
        DBContext db = new DBContext();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into history(userID,point,date) values (?,?,GETDATE());";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setFloat(2,point);

            ps.executeUpdate();
            conn.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            db.closeConnection(rs, ps, conn);
        }
        return true;
    }
    
    public int getNumOfQuestion() throws Exception {
        DBContext db = new DBContext();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getConnection();
            String sql = "SELECT COUNT(*) as num\n"
                    + "  FROM Question";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("num");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            db.closeConnection(rs, ps, conn);
        }
        return 0;
    }
    public ArrayList<Question> getQuestion(int num) throws Exception{
        DBContext db = new DBContext();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Question q;
        ArrayList<Question> questions = new ArrayList<>();
         try {
            conn = db.getConnection();
            String sql = "SELECT TOP (?) [ID]\n" +
                            "      ,[Content]\n" +
                            "      ,[answer]\n" +
                            "      ,[date]\n" +
                            "      ,[opt1]\n" +
                            "      ,[opt2]\n" +
                            "      ,[opt3]\n" +
                            "      ,[opt4]\n" +
                            "  FROM [dbo].[Question] order by NEWID()";
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, num);
            rs=ps.executeQuery();
            while (rs.next()) {
                q = new Question();
                q.setID(rs.getInt("ID"));
                q.setContent(rs.getNString("Content"));
                q.setAnswer(rs.getNString("answer"));
                q.setDateCr(rs.getDate("date"));
                q.getOpt().add(rs.getString("opt1"));
                q.getOpt().add(rs.getString("opt2"));
                q.getOpt().add(rs.getString("opt3"));
                q.getOpt().add(rs.getString("opt4"));
                questions.add(q);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            db.closeConnection(rs, ps, conn);
        }
        return questions;
    }
    
     public ArrayList<Question> getAllQuiz(int pageindex, int pagesize) throws Exception{
        DBContext db = new DBContext();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Question q;
        ArrayList<Question> questions = new ArrayList<>();
         try {
            conn = db.getConnection();
            String sql = "select * from \n"
                    + " (select *,ROW_NUMBER() OVER(Order By ID ASC) "
                    + "as row_num from dbo.Question) a\n"
                    + " WHERE row_num >= (? - 1)*? +1 AND row_num<= (? * ?) ";
            ps = conn.prepareStatement(sql);
            
            ps.setInt(1, pageindex);
            ps.setInt(2, pagesize);
            ps.setInt(3, pageindex);
            ps.setInt(4, pagesize);
            rs=ps.executeQuery();
            while (rs.next()) {
                q = new Question();
                q.setID(rs.getInt("ID"));
                q.setContent(rs.getNString("Content"));
                q.setAnswer(rs.getNString("answer"));
                q.setDateCr(rs.getDate("date"));
                q.getOpt().add(rs.getString("opt1"));
                q.getOpt().add(rs.getString("opt2"));
                q.getOpt().add(rs.getString("opt3"));
                q.getOpt().add(rs.getString("opt4"));
                questions.add(q);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            db.closeConnection(rs, ps, conn);
        }
        return questions;
    }
     
        public int quizCount() throws Exception {
        int count = 0;
         DBContext db = new DBContext();
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection connection = db.getConnection();

        try {
            String sql = "select COUNT (*) as rownum from dbo.Question ";
            statement = connection.prepareStatement(sql);
           
            rs = statement.executeQuery();
            while (rs.next()) {
                count = rs.getInt("rownum");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            db.closeConnection(rs, statement, connection);
        }
        return count;
    }
         public boolean deleteQuiz(int id) throws Exception {
        int count = 0;
         DBContext db = new DBContext();
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection connection = db.getConnection();

        try {
            String sql = "delete from Question where id= ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            int isDelete= statement.executeUpdate();
            if(isDelete !=0) return true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            db.closeConnection(rs, statement, connection);
        }
        return false;
    }
}
