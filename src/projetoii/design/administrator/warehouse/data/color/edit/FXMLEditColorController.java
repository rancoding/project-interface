package projetoii.design.administrator.warehouse.data.color.edit;

import bll.ColorBLL;
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
import org.apache.commons.lang3.text.WordUtils;
import projetoii.design.administrator.warehouse.data.color.list.FXMLListColorController;
import services.ColorService;

public class FXMLEditColorController implements Initializable {
    
    /* New category name, edit button and error label button */
    @FXML private TextField colorName;
    @FXML private Button editColorNameButton;
    @FXML private Label errorLabel;
    
    /* Controller to be able to refresh the table on edit button click, and color list to be able to edit and search for existent color */
    private FXMLListColorController listColorController;
    private ObservableList<ColorBLL> colorList;
    private ColorBLL color;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        editColorNameButton.setDisable(true);
    }    
    
    /* * To be called when needing to initialize values from the list color controller * */
    public void initializeOnControllerCall(FXMLListColorController listColorController, ObservableList<ColorBLL> colorList, ColorBLL color)
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
    
    private void setColorList(ObservableList<ColorBLL> colorList)
    {
        this.colorList = colorList;
    }
    
    private void setColor(ColorBLL color)
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
                boolean exists = ColorService.checkIfNameExists(colorList, color, editedColorName, nonCharacters);

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
        List<ColorBLL> colors = HibernateGenericLibrary.executeHQLQuery("FROM Cor WHERE idcor = " + color.getIdcor());
        
        ColorBLL oldColor = colors.get(0);
        oldColor.setNome(color.getNome());
        
        HibernateGenericLibrary.updateObject(oldColor);
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
