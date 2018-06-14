/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.size.edit;

import bll.SizeBLL;
import hibernate.HibernateGenericLibrary;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import projetoii.design.administrator.warehouse.data.size.list.FXMLListSizeController;
import services.SizeService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLEditSizeController implements Initializable {

    /* New category name, edit button and error label button */
    @FXML private TextField sizeName;
    @FXML private Button editSizeNameButton;
    @FXML private Label errorLabel;
    
    /* Controller to be able to refresh the table on edit button click, and size list to be able to edit and search for existent sizes */
    private FXMLListSizeController listSizeController;
    private ObservableList<SizeBLL> sizeList;
    private SizeBLL size;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        editSizeNameButton.setDisable(true);
    }    
    
    /* * To be called when needing to initialize values from the list size controller * */
    public void initializeOnControllerCall(FXMLListSizeController listSizeController, ObservableList<SizeBLL> sizeList, SizeBLL size)
    {
        /* Sets all variables accordingly to received parameters */
        setListSizeController(listSizeController);
        setSizeList(sizeList);
        setSize(size);
        setField();
    }
    
    private void setListSizeController(FXMLListSizeController listSizeController)
    {
        this.listSizeController = listSizeController;
    }
    
    private void setSizeList(ObservableList<SizeBLL> sizeList)
    {
        this.sizeList = sizeList;
    }
    
    private void setSize(SizeBLL size)
    {
        this.size = size;
    }
    
    private void setField()
    {
        this.sizeName.setText(size.getDescricao());
    }
    
    /* * Sets the new size name, updates in the database, refreshes the list controller table and closes current window * */
    @FXML
    void onEditButtonClick(ActionEvent event) throws IOException
    {
        size.setDescricao(sizeName.getText().toUpperCase());
        
        updateSize();
        
        this.listSizeController.sizeTable.refresh();
        closeStage(event);
    }
    
    /* * If size name exists, disables edit button and shows an error in a label * */
    private void disableEditButtonAndShowError(String message)
    {
        editSizeNameButton.setDisable(true);
        errorLabel.setText(message);
    }
    
    
    /* * Checks if the typed name exists, disabling or enabling the edit button accordingly, and showing label error * */
    @FXML
    void checkNewNameToSetButtonDisable()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String editedSizeName = StringUtils.stripAccents(sizeName.getText().replaceAll(nonCharacters, "").toLowerCase());
        String searchSizeName = StringUtils.stripAccents(size.getDescricao().replaceAll(nonCharacters, "").toLowerCase());
        
        if(sizeName.getText().isEmpty())
        {
            editSizeNameButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(!(editedSizeName.equals(searchSizeName)))
            {
                boolean exists = SizeService.checkIfNameExists(sizeList, size, editedSizeName, nonCharacters);

                if(exists)
                {
                    disableEditButtonAndShowError("Tamanho já existe");
                }
                else
                {
                    if(!(editSizeNameButton.getText().isEmpty()))
                    {
                        errorLabel.setText("");
                    }

                    editSizeNameButton.setDisable(false);
                }
            }
            else
            {
                if(!(editSizeNameButton.getText().isEmpty()))
                {
                    errorLabel.setText("");
                }

                editSizeNameButton.setDisable(true);
            }
        }
    }
    
    /* * Updates entity on database * */
    private void updateSize()
    {
        List<SizeBLL> sizes = HibernateGenericLibrary.executeHQLQuery("FROM Tamanho WHERE idtamanho = " + size.getIdtamanho());
        
        SizeBLL oldSize = sizes.get(0);
        oldSize.setDescricao(size.getDescricao());
        
        HibernateGenericLibrary.updateObject(oldSize);
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
