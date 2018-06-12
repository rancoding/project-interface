/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.brand.edit;

import dao.Marca;
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
import projetoii.design.administrator.warehouse.data.brand.list.FXMLListBrandController;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLEditBrandController implements Initializable {

    /* New category name, edit button and error label button */
    @FXML private TextField brandName;
    @FXML private Button editBrandNameButton;
    @FXML private Label errorLabel;
    
    /* Controller to be able to refresh the table on edit button click, and brand list to be able to edit and search for existent brands */
    private FXMLListBrandController listBrandController;
    private ObservableList<Marca> brandList;
    private Marca brand;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        editBrandNameButton.setDisable(true);
    }    
    
    /* * To be called when needing to initialize values from the list brand controller * */
    public void initializeOnControllerCall(FXMLListBrandController listBrandController, ObservableList<Marca> brandList, Marca brand)
    {
        /* Sets all variables accordingly to received parameters */
        setListBrandController(listBrandController);
        setBrandList(brandList);
        setBrand(brand);
        setField();
    }
    
    private void setListBrandController(FXMLListBrandController listBrandController)
    {
        this.listBrandController = listBrandController;
    }
    
    private void setBrandList(ObservableList<Marca> brandList)
    {
        this.brandList = brandList;
    }
    
    private void setBrand(Marca brand)
    {
        this.brand = brand;
    }
    
    private void setField()
    {
        this.brandName.setText(brand.getNome());
    }
    
    /* * Sets the new brand name, updates in the database, refreshes the list controller table and closes current window * */
    @FXML
    void onEditButtonClick(ActionEvent event) throws IOException
    {
        brand.setNome(WordUtils.capitalizeFully(brandName.getText()));
        
        updateBrand();
        
        this.listBrandController.brandTable.refresh();
        closeStage(event);
    }
    
    /* * Checks if the brand name typed in the text field already exists * */
    private boolean checkIfNameExists(String name, String nonCharacters)
    {
        for(Marca b : brandList)
        {
            if(b.getIdmarca() != brand.getIdmarca())
            {
                String brandName = StringUtils.stripAccents(b.getNome().replaceAll(nonCharacters, "").toLowerCase());
                
                if(name.equals(brandName))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /* * If brand name exists, disables edit button and shows an error in a label * */
    private void disableEditButtonAndShowError(String message)
    {
        editBrandNameButton.setDisable(true);
        errorLabel.setText(message);
    }
    
    /* * Checks if the typed name exists, disabling or enabling the edit button accordingly, and showing label error * */
    @FXML
    void checkNewNameToSetButtonDisable()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String editedBrandName = StringUtils.stripAccents(brandName.getText().replaceAll(nonCharacters, "").toLowerCase());
        String searchBrandName = StringUtils.stripAccents(brand.getNome().replaceAll(nonCharacters, "").toLowerCase());
        
        if(brandName.getText().isEmpty())
        {
            editBrandNameButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(!(editedBrandName.equals(searchBrandName)))
            {
                boolean exists = checkIfNameExists(editedBrandName, nonCharacters);

                if(exists)
                {
                    disableEditButtonAndShowError("Marca já existe");
                }
                else
                {
                    if(!(editBrandNameButton.getText().isEmpty()))
                    {
                        errorLabel.setText("");
                    }

                    editBrandNameButton.setDisable(false);
                }
            }
            else
            {
                if(!(editBrandNameButton.getText().isEmpty()))
                {
                    errorLabel.setText("");
                }

                editBrandNameButton.setDisable(true);
            }
        }
    }
    
    /* * Updates entity on database * */
    private void updateBrand()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Transaction tx = session.beginTransaction();
        
        Marca brandUpdate = (Marca)session.get(Marca.class, brand.getIdmarca());
        brandUpdate.setNome(brand.getNome());
        
        session.update(brandUpdate);
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
