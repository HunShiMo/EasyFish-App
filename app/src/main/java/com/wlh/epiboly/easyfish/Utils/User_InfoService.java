package com.wlh.epiboly.easyfish.Utils;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.wlh.epiboly.easyfish.View.User_Info;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

import android.util.Log;

public class User_InfoService {
    private Connection conn=null; //打开数据库对象
    private PreparedStatement ps=null;//操作整合sql语句的对象
    private ResultSet rs=null;//查询结果的集合
    //DBService 对象
    public static User_InfoService user_infoService=null;

    /**
     * 构造方法 私有化
     * */

    private User_InfoService(){
    }

    /**
     * 获取MySQL数据库单例类对象
     * */

    public static User_InfoService getUser_InfoService(){
        if(user_infoService==null){
            user_infoService=new User_InfoService();
        }
        return user_infoService;
    }

    /**
     * 查
     * */
    public List<User_Info> Login(String username){
        //结果存放集合
        List<User_Info> list=new ArrayList<User_Info>();
        //MySQL 语句
        String sql="select * from user_info where user_name = ?";
        //获取链接数据库对象
        conn= DBOpenHelper.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, username);
                rs= ps.executeQuery();
                if(ps!=null){
                    if(rs!=null){
                        while(rs.next()){
                            User_Info u=new User_Info();
                            u.setUser_email(rs.getString("user_email"));
                            u.setUser_name(rs.getString("user_name"));
                            u.setUser_password(rs.getString("user_password"));
                            list.add(u);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn,ps,rs);//关闭相关操作
        return list;
    }

    /**
     * 查
     * */
    public List<User_Info> GetUserData(){
        //结果存放集合
        List<User_Info> list=new ArrayList<User_Info>();
        //MySQL 语句
        String sql="select user_password from user_info where ";
        //获取链接数据库对象

        conn= DBOpenHelper.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                if(ps!=null){
                    rs= ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            User_Info u=new User_Info();
                            u.setUser_email(rs.getString("user_email"));
                            u.setUser_name(rs.getString("user_name"));
                            u.setUser_password(rs.getString("user_password"));
                            list.add(u);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn,ps,rs);//关闭相关操作
        return list;
    }

    /**
     * 修改数据库中某个对象的状态   改
     * */
    public int updateUserData(String username, String password){
        int result=-1;
        if(isEmpty(username)){
            //获取链接数据库对象
            conn= DBOpenHelper.getConn();
            //MySQL 语句
            String sql="update user_info set user_password=? where user_name=?";
            try {
                boolean closed=conn.isClosed();
                if(conn!=null&&(!closed)){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    ps.setString(1,password);//第一个参数state 一定要和上面SQL语句字段顺序一致
                    ps.setString(2,username);//第二个参数 phone 一定要和上面SQL语句字段顺序一致
                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }

    /**
     * 批量向数据库插入数据   增
     * */
    public int insertUserData(User_Info user_info){
        int result=-1;
        if(user_info!=null){
            //获取链接数据库对象
            conn= DBOpenHelper.getConn();
            System.out.println("======================================="+conn);
            if(conn!=null){
                //MySQL 语句
                String sql="INSERT INTO user_info (user_email,user_name,user_password) VALUES (?,?,?)";
                try {
                    boolean closed=conn.isClosed();
                    if((conn!=null)&&(!closed)){
                        ps= (PreparedStatement) conn.prepareStatement(sql);
                        String useremail= user_info.getUser_email();
                        String username= user_info.getUser_name();
                        String password= user_info.getUser_password();
                        ps.setString(1,useremail);//第一个参数 name 规则同上
                        ps.setString(2,username);//第二个参数 phone 规则同上
                        ps.setString(3,password);//第二个参数 phone 规则同上
                        result=ps.executeUpdate();//返回1 执行成功
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }
    /**
     * 删除数据  删
     * */
    public int delUserData(String username){
        int result=-1;
        if(!isEmpty(username)){
            //获取链接数据库对象
            conn= DBOpenHelper.getConn();
            //MySQL 语句
            String sql="delete from user_info where user_name=?";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    ps.setString(1, username);
                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }
}
