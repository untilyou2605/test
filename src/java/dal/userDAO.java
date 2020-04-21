/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anhnb
 */
public class userDAO{
    public boolean insertUser(User user) throws Exception{
        DBContext db = new DBContext();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into [user] (userName,passWord,type,email) values (?,?,?,?);";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.isType());
            ps.setString(4, user.getEmail());
            ps.executeUpdate();
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            db.closeConnection(rs,ps, conn);
        }
        return true; 
    }
    
    public User getUser(String username,String password) throws Exception{
        DBContext db = new DBContext();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            conn =db.getConnection();
            
            String sql = "SELECT  [ID]\n" +
                        "      ,[userName]\n" +
                        "      ,[passWord]\n" +
                        "      ,[type]\n" +
                        "      ,[email] "+
                        "  FROM [dbo].[user] where [username] like ? "
                    + "and [password] like ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
             rs = ps.executeQuery();
            while(rs.next()){
                user = new User();
                user.setId(rs.getInt("ID"));
                user.setUsername(rs.getNString("username"));
                user.setPassword(rs.getNString("password"));
                user.setType(rs.getBoolean("type"));
                user.setEmail(rs.getNString("email"));
                return user;
            }
         
            
        } catch (Exception ex) {
           throw ex;
        } finally {
           
            db.closeConnection(rs,ps, conn);
        }
        return null; 
    }
    
    public boolean getUserExist(String username) throws Exception{
        DBContext db = new DBContext();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn =db.getConnection();
            
            String sql = "SELECT  [ID]\n" +
                        "      ,[userName]\n" +
                        "      ,[passWord]\n" +
                        "      ,[type]\n" +
                        "      ,[email] "+
                        "  FROM [dbo].[user] where [username] like ? ";
                    
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
             rs = ps.executeQuery();
            while(rs.next()){
                return true;
            }
            
        } catch (Exception ex) {
            throw ex;
        } finally {
            db.closeConnection(rs,ps, conn);
        }
        return false; 
    }
}
