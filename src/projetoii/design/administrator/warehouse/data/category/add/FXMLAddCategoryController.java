package projetoii.design.administrator.warehouse.data.category.add;

import bll.CategoryBLL;
import dao.Tipoproduto;
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
import projetoii.design.administrator.warehouse.data.category.list.FXMLListCategoryController;
import projetoii.design.administrator.warehouse.data.product.add.FXMLAddProductController;
import services.CategoryService;

public class FXMLAddCategoryController implements Initializable {
    
    @FXML private TextField categoryName;
    @FXML private Button addCategoryButton;
    @FXML private Label errorLabel;
    
    private FXMLAddProductController addProductController;
    private FXMLListCategoryController listCategoryController;
    private ObservableList<CategoryBLL> productTypeList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        addCategoryButton.setDisable(true);
    }    
    /* * Initializes all variables when getting called from another controller * */
    public void initializeOnAddProductControllerCall(FXMLAddProductController addProductController, ObservableList<CategoryBLL> typeList)
    {
        setListAddProductController(addProductController);
        setObservableList(typeList);
    }
    
    /* * Sets list controller * */
    private void setListAddProductController(FXMLAddProductController addProductController)
    {
        this.addProductController = addProductController;
    }
    
    /* * Initializes all variables when getting called from another controller * */
    public void initializeOnControllerCall(FXMLListCategoryController listCategoryController, ObservableList<CategoryBLL> productTypeList)
    {
        setListController(listCategoryController);
        setObservableList(productTypeList);
    }
    
    /* * Sets list controller * */
    private void setListController(FXMLListCategoryController listCategoryController)
    {
        this.listCategoryController = listCategoryController;
    }
    
    /* * Sets observable list from a given observable list * */
    private void setObservableList(ObservableList<CategoryBLL> productTypeList)
    {
        this.productTypeList = productTypeList;
    }
    
    /* * Adds a new category and updates the database * */
    @FXML void onAddClick(ActionEvent event)
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        
        CategoryBLL newType = new CategoryBLL();
        //newType.setIdtipoproduto((byte) (productTypeList.size() + 1));
        newType.setNome(StringUtils.capitalize(categoryName.getText()).replaceAll(nonCharacters, ""));
        
        productTypeList.add(newType);
        insertCategory(newType);
        
        if(this.listCategoryController != null)
        {
            this.listCategoryController.setSearchedTableValues(productTypeList);
            this.listCategoryController.categoryTable.refresh();
        }
        else if(this.addProductController != null)
        {
            this.addProductController.typeComboBox.getSelectionModel().select(productTypeList.size()-1);
        }
        
        closeStage(event);
    }
    
    /* * Enables or disables the button * */
    @FXML void setAddButtonUsability()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String newTypeName = StringUtils.stripAccents(categoryName.getText().replaceAll(nonCharacters, "").toLowerCase());
        
        boolean exists = CategoryService.checkForExistentCategory(productTypeList, newTypeName, nonCharacters);
       
        if(categoryName.getText().isEmpty())
        {
            addCategoryButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(exists)
            {
                errorLabel.setText("Tipo de produto já existente");
                addCategoryButton.setDisable(true);
            }
            else
            {
                errorLabel.setText("");
                addCategoryButton.setDisable(false);
            }
        }
    }
    
    /* * Inserts entity into the database * */
    private void insertCategory(CategoryBLL type)
    {
        HibernateGenericLibrary.saveObject(type);
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
