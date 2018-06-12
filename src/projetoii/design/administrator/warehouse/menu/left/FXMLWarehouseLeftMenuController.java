/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.menu.left;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import projetoii.design.administrator.warehouse.data.brand.list.FXMLListBrandController;
import projetoii.design.administrator.warehouse.data.category.list.FXMLListCategoryController;
import projetoii.design.administrator.warehouse.data.color.list.FXMLListColorController;
import projetoii.design.administrator.warehouse.data.product.list.FXMLListProductController;
import projetoii.design.administrator.warehouse.data.size.list.FXMLListSizeController;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLWarehouseLeftMenuController implements Initializable {

    @FXML private ToggleButton productButton;
    @FXML private ToggleButton categoryButton;
    @FXML private ToggleButton brandButton;
    @FXML private ToggleButton sizeButton;
    @FXML private ToggleButton colorButton;
    @FXML private BorderPane warehouseLeftMenu;
    
    private static int selectedButton = 0;

    public static int getSelectedButton() {
        return selectedButton;
    }

    public static void setSelectedButton(int selectedButton) {
        FXMLWarehouseLeftMenuController.selectedButton = selectedButton;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        switchCenter();
    }    
    
    private void switchCenter()
    {
        switch(getSelectedButton())
        {
            case 0:
            {
                disableButtonSelection(true, false, false, false, false);
                switchCenter(FXMLListProductController.class, "FXMLListProduct.fxml");
                break;
            }
            
            case 1:
            {
                disableButtonSelection(false, true, false, false, false);
                switchCenter(FXMLListCategoryController.class, "FXMLListCategory.fxml");
                break;
            }
            
            case 2:
            {
                disableButtonSelection(false, false, true, false, false);
                switchCenter(FXMLListBrandController.class, "FXMLListBrand.fxml");
                break;
            }
            
            case 3:
            {
                disableButtonSelection(false, false, false, true, false);
                switchCenter(FXMLListSizeController.class, "FXMLListSize.fxml");
                break;
            }
            
            case 4:
            {
                disableButtonSelection(false, false, false, false, true);
                switchCenter(FXMLListColorController.class, "FXMLListColor.fxml");
                break;
            }
        }
    }
    
    /* * Switches border pane center depending on selected combobox item * */
    @FXML private void switchCenter(ActionEvent event)
    {
        switch(((ToggleButton) event.getSource()).getId())
        {  
            case "productButton":
            {
                setSelectedButton(0);
                disableButtonSelection(true, false, false, false, false);
                switchCenter(FXMLListProductController.class, "FXMLListProduct.fxml");
                break;
            }
            
            case "categoryButton":
            {
                setSelectedButton(1);
                disableButtonSelection(false, true, false, false, false);
                switchCenter(FXMLListCategoryController.class, "FXMLListCategory.fxml");
                break;
            }
            
            case "brandButton":
            {
                setSelectedButton(2);
                disableButtonSelection(false, false, true, false, false);
                switchCenter(FXMLListBrandController.class, "FXMLListBrand.fxml");
                break;
            }
            
            case "sizeButton":
            {
                setSelectedButton(3);
                disableButtonSelection(false, false, false, true, false);
                switchCenter(FXMLListSizeController.class, "FXMLListSize.fxml");
                break;
            }
            
            case "colorButton":
            {
                setSelectedButton(4);
                disableButtonSelection(false, false, false, false, true);
                switchCenter(FXMLListColorController.class, "FXMLListColor.fxml");
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
            warehouseLeftMenu.setCenter(newPane);
        }
        catch(Exception e)
        {
        }
    }
    
    /* * Disables toggle buttons depending on button clicked * */
    private void disableButtonSelection(boolean product, boolean category, boolean brand, boolean size, boolean color)
    {
        productButton.setSelected(product);
        categoryButton.setSelected(category);
        brandButton.setSelected(brand);
        sizeButton.setSelected(size);
        colorButton.setSelected(color);
    }
}
