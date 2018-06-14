/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.user.work.login;

import bll.ShopBLL;
import dao.Funcionario;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import services.ShopService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLLoginController implements Initializable {

    @FXML private ComboBox workLocationComboBox;
    private List<Funcionario> employeeList;
    private List<ShopBLL> shopList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        employeeList = new ArrayList<>();
        shopList = new ArrayList<>();
        
        shopList = ShopService.getConvertedShopList();
        workLocationComboBox.getItems().addAll(shopList);
        
//        Criteria criteria = session.createCriteria(Funcionario.class);
//        criteria.add(Restrictions.like("localtrabalho.idlocaltrabalho", ((Loja) workLocationComboBox.getSelectionModel().getSelectedItem()).getIdloja()));
//        employeeList = criteria.list();
        
        for(Funcionario f : employeeList)
        {
            System.out.println(f.getNome());
        }
        
    }
    
    @FXML private void onAccountButtonClick(ActionEvent event)
    {
        //openPasswordWindow( ((Button) event.getSource()).set
    }
    
    /* * Opens the password window for the given user * */
    private void openPasswordWindow(Funcionario employee)
    {
        
    }
    
}
