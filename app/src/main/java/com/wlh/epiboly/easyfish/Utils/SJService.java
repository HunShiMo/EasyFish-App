package com.wlh.epiboly.easyfish.Utils;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.wlh.epiboly.easyfish.View.User_Info;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SJService {
    private Connection conn=null; //打开数据库对象
    private PreparedStatement ps=null;//操作整合sql语句的对象
    private ResultSet rs=null;//查询结果的集合
    //DBService 对象
    public static SJService sjService=null;

    /**
     * 构造方法 私有化
     * */

    private SJService(){
    }

    /**
     * 获取MySQL数据库单例类对象
     * */

    public static SJService getSJService(){
        if(sjService==null){
            sjService=new SJService();
        }
        return sjService;
    }

    /**
     * 查
     * */
    public List<User_Info> Login(String username){
        //结果存放集合
        List<User_Info> list=new ArrayList<User_Info>();
        //MySQL 语句
        String sql="select * from sj where user_name = ?";
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

/*
    *//**
     * 查
     * *//*
    public List<SJ> GetLateData(int id){
        //结果存放集合
        List<SJ> list=new ArrayList<SJ>();
        //MySQL 语句
        String sql="select * from sj where id > ?";
        //获取链接数据库对象
        conn= DBOpenHelper.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                ps.setInt(1, id);
                if(ps!=null){
                    rs= ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            SJ sj=new SJ();
                            sj.setId(rs.getInt("id"));
                            sj.setTable_key(rs.getString("table_key"));
                            sj.setTable_value(rs.getString("table_value"));
                            sj.setTime(rs.getString("time"));
                            sj.setPosition(rs.getString("position"));
                            list.add(sj);
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

    *//**
     * 查
     * *//*
    public List<SJ> GetAllData(){
        //结果存放集合
        List<SJ> list=new ArrayList<SJ>();
        //MySQL 语句
        String sql="select * from sj ";
        //获取链接数据库对象
        conn= DBOpenHelper.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                if(ps!=null){
                    rs= ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            SJ sj=new SJ();
                            sj.setId(rs.getInt("id"));
                            sj.setTable_key(rs.getString("table_key"));
                            sj.setTable_value(rs.getString("table_value"));
                            sj.setTime(rs.getString("time"));
                            sj.setPosition(rs.getString("position"));
                            list.add(sj);
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

    *//**
     * 修改数据库中某个对象的状态   改
     * *//*
    public int updateSJData(int id,SJ sj){
        int result=-1;
        //获取链接数据库对象
        conn= DBOpenHelper.getConn();
        //MySQL 语句
        String sql="update user_info set user_password=? where id=?";
        try {
            boolean closed=conn.isClosed();
            if(conn!=null&&(!closed)){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1,sj.getTime());//第一个参数state 一定要和上面SQL语句字段顺序一致
                ps.setInt(2,id);//第二个参数 phone 一定要和上面SQL语句字段顺序一致
                result=ps.executeUpdate();//返回1 执行成功
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }

    *//**
     * 批量向数据库插入数据   增
     * *//*
    public int insertSJData(SJ sj){
        int result=-1;
        if(sj!=null){
            //获取链接数据库对象
            conn= DBOpenHelper.getConn();
            if(conn!=null){
                //MySQL 语句
                String sql="INSERT INTO user_info (id,table_key,table_value,time,position) VALUES (?,?,?,?,?)";
                try {
                    boolean closed=conn.isClosed();
                    if((conn!=null)&&(!closed)){
                        ps= (PreparedStatement) conn.prepareStatement(sql);
                        int id= sj.getId();
                        String table_key= sj.getTable_key();
                        String table_value= sj.getTable_value();
                        String time= sj.getTime();
                        String position= sj.getPosition();

                        ps.setInt(1,id);//第一个参数 name 规则同上
                        ps.setString(2,table_key);//第二个参数 phone 规则同上
                        ps.setString(3,table_value);//第二个参数 phone 规则同上
                        ps.setString(4,time);//第二个参数 phone 规则同上
                        ps.setString(5,position);//第二个参数 phone 规则同上
                        result=ps.executeUpdate();//返回1 执行成功
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }*/
    /**
     * 删除数据  删
     * */
    public int delUserData(int id){
        int result=-1;
        //获取链接数据库对象
        conn= DBOpenHelper.getConn();
        //MySQL 语句
        String sql="delete from sj where id= ? ";
        try {
            boolean closed=conn.isClosed();
            if((conn!=null)&&(!closed)){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                ps.setInt(1, id);
                result=ps.executeUpdate();//返回1 执行成功
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }
}
