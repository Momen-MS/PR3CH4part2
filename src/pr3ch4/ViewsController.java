/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr3ch4;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author MOMEN
 */
public class ViewsController implements Initializable {

    @FXML
    private Button save;
    @FXML
    private Button show;
    @FXML
    private Button update;
    @FXML
    private Button delete;
    @FXML
    private TableColumn<student, Integer> tcid;
    @FXML
    private TableColumn<student, String> tcname;
    @FXML
    private TableColumn<student, String> tcmajor;
    @FXML
    private TableColumn<student, Double> tcgrade;
    @FXML
    private Button clear;
    @FXML
    private Button savereg;
    @FXML
    private Button withd;
    @FXML
    private Button updatreg;
    @FXML
    private Button showinfo;
    @FXML
    private Label nof;
    @FXML
    private TableView<student> studentTb;
    Statement stat;
    @FXML
    private TextField idstf;
    @FXML
    private TextField nstf;
    @FXML
    private TextField mstf;
    @FXML
    private TextField gstf;
    Connection connection;
    @FXML
    private TextField couidreg;
    @FXML
    private TextField reidtf;
    @FXML
    private TextField semregtf;
    @FXML
    private TableView<register> regtb;
    @FXML
    private TableColumn<register, Integer> idrg;
    @FXML
    private TableColumn<register, Integer> coureg;
    @FXML
    private TableColumn<register, String> semregtb;
    @FXML
    private TextArea quertf;
    @FXML
    private Button run;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection
                    = DriverManager.
                            getConnection("jdbc:mysql://127.0.0.1:3306/students",
                                    "root", "");

            this.stat = connection.createStatement();

            tcid.setCellValueFactory(new PropertyValueFactory("id"));
            tcname.setCellValueFactory(new PropertyValueFactory("name"));
            tcmajor.setCellValueFactory(new PropertyValueFactory("major"));
            tcgrade.setCellValueFactory(new PropertyValueFactory("grade"));
            ResultSet rs = this.stat.executeQuery("Select * From student");
            studentTb.getItems().clear();
            while (rs.next()) {
                student st1 = new student();
                st1.setId(rs.getInt("id"));
                st1.setName(rs.getString("name"));
                st1.setMajor(rs.getString("major"));
                st1.setGrade(rs.getDouble("grade"));
                studentTb.getItems().add(st1);
            }
//                show regerstrions
            idrg.setCellValueFactory(new PropertyValueFactory("id"));
            coureg.setCellValueFactory(new PropertyValueFactory("courseId"));
            semregtb.setCellValueFactory(new PropertyValueFactory("semester"));
            ResultSet reg = this.stat.executeQuery("Select * From registration");
            regtb.getItems().clear();
            while (reg.next()) {
                register r1 = new register();
                r1.setId(reg.getInt("studentid"));
                r1.setCourseId(reg.getInt("courseid"));
                r1.setSemester(reg.getString("semester"));
                regtb.getItems().add(r1);
            }

