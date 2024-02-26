package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Helper.Item;
import com.patikadev.Model.Course;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Patika;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scrl_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JTextField fld_user_name;
    private JTextField fld_user_uname;
    private JTextField fld_user_password;
    private JComboBox cmb_user_type;
    private JButton btn_user_add;
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_search_user_name;
    private JTextField fld_search_user_uname;
    private JComboBox cmb_search_user_type;
    private JButton btn_user_search;
    private JPanel pnl_patika_list;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_patika_add;
    private JPanel pnl_user_top;
    private JPanel pnl_course_list;
    private JScrollPane scrl_course_list;
    private JTable tbl_course_list;
    private JPanel pnl_course_add;
    private JTextField fld_course_name;
    private JTextField fld_course_lang;
    private JComboBox cmb_course_patika;
    private JComboBox cmb_course_user;
    private JButton btn_course_add;

    private DefaultTableModel mdl_patika_list;

    private Object[] row_patika_list;

    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;
    private final Operator operator;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;

    private JPopupMenu patikaMenu;
    public OperatorGUI(Operator operator){
        this.operator = operator;
        Helper.setLoyout();
        add(wrapper);
        setSize(500,500);
        int x = Helper.screenCenterPosition("x",getSize());
        int y = Helper.screenCenterPosition("y",getSize());
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldin : "+operator.getUser_name());

        //ModelUserList
        mdl_user_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                return false;
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_user_list = {"ID","Ad Soyad","Kullanıcı Adı","Şifre","Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);
        row_user_list = new Object[col_user_list.length];

        loadUserModel();
        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {//seçilen değeri alma işlemi
            try {
                String selected_user_id = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString();
                fld_user_id.setText(selected_user_id);
            }
            catch (Exception exception){
                System.out.println(exception.getMessage());
            }

        });
        tbl_user_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE){
                int user_id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString());
                String user_name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),1).toString();
                String user_uname = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),2).toString();
                String user_password = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),3).toString();
                String user_type = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),4).toString();

                if (User.update(user_id,user_name,user_uname,user_password,user_type)){
                    Helper.showMessage("done");

                }
                loadUserModel();
                loadEducatorCombo();
                loadCourseModel();
            }
        });

        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Güncelle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
        int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(),0).toString());
        UpdatePatikaGUI updatePatikaGUI = new UpdatePatikaGUI(Patika.getFetch(select_id));
        updatePatikaGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loadPatikaModel();
                loadPatikaCombo();
                loadCourseModel();
            }
        });
        });

        deleteMenu.addActionListener(e -> {
            if (Helper.confirm("sure")){
                int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(),0).toString());
                if (Patika.delete(select_id)){
                    Helper.showMessage("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseModel();
                }else {
                    Helper.showMessage("error");
                }
            }
        });

        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID","Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(75);

        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selected_row,selected_row);
            }
        });

        mdl_course_list = new DefaultTableModel();
        Object[] col_course_list = {"ID","Ders Adı","Programlama Dili","Patika","Eğitmen"};
        mdl_course_list.setColumnIdentifiers(col_course_list);
        row_course_list = new Object[col_course_list.length];
        loadCourseModel();
        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);
        loadPatikaCombo();
        loadEducatorCombo();




        btn_user_add.addActionListener(e -> {
        if (Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_user_password)){
           Helper.showMessage("fill");
        }
        else {
            String name = fld_user_name.getText();
            String u_name = fld_user_uname.getText();
            String pass = fld_user_password.getText();
            String type = cmb_user_type.getSelectedItem().toString();
            if(User.add(name,u_name,pass,type)){
                Helper.showMessage("done");
                loadUserModel();
                loadEducatorCombo();
                fld_user_name.setText(null);
                fld_user_uname.setText(null);
                fld_user_password.setText(null);
            }
        }
        });

        btn_user_delete.addActionListener(e -> {
        if (Helper.isFieldEmpty(fld_user_id)){
            Helper.showMessage("fill");
        }
        else{
            if (Helper.confirm("sure")){
                int user_id = Integer.parseInt(fld_user_id.getText());
                if (User.delete(user_id)){
                    Helper.showMessage("done");
                    loadUserModel();
                    loadEducatorCombo();
                    loadCourseModel();
                    fld_user_id.setText(null);
                }
                else {
                    Helper.showMessage("error");
                }
            }
        }
        });
        btn_user_search.addActionListener(e -> {
            String user_name = fld_search_user_name.getText();
            String user_uname = fld_search_user_uname.getText();
            String user_type = cmb_search_user_type.getSelectedItem().toString();
            String query = User.searchQuery(user_name,user_uname,user_type);
            loadUserModel(User.searchUserList(query));


        });
        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI loginGUI = new LoginGUI();
        });

        btn_patika_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_patika_name)){
                Helper.showMessage("fill");
            }
            else{
                if (Patika.add(fld_patika_name.getText())){
                    Helper.showMessage("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    fld_patika_name.setText(null);
                }
                else {
                    Helper.showMessage("error");
                }
            }
        });

        btn_course_add.addActionListener(e -> {
        Item patikaItem = (Item) cmb_course_patika.getSelectedItem();
        Item userItem = (Item) cmb_course_user.getSelectedItem();
        if (Helper.isFieldEmpty(fld_course_name) || Helper.isFieldEmpty(fld_course_lang)){
            Helper.showMessage("fill");
        }else {
            if (Course.add(userItem.getKey(), patikaItem.getKey(),fld_course_name.getText(),fld_course_lang.getText())){
                Helper.showMessage("done");
                loadCourseModel();
                fld_course_name.setText(null);
                fld_course_lang.setText(null);
            }
            else {
                Helper.showMessage("error");
            }
        }
        });
    }

    private void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        int i=0;
        for (Course obj : Course.getList()){
        i = 0;
        row_course_list[i++] = obj.getCourse_id();
        row_course_list[i++] = obj.getCourse_name();
        row_course_list[i++] = obj.getCourse_language();
        row_course_list[i++] = obj.getPatika().getPatika_name();
        row_course_list[i++] = obj.getEducator().getUser_name();
        mdl_course_list.addRow(row_course_list);

        }
    }

    private void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);//içindekiler temizleniyor
        int i;
        for (Patika obj : Patika.getList()){
        i=0;
        row_patika_list[i++] = obj.getPatika_id();
        row_patika_list[i++] = obj.getPatika_name();
        mdl_patika_list.addRow(row_patika_list);

        }
    }

    public void loadUserModel(){

        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj: User.getList()){
            int i = 0;
            row_user_list[i++] = obj.getUser_id();
            row_user_list[i++] = obj.getUser_name();
            row_user_list[i++] = obj.getUser_uname();
            row_user_list[i++] = obj.getUser_password();
            row_user_list[i++] = obj.getUser_type();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadUserModel(ArrayList<User> list){

        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj: list){
            int i = 0;
            row_user_list[i++] = obj.getUser_id();
            row_user_list[i++] = obj.getUser_name();
            row_user_list[i++] = obj.getUser_uname();
            row_user_list[i++] = obj.getUser_password();
            row_user_list[i++] = obj.getUser_type();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadPatikaCombo(){
        cmb_course_patika.removeAllItems();
        for (Patika obj : Patika.getList()){
            cmb_course_patika.addItem(new Item(obj.getPatika_id(),obj.getPatika_name()));
        }
    }

    public void loadEducatorCombo(){
        cmb_course_user.removeAllItems();
        for (User obj : User.getList()){
           if (obj.getUser_type().equals("educator")){
               cmb_course_user.addItem(new Item(obj.getUser_id(),obj.getUser_name()));
           }
        }
    }

    public static void main(String[] args){
        Helper.setLoyout();
        Operator operator =  new Operator();
        operator.setUser_id(1);
        operator.setUser_name("cf Dolma");
        operator.setUser_uname("ezgi");
        operator.setUser_password("1234");
        OperatorGUI operatorGUI = new OperatorGUI(operator);
    }
}
