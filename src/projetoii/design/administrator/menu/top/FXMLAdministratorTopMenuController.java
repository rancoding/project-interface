package projetoii.design.administrator.menu.top;

import bll.ShopBLL;
import bll.WarehouseBLL;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import projetoii.design.administrator.shop.menu.top.FXMLShopTopMenuController;
import projetoii.design.administrator.warehouse.menu.top.FXMLWarehouseTopMenuController;
import services.ShopService;
import services.WarehouseService;

public class FXMLAdministratorTopMenuController implements Initializable {

    @FXML private ComboBox workLocationComboBox;
    @FXML private BorderPane adminTopMenu;
    
    private static byte workLocationId;

    public static byte getWorkLocationId() {
        return workLocationId;
    }

    public static void setWorkLocationId(byte workLocationId) {
        FXMLAdministratorTopMenuController.workLocationId = workLocationId;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        List<WarehouseBLL> warehouseList = WarehouseService.getConvertedWarehouseList();
        List<ShopBLL> shopList = ShopService.getConvertedShopList();
        
        /* Adds both lists to the combobox */
        if(!(warehouseList.isEmpty()))
        {
            workLocationComboBox.getItems().addAll(warehouseList);
        }
        if(!(shopList.isEmpty()))
        {
            workLocationComboBox.getItems().addAll(shopList);
        }
        
        //setComboBoxConverter();
        switchCenter();
    }    
    
    /* * Switches center when initializing * */
    private void switchCenter()
    {
        workLocationComboBox.getSelectionModel().select(0);
            
        if(!(workLocationComboBox.getSelectionModel().isEmpty()))
        {
            if(workLocationComboBox.getSelectionModel().getSelectedItem() instanceof WarehouseBLL)
            {
                setWorkLocationId( ((WarehouseBLL) workLocationComboBox.getSelectionModel().getSelectedItem()).getIdarmazem() );
                switchCenter(FXMLWarehouseTopMenuController.class, "FXMLWarehouseTopMenu.fxml");
            }
            else if(workLocationComboBox.getSelectionModel().getSelectedItem() instanceof ShopBLL)
            {
                setWorkLocationId( ((ShopBLL) workLocationComboBox.getSelectionModel().getSelectedItem()).getIdloja());
                switchCenter(FXMLShopTopMenuController.class, "FXMLShopTopMenu.fxml");
            }
        }
    }
    
    /* * Switches border pane center depending on selected combobox item * */
    @FXML private void switchCenter(ActionEvent event)
    {
        if(((ComboBox) event.getSource()).getSelectionModel().getSelectedItem() instanceof WarehouseBLL)
        {
            setWorkLocationId( ((WarehouseBLL) workLocationComboBox.getSelectionModel().getSelectedItem()).getIdarmazem() );
            switchCenter(FXMLWarehouseTopMenuController.class, "FXMLWarehouseTopMenu.fxml");
        }
        else if(((ComboBox) event.getSource()).getSelectionModel().getSelectedItem() instanceof ShopBLL)
        {
            setWorkLocationId( ((ShopBLL) workLocationComboBox.getSelectionModel().getSelectedItem()).getIdloja());
            switchCenter(FXMLShopTopMenuController.class, "FXMLShopTopMenu.fxml");
        }
    }
    
    /* * Sets the border pane center * */
    private void switchCenter(Class controller, String file)
    {
        try
        {
            Pane newPane = FXMLLoader.load(controller.getResource(file));
            adminTopMenu.setCenter(newPane);
        }
        catch(IOException e)
        {
        }
    }
}
