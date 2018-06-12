/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.shop.menu.top;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import projetoii.design.administrator.shop.employee.list.FXMLListEmployeeController;
import projetoii.design.administrator.shop.menu.left.FXMLShopLeftMenuController;
import projetoii.design.administrator.shop.reposition.history.list.FXMLListRepositionHistoryController;
import projetoii.design.administrator.shop.sale.list.FXMLListSaleController;

public class FXMLShopTopMenuController implements Initializable {

    @FXML private ToggleButton shopButton;
    @FXML private ToggleButton employeeButton;
    @FXML private ToggleButton repositionButton;
    @FXML private ToggleButton saleButton;
    @FXML private BorderPane shopTopMenu;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        switchCenter();
    }    
     /* * Switches center on initialize * */
    private void switchCenter()
    {
        disableButtonSelection(true, false, false, false);
        this.switchCenter(FXMLShopLeftMenuController.class, "FXMLShopLeftMenu.fxml");
    }
    
    /* * Switches border pane center depending on selected combobox item * */
    @FXML private void switchCenter(ActionEvent event)
    {
        switch(((ToggleButton) event.getSource()).getId())
        {  
            case "shopButton":
            {
                System.out.println("Hello");
                disableButtonSelection(true, false, false, false);
                System.out.println("Hello2");
                switchCenter(FXMLShopLeftMenuController.class, "FXMLShopLeftMenu.fxml");
                System.out.println("Hello3");
                break;
            }
            
            case "employeeButton":
            {
                disableButtonSelection(false, true, false, false);
                switchCenter(FXMLListEmployeeController.class, "FXMLListEmployee.fxml");
                break;
            }
            
            case "repositionButton":
            {
                disableButtonSelection(false, false, true, false);
                switchCenter(FXMLListRepositionHistoryController.class, "FXMLListRepositionHistory.fxml");
                break;
            }
            
            case "saleButton":
            {
                disableButtonSelection(false, false, false, true);
                switchCenter(FXMLListSaleController.class, "FXMLListSale.fxml");
                break;
            }
        }
    }
    
    /* * Sets the border pane center * */
    private void switchCenter(Class controller, String file)
    {
        try
        {
            System.out.println("Hello4");
            Pane newPane = FXMLLoader.load(controller.getResource(file));
            System.out.println("Hello5");
            shopTopMenu.setCenter(newPane);
            System.out.println("Hello6");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /* * Disables toggle buttons depending on button clicked * */
    private void disableButtonSelection(boolean shop, boolean employee, boolean reposition, boolean sale)
    {
        shopButton.setSelected(shop);
        employeeButton.setSelected(employee);
        repositionButton.setSelected(reposition);
        saleButton.setSelected(sale);
    }
}
