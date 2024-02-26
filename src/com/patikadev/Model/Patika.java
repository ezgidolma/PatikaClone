package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Patika {
    private  int patika_id;
    private String patika_name;

    public Patika(int patika_id, String patika_name) {
        this.patika_id = patika_id;
        this.patika_name = patika_name;
    }

    public int getPatika_id() {
        return patika_id;
    }

    public void setPatika_id(int patika_id) {
        this.patika_id = patika_id;
    }

    public String getPatika_name() {
        return patika_name;
    }

    public void setPatika_name(String patika_name) {
        this.patika_name = patika_name;
    }

    public static ArrayList<Patika> getList(){
        ArrayList<Patika> patikaList = new ArrayList<>();
        Patika obj;

        try {
            Statement statement = DBConnector.getIstance().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM patika");

            while (resultSet.next()){
                obj = new Patika(resultSet.getInt("patika_id"),resultSet.getString("patika_name"));
                patikaList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return patikaList;
    }

    public static boolean add(String name){
        String query = "INSERT INTO patika (patika_name) VALUES (?)";
        try {
            PreparedStatement preparedStatement = DBConnector.getIstance().prepareStatement(query);
            preparedStatement.setString(1,name);
            return  preparedStatement.executeUpdate()!=-1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
        return true;
    }

    public static boolean update(int id, String name){
        String query = "UPDATE patika SET patika_name = ? WHERE patika_id = ?";
        try {
            PreparedStatement preparedStatement = DBConnector.getIstance().prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,id);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static Patika getFetch(int patika_id){
        Patika obj = null;
        String query = "SELECT * FROM patika WHERE patika_id = ?";
        try {
            PreparedStatement statement = DBConnector.getIstance().prepareStatement(query);
            statement.setInt(1,patika_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                obj = new Patika(resultSet.getInt("patika_id"),resultSet.getString("patika_name"));
            }
        } catch (SQLException throwables) {
           throwables.printStackTrace();
        }
        return obj;
    }

    public static boolean delete(int patika_id){
        String query = "DELETE FROM patika WHERE patika_id = ?";
        ArrayList<Course> courseList = Course.getList();
        for (Course obj : courseList){
            if (obj.getPatika().getPatika_id() == patika_id){
                Course.delete(obj.getCourse_id());
            }
        }
        try {
            PreparedStatement preparedStatement = DBConnector.getIstance().prepareStatement(query);
            preparedStatement.setInt(1,patika_id);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
        return true;
    }
}
