package projetoii.design.administrator.warehouse.data.size.add;

import dao.Tamanho;
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
import projetoii.design.administrator.warehouse.data.product.add.FXMLAddProductController;
import projetoii.design.administrator.warehouse.data.size.list.FXMLListSizeController;

public class FXMLAddSizeController implements Initializable {

    @FXML private TextField sizeName;
    @FXML private Button addSizeButton;
    @FXML private Label errorLabel;
    
    private FXMLAddProductController addProductController;
    private FXMLListSizeController listSizeController;
    private ObservableList<Tamanho> sizeList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        addSizeButton.setDisable(true);
    }    
    
    public void setList(ObservableList brandsList){
    
    }
    
     /* * Initializes all variables when getting called from another controller * */
    public void initializeOnAddProductControllerCall(FXMLAddProductController addProductController, ObservableList<Tamanho> sizeList)
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
    public void initializeOnControllerCall(FXMLListSizeController listSizeController, ObservableList<Tamanho> sizeList)
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
    private void setObservableList(ObservableList<Tamanho> sizeList)
    {
        this.sizeList = sizeList;
    }
    
    /* * Adds a new size and updates the database * */
    @FXML void onAddClick(ActionEvent event)
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        
        Tamanho newSize = new Tamanho();
        newSize.setIdtamanho((byte) (sizeList.size() + 1));
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
    
    /* * Searches for a size with the same name as the new one in the size list * */
    private boolean checkForExistentSize(String name, String nonCharacters)
    {
        if(!(sizeList.isEmpty()))
        {
            for(Tamanho size : sizeList)
            {
                String sizeName = StringUtils.stripAccents(size.getDescricao().replaceAll(nonCharacters, "").toLowerCase());

                if(name.equals(sizeName))
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
        String newSizeName = StringUtils.stripAccents(sizeName.getText().replaceAll(nonCharacters, "").toLowerCase());
        
        boolean exists = checkForExistentSize(newSizeName, nonCharacters);
       
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
    private void insertSize(Tamanho size)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Transaction tx = session.beginTransaction();
        session.save(size);

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
