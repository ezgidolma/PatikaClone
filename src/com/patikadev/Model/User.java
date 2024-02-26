package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.*;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class User {
    private int user_id;
    private String user_name;
    private String user_uname;
    private String user_password;
    private String user_type;

    public User(){

    }
    public User(int user_id, String user_name, String user_uname, String user_password, String user_type) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_uname = user_uname;
        this.user_password = user_password;
        this.user_type = user_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_uname() {
        return user_uname;
    }

    public void setUser_uname(String user_uname) {
        this.user_uname = user_uname;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public static ArrayList<User> getList(){
        ArrayList<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user";
        User obj;
        try {
            Statement statement = DBConnector.getIstance().createStatement();
            ResultSet set = statement.executeQuery(query);
            while (set.next()){
                obj = new User();
                obj.setUser_id(set.getInt("user_id"));
                obj.setUser_name(set.getString("user_name"));
                obj.setUser_uname(set.getString("user_uname"));
                obj.setUser_password(set.getString("user_password"));
                obj.setUser_type(set.getString("user_type"));
                userList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public static boolean add(String user_name, String user_uname, String user_password,String user_type){
        String query = "INSERT INTO user (user_name,user_uname,user_password,user_type) VALUES (?,?,?,?)";
        User findUser = User.getFetch(user_uname);
        if (findUser != null) {
            Helper.showMessage("Bu kullanıcı adı daha öncesinde kullanılmış. Farklı bir kullanıcı adı giriniz.");
            return false;
        }
        try {
            PreparedStatement p_statement = DBConnector.getIstance().prepareStatement(query);
            p_statement.setString(1,user_name);
            p_statement.setString(2,user_uname);
            p_statement.setString(3,user_password);
            p_statement.setString(4,user_type);
            int response = p_statement.executeUpdate();
           if (response == -1){
               Helper.showMessage("error");
           }
            return response != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
        return true;
    }

    public static User getFetch(String user_uname){
        User obj = null;
        String query = "SELECT * FROM user WHERE user_uname = ?";
        try {
            PreparedStatement preparedStatement = DBConnector.getIstance().prepareStatement(query);
            preparedStatement.setString(1,user_uname);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                obj = new User();
                obj.setUser_id(resultSet.getInt("user_id"));
                obj.setUser_name(resultSet.getString("user_name"));
                obj.setUser_uname(resultSet.getString("user_uname"));
                obj.setUser_password(resultSet.getString("user_password"));
                obj.setUser_type(resultSet.getString("user_type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static User getFetch(int user_id){
        User obj = null;
        String query = "SELECT * FROM user WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = DBConnector.getIstance().prepareStatement(query);
            preparedStatement.setInt(1,user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                obj = new User();
                obj.setUser_id(resultSet.getInt("user_id"));
                obj.setUser_name(resultSet.getString("user_name"));
                obj.setUser_uname(resultSet.getString("user_uname"));
                obj.setUser_password(resultSet.getString("user_password"));
                obj.setUser_type(resultSet.getString("user_type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static boolean delete(int id){
        String query = "DELETE FROM user WHERE user_id = ?";
        ArrayList<Course> courseList = Course.getListByUser(id);
        for (Course c : courseList){
            Course.delete(c.getCourse_id());
        }
        try {
            PreparedStatement preparedStatement = DBConnector.getIstance().prepareStatement(query);
            preparedStatement.setInt(1,id);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
        return true;
    }

    public static boolean update(int user_id,String user_name,String user_uname,String user_password,String user_type){
        String query = "UPDATE user SET user_name=?, user_uname=?, user_password=?, user_type=? WHERE user_id=?";

        User findUser = User.getFetch(user_uname);
        if (findUser != null && findUser.getUser_id() != user_id) {
            Helper.showMessage("Bu kullanıcı adı daha öncesinde kullanılmış. Farklı bir kullanıcı adı giriniz.");
            return false;
        }

        try {
            PreparedStatement preparedStatement = DBConnector.getIstance().prepareStatement(query);
            preparedStatement.setString(1,user_name);
            preparedStatement.setString(2,user_uname);
            preparedStatement.setString(3,user_password);
            preparedStatement.setString(4,user_type);
            preparedStatement.setInt(5,user_id);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
        return true;
    }

    public static ArrayList<User> searchUserList (String query){
        ArrayList<User> userList = new ArrayList<>();
        User obj;
        try {
            Statement statement = DBConnector.getIstance().createStatement();
            ResultSet set = statement.executeQuery(query);
            while (set.next()){
                obj = new User();
                obj.setUser_id(set.getInt("user_id"));
                obj.setUser_name(set.getString("user_name"));
                obj.setUser_uname(set.getString("user_uname"));
                obj.setUser_password(set.getString("user_password"));
                obj.setUser_type(set.getString("user_type"));
                userList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public static String searchQuery(String user_name,String user_uname,String user_type){
        String query = "SELECT * FROM user WHERE user_name LIKE '%{{user_name}}%' AND user_uname LIKE '%{{user_uname}}%' AND user_type LIKE '%{{user_type}}%'";
        query = query.replace("{{user_name}}",user_name);
        query = query.replace("{{user_uname}}",user_uname);
        query = query.replace("{{user_type}}",user_type);
        System.out.println(query);
        return query;
    }

    public static User getFetch(String user_uname, String user_password ){
        User obj = null;
        String query = "SELECT * FROM user WHERE user_uname = ? AND user_password = ?";
        try {
            PreparedStatement preparedStatement = DBConnector.getIstance().prepareStatement(query);
            preparedStatement.setString(1,user_uname);
            preparedStatement.setString(2,user_password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                switch (resultSet.getString("user_type")){
                    case "operator":
                        obj = new Operator();
                        break;
                    default:
                        obj = new User();
                }
                obj = new User();
                obj.setUser_id(resultSet.getInt("user_id"));
                obj.setUser_name(resultSet.getString("user_name"));
                obj.setUser_uname(resultSet.getString("user_uname"));
                obj.setUser_password(resultSet.getString("user_password"));
                obj.setUser_type(resultSet.getString("user_type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
}
