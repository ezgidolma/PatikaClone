package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Operator;
import com.patikadev.Model.User;

import javax.swing.*;

public class LoginGUI extends JFrame{
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    private JTextField fld_user_uname;
    private JPasswordField fld_user_password;
    private JButton btn_login;

    public LoginGUI(){
        add(wrapper);
        setSize(500,500);
        setLocation(Helper.screenCenterPosition("x",getSize()),Helper.screenCenterPosition("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);

        setVisible(true);
        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_uname) || Helper.isFieldEmpty(fld_user_password)){
                Helper.showMessage("fill");
            }else {
                User user = User.getFetch(fld_user_uname.getText(),fld_user_password.getText());
                if (user == null){
                    Helper.showMessage("Kullanıcı Bulunamadı !");
                }else {
                   switch (user.getUser_type()){
                       case "operator":
                           OperatorGUI operatorGUI = new OperatorGUI((Operator) user);
                           break;
                       case "educator":
                           EducatorGUI educatorGUI = new EducatorGUI();
                           break;
                       case "student":
                           StudentGUI studentGUI = new StudentGUI();
                           break;
                   }
                   dispose();
                }
            }
        });
    }

    public static void main(String[] args){
        Helper.setLoyout();
        LoginGUI loginGUI = new LoginGUI();


    }
}
