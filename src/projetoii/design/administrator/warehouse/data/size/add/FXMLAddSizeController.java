package projetoii.design.administrator.warehouse.data.size.add;

import bll.SizeBLL;
import hibernate.HibernateGenericLibrary;
import hibernate.HibernateUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import projetoii.design.administrator.warehouse.data.product.add.FXMLAddProductController;
import projetoii.design.administrator.warehouse.data.size.list.FXMLListSizeController;
import services.SizeService;

public class FXMLAddSizeController implements Initializable {

    @FXML private TextField sizeName;
    @FXML private Button addSizeButton;
    @FXML private Label errorLabel;
    
    private FXMLAddProductController addProductController;
    private FXMLListSizeController listSizeController;
    private ObservableList<SizeBLL> sizeList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        addSizeButton.setDisable(true);
    }    
    
    public void setList(ObservableList brandsList){
    
    }
    
     /* * Initializes all variables when getting called from another controller * */
    public void initializeOnAddProductControllerCall(FXMLAddProductController addProductController, ObservableList<SizeBLL> sizeList)
    {
        setListAddProductController(addProductController);
        setObservableList(sizeList);
    }
    
    /* * Sets list controller * */
    private void setListAddProductController(FXMLAddProductController addProductController)
    {
        this.addProductController = addProductController;
    }
    
    /* * Initializes all variables when getting called from another controller * */
    public void initializeOnControllerCall(FXMLListSizeController listSizeController, ObservableList<SizeBLL> sizeList)
    {
        setListController(listSizeController);
        setObservableList(sizeList);
    }
    
    /* * Sets list controller * */
    private void setListController(FXMLListSizeController listSizeController)
    {
        this.listSizeController = listSizeController;
    }
    
    /* * Sets observable list from a given observable list * */
    private void setObservableList(ObservableList<SizeBLL> sizeList)
    {
        this.sizeList = sizeList;
    }
    
    /* * Adds a new size and updates the database * */
    @FXML void onAddClick(ActionEvent event)
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        
        SizeBLL newSize = new SizeBLL();
        //newSize.setIdtamanho((byte) (sizeList.size() + 1));
        newSize.setDescricao(sizeName.getText().toUpperCase().replaceAll(nonCharacters, ""));
        
        sizeList.add(newSize);
        insertSize(newSize);
        
        if(this.listSizeController != null)
        {
            this.listSizeController.setSearchedTableValues(sizeList);
            this.listSizeController.sizeTable.refresh();
        }
        else if(this.addProductController != null)
        {
            this.addProductController.sizeComboBox.getSelectionModel().select(sizeList.size()-1);
        }
        closeStage(event);
    }
    
    /* * Enables or disables the button * */
    @FXML void setAddButtonUsability()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String newSizeName = StringUtils.stripAccents(sizeName.getText().replaceAll(nonCharacters, "").toLowerCase());
        
        boolean exists = SizeService.checkForExistentSize(sizeList, newSizeName, nonCharacters);
       
        if(sizeName.getText().isEmpty())
        {
            addSizeButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(exists)
            {
                errorLabel.setText("Tamanho já existente");
                addSizeButton.setDisable(true);
            }
            else
            {
                errorLabel.setText("");
                addSizeButton.setDisable(false);
            }
        }
    }
    
    /* * Inserts entity into the database * */
    private void insertSize(SizeBLL size)
    {
        HibernateGenericLibrary.saveObject(size);
    }
    
    /* * Closes the stage on cancel button click * */
    @FXML void onCancelClick(ActionEvent event)
    {
        closeStage(event);
    }
    
    /* * Closes current window * */
    private void closeStage(ActionEvent event)
    {
        Node node = (Node)event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