            studentTb.getSelectionModel().selectedItemProperty().addListener(
                    event -> showSelectedstudent());
            regtb.getSelectionModel().selectedItemProperty().addListener(
                    event -> showSelectedreg());
        } catch (SQLException ex) {
            Logger.getLogger(ViewsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void saveBut(ActionEvent event) throws SQLException {
        Integer id = Integer.parseInt(idstf.getText());
        String name = nstf.getText();
        String major = mstf.getText();
        Double grade = Double.parseDouble(gstf.getText());
        if (checkIdName(id, name)) {
            String sql = "Insert Into student values(" + id + ",'" + name + "','"
                    + major + "'," + grade + ")";
            this.stat.executeUpdate(sql);
            ResultSet ss = this.stat.executeQuery("Select * From student");
            studentTb.getItems().clear();
            while (ss.next()) {
                student st1 = new student();
                st1.setId(ss.getInt("id"));
                st1.setName(ss.getString("name"));
                st1.setMajor(ss.getString("major"));
                st1.setGrade(ss.getDouble("grade"));
                studentTb.getItems().add(st1);
            }
        idstf.setText("");
        nstf.setText("");
        mstf.setText("");
        gstf.setText("");
        } else {
            nof.setText("System:duplication in id or name");
        }
    }

    private boolean checkIdName(int id, String name) throws SQLException {
        boolean result = true;
        ResultSet rs = this.stat.executeQuery("Select * From student");
        while (rs.next()) {
            student st2 = new student();
            st2.setId(rs.getInt("id"));
            st2.setName(rs.getString("name"));
            if (st2.getId() == id || st2.getName().equalsIgnoreCase(name)) {
                result = false;
            }
        }
        return result;
    }

    @FXML
    private void showBut(ActionEvent event) throws SQLException {
        student st1 = studentTb.getSelectionModel().getSelectedItem();
        if (st1 != null) {
            String id = String.valueOf(st1.getId());
        
        ResultSet reg = this.stat.executeQuery("Select * From registration WHERE studentid ="+id);
            regtb.getItems().clear();
            while (reg.next()) {
                register r1 = new register();
                r1.setId(reg.getInt("studentid"));
                r1.setCourseId(reg.getInt("courseid"));
                r1.setSemester(reg.getString("semester"));
                regtb.getItems().add(r1);
            }
        }

    }

    @FXML
    private void updateBut(ActionEvent event) throws Exception {
        Integer id = Integer.parseInt(idstf.getText());
        String name = nstf.getText();
        String major = mstf.getText();
        Double grade = Double.parseDouble(gstf.getText());
        String sql = "Update student Set name='" + name + "', major='"
                + major + "', grade=" + grade + " Where id=" + id;
        this.stat.executeUpdate(sql);
        ResultSet rs = this.stat.executeQuery("Select * From student");
        studentTb.getItems().clear();
        while (rs.next()) {
            student st1 = new student();
            st1.setId(rs.getInt("id"));
            st1.setName(rs.getString("name"));
            st1.setMajor(rs.getString("major"));
            st1.setGrade(rs.getDouble("grade"));
            studentTb.getItems().add(st1);
        }
        idstf.setText("");
        nstf.setText("");
        mstf.setText("");
        gstf.setText("");

    }

    @FXML
    private void deleteBut(ActionEvent event) throws SQLException {
        student st1 = studentTb.getSelectionModel().getSelectedItem();
        if (st1 != null) {
            String id = String.valueOf(st1.getId());
            String sql = "DELETE FROM student WHERE id =" + id;
            this.stat.executeUpdate(sql);
            ResultSet rs = this.stat.executeQuery("Select * From student");
            studentTb.getItems().clear();
            while (rs.next()) {
                st1.setId(rs.getInt("id"));
                st1.setName(rs.getString("name"));
                st1.setMajor(rs.getString("major"));
                st1.setGrade(rs.getDouble("grade"));
                studentTb.getItems().add(st1);
            }
            idstf.setText("");
            nstf.setText("");
            mstf.setText("");
            gstf.setText("");
        }
    }

    private void showSelectedstudent() {
        student st1 = studentTb.getSelectionModel().getSelectedItem();
        if (st1 != null) {
            idstf.setText(String.valueOf(st1.getId()));
            nstf.setText(st1.getName());
            mstf.setText(st1.getMajor());
            gstf.setText(String.valueOf(st1.getGrade()));
        }

    }

    private void showSelectedreg() {
        register r1 = regtb.getSelectionModel().getSelectedItem();
        if (r1 != null) {
            reidtf.setText(String.valueOf(r1.getId()));
            couidreg.setText(String.valueOf(r1.getCourseId()));
            semregtf.setText(r1.getSemester());
        }

    }

    @FXML
    private void clearbut(ActionEvent event) {
        reidtf.setText("");
        couidreg.setText("");
        semregtf.setText("");
    }

    @FXML
    private void saveregbut(ActionEvent event) throws SQLException {
        Integer Stid = Integer.parseInt(reidtf.getText());
        Integer CoId = Integer.parseInt(couidreg.getText());
        String Semester = semregtf.getText();
//        if (checkIdCou(Stid, CoId)) {
            String sql = "Insert Into registration values(" + Stid + ",'" + CoId + "','"
                    + Semester + ")";
            this.stat.executeUpdate(sql);
            ResultSet reg = this.stat.executeQuery("Select * From registration");
            regtb.getItems().clear();
            while (reg.next()) {
                register r1 = new register();
                r1.setId(reg.getInt("studentid"));
                r1.setCourseId(reg.getInt("courseid"));
                r1.setSemester(reg.getString("semester"));
                regtb.getItems().add(r1);
            }
//        } else {
//            nof.setText("System:student id or course id not found");
//        }

    }

    @FXML
    private void withdbut(ActionEvent event) throws SQLException {
        register r7 = regtb.getSelectionModel().getSelectedItem();
        if (r7 != null) {
            String id = String.valueOf(r7.getId());
            String sql = "DELETE FROM registration WHERE studentid =" + id;
            this.stat.executeUpdate(sql);
            ResultSet reg = this.stat.executeQuery("Select * From registration");
            regtb.getItems().clear();
            while (reg.next()) {
                register r1 = new register();
                r1.setId(reg.getInt("studentid"));
                r1.setCourseId(reg.getInt("courseid"));
                r1.setSemester(reg.getString("semester"));
                regtb.getItems().add(r1);
            }
            reidtf.setText("");
            couidreg.setText("");
            semregtf.setText("");
        }
    }

    @FXML
    private void updatregbut(ActionEvent event) throws SQLException {
        Integer id = Integer.parseInt(reidtf.getText());
        Integer CoId = Integer.parseInt(couidreg.getText());
        String Semester = semregtf.getText();
        String sql = "Update registration Set courseid='" + CoId + "', semester='"
                + Semester +" Where studentid=" + id;
        this.stat.executeUpdate(sql);
        ResultSet reg = this.stat.executeQuery("Select * From registration");
            regtb.getItems().clear();
            while (reg.next()) {
                register r1 = new register();
                r1.setId(reg.getInt("studentid"));
                r1.setCourseId(reg.getInt("courseid"));
                r1.setSemester(reg.getString("semester"));
                regtb.getItems().add(r1);
            }
            reidtf.setText("");
            couidreg.setText("");
            semregtf.setText("");
        }
        
    

    @FXML
    private void showinfobut(ActionEvent event) throws SQLException {
        register r1 = regtb.getSelectionModel().getSelectedItem();
        String idnow = String.valueOf(r1.getId());
        ResultSet aa = this.stat.executeQuery("Select * From student Where id =" + idnow);
        studentTb.getItems().clear();
        while (aa.next()) {
            idstf.setText(aa.getString("id"));
            nstf.setText(aa.getString("name"));
            mstf.setText(aa.getString("major"));
            gstf.setText(aa.getString("grade"));

        }

    }

    private boolean checkIdCou(int id, int cour) throws SQLException {
        boolean result = false;
//              ResultSet rs = this.stat.executeQuery("Select * From student");
//                 while (rs.next()) {
//            student st2 = new student();
//            st2.setId(rs.getInt("id"));
//            st2.setName(rs.getString("name"));
//            if (st2.getId() == id) {
                ResultSet cc = this.stat.executeQuery("Select id From course");
                while (cc.next()) {
                    register r1 = new register();
                    r1.setCourseId(cc.getInt("id"));
                    if (r1.getCourseId() == cour) {
                        result = true;
                    }
                }
            
        
        return result;
    }

//    @FXML
//    private void saveBut(ActionEvent event) {
//    }

    @FXML
    private void clearbutttt(ActionEvent event) throws SQLException {
        quertf.clear();
                    ResultSet ss = this.stat.executeQuery("Select * From student");
            studentTb.getItems().clear();
            while (ss.next()) {
                student st1 = new student();
                st1.setId(ss.getInt("id"));
                st1.setName(ss.getString("name"));
                st1.setMajor(ss.getString("major"));
                st1.setGrade(ss.getDouble("grade"));
                studentTb.getItems().add(st1);
            }
    }

    @FXML
    private void runbb(ActionEvent event) throws SQLException {
                    String sql = quertf.getText();
                    String xx = sql.substring(0, 2);
                    if(xx.equalsIgnoreCase("UP")){
                     this.stat.executeUpdate(sql);
                ResultSet rs = this.stat.executeQuery("Select * From student");
            studentTb.getItems().clear();
            while (rs.next()) {
                student st1 = new student();
                st1.setId(rs.getInt("id"));
                st1.setName(rs.getString("name"));
                st1.setMajor(rs.getString("major"));
                st1.setGrade(rs.getDouble("grade"));
                studentTb.getItems().add(st1);
            }
                    }else{
            ResultSet ss = this.stat.executeQuery(sql);
            studentTb.getItems().clear();
            while (ss.next()) {
                student st1 = new student();
                st1.setId(ss.getInt("id"));
                st1.setName(ss.getString("name"));
                st1.setMajor(ss.getString("major"));
                st1.setGrade(ss.getDouble("grade"));
                studentTb.getItems().add(st1);
            }
    }
    }
}
