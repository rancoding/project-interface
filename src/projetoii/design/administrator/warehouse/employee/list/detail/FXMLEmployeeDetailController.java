/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.employee.list.detail;

import dao.Funcionario;
import dao.Horario;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLEmployeeDetailController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField firstEntranceField;
    @FXML private TextField firstExitField;
    @FXML private TextField secondEntranceField;
    @FXML private TextField secondExitField;
    @FXML private DatePicker birthdayDate;
    @FXML private ComboBox genderBox;
    @FXML private ComboBox typeBox;
    @FXML private ComboBox activeBox;
    
    private Funcionario employee;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    /* * Initializes variables when called from other controller * */
    public void initializeOnControllerCall(Funcionario employee)
    {
        setEmployee(employee);
        setFields();
    }
    
    private void setEmployee(Funcionario employee)
    {
        this.employee = employee;
    }
    
    /* * Sets FXML fields to contain the correct employee information * */
    private void setFields()
    {
        nameField.setText(employee.getNome());
        // addressField.setText(employee.getMorada());
        
        LocalDate localDate = getLocalDateFromDate(employee.getDatanascimento());
        birthdayDate.setValue(localDate);
        birthdayDate.setStyle("-fx-opacity: 1;");
        
        String gender = getGenderFullName((employee.getSexo()));
        genderBox.setValue(gender);
        genderBox.setStyle("-fx-opacity: 1;");
        
        String typeText = getTypeFullName(employee.isTipo());
        typeBox.setValue(typeText);
        typeBox.setStyle("-fx-opacity: 1;");
        
        String activeText = getActiveFullText(employee.isActivo());
        activeBox.setValue(activeText);
        activeBox.setStyle("-fx-opacity: 1;");
        
        Horario horario = employee.getHorario();
        
        firstEntranceField.setText( getDateTime(horario.getHoraprimeiraentrada()) );
        firstExitField.setText( getDateTime(horario.getHoraprimeirasaida()) );
        secondEntranceField.setText( getDateTime(horario.getHorasegundaentrada()) );
        secondExitField.setText( getDateTime(horario.getHorasegundasaida()) );
    }
    
    /* * Converts the employee date to a local date * */
    private LocalDate getLocalDateFromDate(Date date)
    {
        LocalDate localDate = LocalDate.parse(date.toString());
        // LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }
    
    /* * Gets the gender full name (Male, Female, Undefined) depending on the given character * */
    private String getGenderFullName(char gender)
    {
        switch(gender)
        {
            case 'u':
            case 'U':
            {
                return "Uniforme";
            }
            
            case 'm':
            case 'M':
            {
                return "Masculino";
            }
            
            case 'f':
            case 'F':
            {
                return "Feminino";
            }
            
            default:
            {
                return "Indefinido";
            }
        }
    }
    
    /* * Returns if an employee is still working or not * */
    private String getActiveFullText(boolean active)
    {
        if(active)
        {
            return "Sim";
        }
        else
        {
            return "Não";
        }
    }
    
    /* * Returns if an employee is still working or not * */
    private String getTypeFullName(boolean type)
    {
        if(type)
        {
            return "Administrador";
        }
        else
        {
            return "Utilizador";
        }
    }
    
    /* * Returns the hour and minutes of a given date * */
    private String getDateTime(Date date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(date);
    }
    
    /* * Closes the stage on cancel button click * */
    @FXML void onCancelClick(ActionEvent event)
    {
        closeStage(event);
    }
    
    /* * Closes current window * */
    private void closeStage(ActionEvent event)
    {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }  
}
