package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;

import javax.swing.*;

public class StudentGUI extends JFrame{
    private JPanel panel1;
    private JPanel wrapper;

    public StudentGUI() {
        Helper.setLoyout();
        add(wrapper);
        setSize(500, 500);
        int x = Helper.screenCenterPosition("x", getSize());
        int y = Helper.screenCenterPosition("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
    }
}
