package projetoii.design.administrator.warehouse.data.color.add;

import dao.Cor;
import dao.Tamanho;
import dao.Tipoproduto;
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
import org.hibernate.Session;
import org.hibernate.Transaction;
import projetoii.design.administrator.warehouse.data.color.list.FXMLListColorController;
import projetoii.design.administrator.warehouse.data.product.add.FXMLAddProductController;

public class FXMLAddColorController implements Initializable {

    @FXML private TextField colorName;
    @FXML private Button addColorButton;
    @FXML private Label errorLabel;
    
    private FXMLAddProductController addProductController;
    private FXMLListColorController listColorController;
    private ObservableList<Cor> colorList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        addColorButton.setDisable(true);
    }    
   
    /* * Initializes all variables when getting called from another controller * */
    public void initializeOnAddProductControllerCall(FXMLAddProductController addProductController, ObservableList<Cor> colorList)
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
    public void initializeOnControllerCall(FXMLListColorController listColorController, ObservableList<Cor> colorList)
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
    private void setObservableList(ObservableList<Cor> colorList)
    {
        this.colorList = colorList;
    }
    
    /* * Adds a new color and updates the database * */
    @FXML void onAddClick(ActionEvent event)
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        
        Cor newColor = new Cor();
        newColor.setIdcor((byte) (colorList.size() + 1));
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
    
    /* * Searches for a color with the same name as the new one in the color list * */
    private boolean checkForExistentColor(String name, String nonCharacters)
    {
        if(!(colorList.isEmpty()))
        {
            for(Cor color : colorList)
            {
                String searchColorName = StringUtils.stripAccents(color.getNome().replaceAll(nonCharacters, "").toLowerCase());

                if(name.equals(searchColorName))
                {
                    return true;
                }
            }
        }
            
        return false;
    }
    
    /* * Enables or disables the button * */
    @FXML void setAddButtonUsability()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String newColorName = StringUtils.stripAccents(colorName.getText().replaceAll(nonCharacters, "").toLowerCase());
        
        boolean exists = checkForExistentColor(newColorName, nonCharacters);
       
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
    private void insertColor(Cor color)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Transaction tx = session.beginTransaction();
        session.save(color);

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
