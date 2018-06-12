/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.category.edit;

import dao.Tipoproduto;
import hibernate.HibernateUtil;
import java.io.IOException;
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
import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import projetoii.design.administrator.warehouse.data.category.list.FXMLListCategoryController;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLEditCategoryController implements Initializable {

    /* New category name, edit button and error label button */
    @FXML private TextField categoryName;
    @FXML private Button editCategoryNameButton;
    @FXML private Label errorLabel;
    
    /* Controller to be able to refresh the table on edit button click, and category list to be able to edit and search for existent categories */
    private FXMLListCategoryController listCategoryController;
    private ObservableList<Tipoproduto> productTypeList;
    private Tipoproduto productType;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        editCategoryNameButton.setDisable(true);
    }    
    
    /* * To be called when needing to initialize values from the list category controller * */
    public void initializeOnControllerCall(FXMLListCategoryController listCategoryController, ObservableList<Tipoproduto> productTypeList, Tipoproduto productType)
    {
        /* Sets all variables accordingly to received parameters */
        setListCategoryController(listCategoryController);
        setProductTypeList(productTypeList);
        setProductType(productType);
        setField();
    }
    
    private void setListCategoryController(FXMLListCategoryController listCategoryController)
    {
        this.listCategoryController = listCategoryController;
    }
    
    private void setProductTypeList(ObservableList<Tipoproduto> productTypeList)
    {
        this.productTypeList = productTypeList;
    }
    
    private void setProductType(Tipoproduto productType)
    {
        this.productType = productType;
    }
    
    private void setField()
    {
        this.categoryName.setText(productType.getNome());
    }
    
    /* * Sets the new product name, updates in the database, refreshes the list controller table and closes current window * */
    @FXML
    void onEditButtonClick(ActionEvent event) throws IOException
    {
        productType.setNome(WordUtils.capitalizeFully(categoryName.getText()));
        
        updateCategory();
        
        this.listCategoryController.categoryTable.refresh();
        closeStage(event);
    }
    
    /* * Checks if the category name typed in the text field already exists * */
    private boolean checkIfNameExists(String name, String nonCharacters)
    {
        for(Tipoproduto type : productTypeList)
        {
            if(type.getIdtipoproduto() != productType.getIdtipoproduto())
            {
                String typeName = StringUtils.stripAccents(type.getNome().replaceAll(nonCharacters, "").toLowerCase());
                
                if(name.equals(typeName))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /* * If category name exists, disables edit button and shows an error in a label * */
    private void disableEditButtonAndShowError(String message)
    {
        editCategoryNameButton.setDisable(true);
        errorLabel.setText(message);
    }
    
    /* * Checks if the typed name exists, disabling or enabling the edit button accordingly, and showing label error * */
    @FXML
    void checkNewNameToSetButtonDisable()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String editedTypeName = StringUtils.stripAccents(categoryName.getText().replaceAll(nonCharacters, "").toLowerCase());
        String typeName = StringUtils.stripAccents(productType.getNome().replaceAll(nonCharacters, "").toLowerCase());
        
        if(categoryName.getText().isEmpty())
        {
            editCategoryNameButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(!(editedTypeName.equals(typeName)))
            {
                boolean exists = checkIfNameExists(editedTypeName, nonCharacters);

                if(exists)
                {
                    disableEditButtonAndShowError("Tipo de producto já existe");
                }
                else
                {
                    if(!(editCategoryNameButton.getText().isEmpty()))
                    {
                        errorLabel.setText("");
                    }

                    editCategoryNameButton.setDisable(false);
                }
            }
            else
            {
                if(!(editCategoryNameButton.getText().isEmpty()))
                {
                    errorLabel.setText("");
                }

                editCategoryNameButton.setDisable(true);
            }
        }
    }
    
    /* * Updates entity on database * */
    private void updateCategory()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Transaction tx = session.beginTransaction();
        
        Tipoproduto typeUpdate = (Tipoproduto)session.get(Tipoproduto.class, productType.getIdtipoproduto());
        typeUpdate.setNome(productType.getNome());
        
        session.update(typeUpdate);
        tx.commit();
        
        session.close();
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
