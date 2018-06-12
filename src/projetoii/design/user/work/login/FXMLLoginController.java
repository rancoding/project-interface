/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.user.work.login;

import dao.Funcionario;
import dao.Loja;
import hibernate.HibernateUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLLoginController implements Initializable {

    @FXML private ComboBox workLocationComboBox;
    private List<Funcionario> employeeList;
    private List<Loja> shopList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        employeeList = new ArrayList<>();
        shopList = new ArrayList<>();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        shopList = session.createCriteria(Loja.class).list();
        workLocationComboBox.getItems().addAll(shopList);
        
        setComboBoxConverter();
//        Criteria criteria = session.createCriteria(Funcionario.class);
//        criteria.add(Restrictions.like("localtrabalho.idlocaltrabalho", ((Loja) workLocationComboBox.getSelectionModel().getSelectedItem()).getIdloja()));
//        employeeList = criteria.list();
        
        for(Funcionario f : employeeList)
        {
            System.out.println(f.getNome());
        }
        
        session.close();
    }
    
    @FXML private void onAccountButtonClick(ActionEvent event)
    {
        //openPasswordWindow( ((Button) event.getSource()).set
    }
    
    /* * Opens the password window for the given user * */
    private void openPasswordWindow(Funcionario employee)
    {
        
    }
    
    /* * Converts combobox to show working place names * */
    private void setComboBoxConverter()
    {
        workLocationComboBox.setConverter(new StringConverter() {
            @Override
            public String toString(Object object)
            {
                return ((Loja) object).getNome();
            }

            @Override
            public Object fromString(String string) {
                return "";
            }
        });
    }
    
}
