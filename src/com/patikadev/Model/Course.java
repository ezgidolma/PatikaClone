package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Course {
    private int course_id;
    private int course_user_id;
    private int course_patika_id;
    private String course_name;
    private String course_language;

    private User educator;
    private Patika patika;

    public Course(int course_id, int course_user_id, int course_patika_id, String course_name, String course_language) {
        this.course_id = course_id;
        this.course_user_id = course_user_id;
        this.course_patika_id = course_patika_id;
        this.course_name = course_name;
        this.course_language = course_language;
        this.patika = Patika.getFetch(course_patika_id);
        this.educator = User.getFetch(course_user_id);
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getCourse_user_id() {
        return course_user_id;
    }

    public void setCourse_user_id(int course_user_id) {
        this.course_user_id = course_user_id;
    }

    public int getCourse_patika_id() {
        return course_patika_id;
    }

    public void setCourse_patika_id(int course_patika_id) {
        this.course_patika_id = course_patika_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_language() {
        return course_language;
    }

    public void setCourse_language(String course_language) {
        this.course_language = course_language;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        this.educator = educator;
    }

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }

    public static ArrayList<Course> getList(){
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;

        try {
            Statement statement = DBConnector.getIstance().createStatement();
            ResultSet resultSet = statement.executeQuery("SElECT * FROM course");
            while (resultSet.next()){
                int id = resultSet.getInt("course_id");
                int user_id = resultSet.getInt("course_user_id");
                int patika_id = resultSet.getInt("course_patika_id");
                String name = resultSet.getString("course_name");
                String language = resultSet.getString("course_language");
                obj = new Course(id,user_id,patika_id,name,language);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            return courseList;
    }

    public static boolean add(int course_user_id, int course_patika_id,String course_name,String course_language){
        String query = "INSERT INTO course (course_user_id, course_patika_id, course_name, course_language) VALUES (?,?,?,?)";

        try {
            PreparedStatement preparedStatement = DBConnector.getIstance().prepareStatement(query);
            preparedStatement.setInt(1,course_user_id);
            preparedStatement.setInt(2,course_patika_id);
            preparedStatement.setString(3,course_name);
            preparedStatement.setString(4,course_language);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
        return true;
    }

    public static ArrayList<Course> getListByUser(int user_id){
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;

        try {
            Statement statement = DBConnector.getIstance().createStatement();
            ResultSet resultSet = statement.executeQuery("SElECT * FROM course WHERE  course_user_id = "+user_id);
            while (resultSet.next()){
                int id = resultSet.getInt("course_id");
                int userId = resultSet.getInt("course_user_id");
                int patika_id = resultSet.getInt("course_patika_id");
                String name = resultSet.getString("course_name");
                String language = resultSet.getString("course_language");
                obj = new Course(id,userId,patika_id,name,language);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }

    public static boolean delete(int id){
        String query = "DELETE FROM course WHERE course_id = ?";
        try {
            PreparedStatement pr = DBConnector.getIstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

}
