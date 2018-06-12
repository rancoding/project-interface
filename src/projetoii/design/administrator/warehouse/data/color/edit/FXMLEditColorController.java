package projetoii.design.administrator.warehouse.data.color.edit;

import dao.Cor;
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
import projetoii.design.administrator.warehouse.data.color.list.FXMLListColorController;

public class FXMLEditColorController implements Initializable {
    
    /* New category name, edit button and error label button */
    @FXML private TextField colorName;
    @FXML private Button editColorNameButton;
    @FXML private Label errorLabel;
    
    /* Controller to be able to refresh the table on edit button click, and color list to be able to edit and search for existent color */
    private FXMLListColorController listColorController;
    private ObservableList<Cor> colorList;
    private Cor color;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        editColorNameButton.setDisable(true);
    }    
    
    /* * To be called when needing to initialize values from the list color controller * */
    public void initializeOnControllerCall(FXMLListColorController listColorController, ObservableList<Cor> colorList, Cor color)
    {
        /* Sets all variables accordingly to received parameters */
        setListColorController(listColorController);
        setColorList(colorList);
        setColor(color);
        setField();
    }
    
    private void setListColorController(FXMLListColorController listColorController)
    {
        this.listColorController = listColorController;
    }
    
    private void setColorList(ObservableList<Cor> colorList)
    {
        this.colorList = colorList;
    }
    
    private void setColor(Cor color)
    {
        this.color = color;
    }
    
    private void setField()
    {
        this.colorName.setText(color.getNome());
    }
    
    /* * Sets the new color name, updates in the database, refreshes the list controller table and closes current window * */
    @FXML
    void onEditButtonClick(ActionEvent event) throws IOException
    {
        color.setNome(WordUtils.capitalizeFully(colorName.getText()));
        
        updateColor();
        
        this.listColorController.colorTable.refresh();
        closeStage(event);
    }
    
    /* * Checks if the color name typed in the text field already exists * */
    private boolean checkIfNameExists(String name, String nonCharacters)
    {
        for(Cor c : colorList)
        {
            if(c.getIdcor() != color.getIdcor())
            {
                String colorName = StringUtils.stripAccents(c.getNome().replaceAll(nonCharacters, "").toLowerCase());
                
                if(name.equals(colorName))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /* * If color name exists, disables edit button and shows an error in a label * */
    private void disableEditButtonAndShowError(String message)
    {
        editColorNameButton.setDisable(true);
        errorLabel.setText(message);
    }
    
    /* * Checks if the typed name exists, disabling or enabling the edit button accordingly, and showing label error * */
    @FXML
    void checkNewNameToSetButtonDisable()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String editedColorName = StringUtils.stripAccents(colorName.getText().replaceAll(nonCharacters, "").toLowerCase());
        String searchColorName = StringUtils.stripAccents(color.getNome().replaceAll(nonCharacters, "").toLowerCase());
        
        if(colorName.getText().isEmpty())
        {
            editColorNameButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(!(editedColorName.equals(searchColorName)))
            {
                boolean exists = checkIfNameExists(editedColorName, nonCharacters);

                if(exists)
                {
                    disableEditButtonAndShowError("Cor já existe");
                }
                else
                {
                    if(!(editColorNameButton.getText().isEmpty()))
                    {
                        errorLabel.setText("");
                    }

                    editColorNameButton.setDisable(false);
                }
            }
            else
            {
                if(!(editColorNameButton.getText().isEmpty()))
                {
                    errorLabel.setText("");
                }

                editColorNameButton.setDisable(true);
            }
        }
    }
    
    /* * Updates entity on database * */
    private void updateColor()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Transaction tx = session.beginTransaction();
        
        Cor colorUpdate = (Cor)session.get(Cor.class, color.getIdcor());
        colorUpdate.setNome(color.getNome());
        
        session.update(colorUpdate);
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
