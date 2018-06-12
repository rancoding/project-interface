/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.size.edit;

import dao.Tamanho;
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
import org.hibernate.Session;
import org.hibernate.Transaction;
import projetoii.design.administrator.warehouse.data.size.list.FXMLListSizeController;

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
    private ObservableList<Tamanho> sizeList;
    private Tamanho size;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        editSizeNameButton.setDisable(true);
    }    
    
    /* * To be called when needing to initialize values from the list size controller * */
    public void initializeOnControllerCall(FXMLListSizeController listSizeController, ObservableList<Tamanho> sizeList, Tamanho size)
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
    
    private void setSizeList(ObservableList<Tamanho> sizeList)
    {
        this.sizeList = sizeList;
    }
    
    private void setSize(Tamanho size)
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
    
    /* * Checks if the size name typed in the text field already exists * */
    private boolean checkIfNameExists(String name, String nonCharacters)
    {
        for(Tamanho s : sizeList)
        {
            if(s.getIdtamanho() != size.getIdtamanho())
            {
                String sizeName = StringUtils.stripAccents(s.getDescricao().replaceAll(nonCharacters, "").toLowerCase());
                
                if(name.equals(sizeName))
                {
                    return true;
                }
            }
        }
        
        return false;
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
                boolean exists = checkIfNameExists(editedSizeName, nonCharacters);

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
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Transaction tx = session.beginTransaction();
        
        Tamanho sizeUpdate = (Tamanho)session.get(Tamanho.class, size.getIdtamanho());
        sizeUpdate.setDescricao(size.getDescricao());
        
        session.update(sizeUpdate);
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
