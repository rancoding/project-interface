package projetoii.design.administrator.warehouse.data.color.add;

import bll.ColorBLL;
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
import projetoii.design.administrator.warehouse.data.color.list.FXMLListColorController;
import projetoii.design.administrator.warehouse.data.product.add.FXMLAddProductController;
import services.ColorService;

public class FXMLAddColorController implements Initializable {

    @FXML private TextField colorName;
    @FXML private Button addColorButton;
    @FXML private Label errorLabel;
    
    private FXMLAddProductController addProductController;
    private FXMLListColorController listColorController;
    private ObservableList<ColorBLL> colorList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        addColorButton.setDisable(true);
    }    
   
    /* * Initializes all variables when getting called from another controller * */
    public void initializeOnAddProductControllerCall(FXMLAddProductController addProductController, ObservableList<ColorBLL> colorList)
    {
        setListAddProductController(addProductController);
        setObservableList(colorList);
    }
    
    /* * Sets list controller * */
    private void setListAddProductController(FXMLAddProductController addProductController)
    {
        this.addProductController = addProductController;
    }
    
    
    /* * Initializes all variables when getting called from another controller * */
    public void initializeOnControllerCall(FXMLListColorController listColorController, ObservableList<ColorBLL> colorList)
    {
        setListController(listColorController);
        setObservableList(colorList);
    }
    
    /* * Sets list controller * */
    private void setListController(FXMLListColorController listolorController)
    {
        this.listColorController = listolorController;
    }
    
    /* * Sets observable list from a given observable list * */
    private void setObservableList(ObservableList<ColorBLL> colorList)
    {
        this.colorList = colorList;
    }
    
    /* * Adds a new color and updates the database * */
    @FXML void onAddClick(ActionEvent event)
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        
        ColorBLL newColor = new ColorBLL();
        //newColor.setIdcor((byte) (colorList.size() + 1));
        newColor.setNome(StringUtils.capitalize(colorName.getText()).replaceAll(nonCharacters, ""));
        
        colorList.add(newColor);
        insertColor(newColor);
        
        if(this.listColorController != null)
        {
            this.listColorController.setSearchedTableValues(colorList);
            this.listColorController.colorTable.refresh();
        }
        else if(this.addProductController != null)
        {
            this.addProductController.colorComboBox.getSelectionModel().select(colorList.size()-1);
        }
        
        closeStage(event);
    }
    
    /* * Enables or disables the button * */
    @FXML void setAddButtonUsability()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String newColorName = StringUtils.stripAccents(colorName.getText().replaceAll(nonCharacters, "").toLowerCase());
        
        boolean exists = ColorService.checkForExistentColor(colorList, newColorName, nonCharacters);
       
        if(colorName.getText().isEmpty())
        {
            addColorButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(exists)
            {
                errorLabel.setText("Cor já existente");
                addColorButton.setDisable(true);
            }
            else
            {
                errorLabel.setText("");
                addColorButton.setDisable(false);
            }
        }
    }
    
    /* * Inserts entity into the database * */
    private void insertColor(ColorBLL color)
    {
        HibernateGenericLibrary.saveObject(color);
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
