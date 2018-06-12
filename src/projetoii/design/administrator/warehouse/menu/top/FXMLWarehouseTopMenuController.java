/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.menu.top;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import projetoii.design.administrator.warehouse.box.list.FXMLListBoxController;
import projetoii.design.administrator.warehouse.employee.list.FXMLListEmployeeController;
import projetoii.design.administrator.warehouse.menu.left.FXMLWarehouseLeftMenuController;
import projetoii.design.administrator.warehouse.reposition.current.list.FXMLListCurrentRepositionController;

public class FXMLWarehouseTopMenuController implements Initializable {

    @FXML private ToggleButton warehouseButton;
    @FXML private ToggleButton employeeButton;
    @FXML private ToggleButton repositionButton;
    @FXML private ToggleButton boxButton;
    @FXML private BorderPane warehouseTopMenu;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        switchCenter();
    }    
    
    /* * Switches center on initialize * */
    private void switchCenter()
    {
        disableButtonSelection(true, false, false, false);
        this.switchCenter(FXMLWarehouseLeftMenuController.class, "FXMLWarehouseLeftMenu.fxml");
    }
    
    /* * Switches border pane center depending on selected combobox item * */
    @FXML private void switchCenter(ActionEvent event)
    {
        switch(((ToggleButton) event.getSource()).getId())
        {  
            case "warehouseButton":
            {
                disableButtonSelection(true, false, false, false);
                switchCenter(FXMLWarehouseLeftMenuController.class, "FXMLWarehouseLeftMenu.fxml");
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
                switchCenter(FXMLListCurrentRepositionController.class, "FXMLListCurrentReposition.fxml");
                break;
            }
            
            case "boxButton":
            {
                disableButtonSelection(false, false, false, true);
                switchCenter(FXMLListBoxController.class, "FXMLListBox.fxml");
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
            warehouseTopMenu.setCenter(newPane);
        }
        catch(Exception e)
        {
        }
    }
    
    /* * Disables toggle buttons depending on button clicked * */
    private void disableButtonSelection(boolean warehouse, boolean employee, boolean reposition, boolean box)
    {
        warehouseButton.setSelected(warehouse);
        employeeButton.setSelected(employee);
        repositionButton.setSelected(reposition);
        boxButton.setSelected(box);
    }
}
