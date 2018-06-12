/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.shop.menu.left;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import projetoii.design.administrator.shop.data.product.list.FXMLListProductController;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLShopLeftMenuController implements Initializable {

    @FXML private ToggleButton productButton;
    @FXML private BorderPane shopLeftMenu;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        switchCenter();
    }    
    
    private void switchCenter()
    {
        disableButtonSelection(true);
        switchCenter(FXMLListProductController.class, "FXMLListProduct.fxml");
    }
    
    @FXML private void switchCenter(ActionEvent event)
    {
         switch(((ToggleButton) event.getSource()).getId())
        {  
            case "productButton":
            {
                disableButtonSelection(true);
                switchCenter(FXMLListProductController.class, "FXMLListProduct.fxml");
                break;
            }
         }
    }
    
    /* * Sets the border pane center * */
    private void switchCenter(Class controller, String file)
    {
        try
        {
            Pane newPane = FXMLLoader.load(controller.getResource(file));
            shopLeftMenu.setCenter(newPane);
        }
        catch(Exception e)
        {
        }
    }
    
    /* * Disables toggle buttons depending on button clicked * */
    private void disableButtonSelection(boolean product)
    {
        productButton.setSelected(product);
    }
    
}
