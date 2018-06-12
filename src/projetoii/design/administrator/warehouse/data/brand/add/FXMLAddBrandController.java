package projetoii.design.administrator.warehouse.data.brand.add;

import dao.Marca;
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
import projetoii.design.administrator.warehouse.data.brand.list.FXMLListBrandController;
import projetoii.design.administrator.warehouse.data.product.add.FXMLAddProductController;

public class FXMLAddBrandController implements Initializable {
   
    @FXML private TextField brandName;
    @FXML private Button addBrandButton;
    @FXML private Label errorLabel;
    
    
    private FXMLAddProductController addProductController;
    private FXMLListBrandController listBrandController;
    private ObservableList<Marca> brandList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        addBrandButton.setDisable(true);
    }    
    
    public void setList(ObservableList brandsList){
        
    }    
 
    
    /* * Initializes all variables when getting called from another controller * */
    public void initializeOnAddProductControllerCall(FXMLAddProductController addProductController, ObservableList<Marca> brandList)
    {
        setListAddProductController(addProductController);
        setObservableList(brandList);
    }
    
    /* * Sets list controller * */
    private void setListAddProductController(FXMLAddProductController addProductController)
    {
        this.addProductController = addProductController;
    }

    /* * Initializes all variables when getting called from another controller * */
    public void initializeOnControllerCall(FXMLListBrandController listBrandController, ObservableList<Marca> brandList)
    {
        setListController(listBrandController);
        setObservableList(brandList);
    }
    
    /* * Sets list controller * */
    private void setListController(FXMLListBrandController listBrandController)
    {
        this.listBrandController = listBrandController;
    }
    
    /* * Sets observable list from a given observable list * */
    private void setObservableList(ObservableList<Marca> productTypeList)
    {
        this.brandList = productTypeList;
    }
    
    /* * Adds a new brand and updates the database * */
    @FXML void onAddClick(ActionEvent event)
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        
        Marca newBrand = new Marca();
        newBrand.setIdmarca((byte) (brandList.size() + 1));
        newBrand.setNome(StringUtils.capitalize(brandName.getText()));
        
        brandList.add(newBrand);
        insertBrand(newBrand);
        
        if(this.listBrandController != null)
        {
            this.listBrandController.setSearchedTableValues(brandList);
            this.listBrandController.brandTable.refresh();
        } 
        else if(this.addProductController != null)
        {
            this.addProductController.brandComboBox.getSelectionModel().select(brandList.size()-1);
        }
        
        closeStage(event);
    }
    
    /* * Searches for a brand with the same name as the new one in the brand list * */
    private boolean checkForExistentBrand(String name, String nonCharacters)
    {
        if(!(brandList.isEmpty()))
        {
            for(Marca brand : brandList)
            {
                String brandName = StringUtils.stripAccents(brand.getNome().replaceAll(nonCharacters, "").toLowerCase());

                if(name.equals(brandName))
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
        String newBrandName = StringUtils.stripAccents(brandName.getText().replaceAll(nonCharacters, "").toLowerCase());
        
        boolean exists = checkForExistentBrand(newBrandName, nonCharacters);
       
        if(brandName.getText().isEmpty())
        {
            addBrandButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(exists)
            {
                errorLabel.setText("Marca já existente");
                addBrandButton.setDisable(true);
            }
            else
            {
                errorLabel.setText("");
                addBrandButton.setDisable(false);
            }
        }
    }
    
    /* * Inserts entity into the database * */
    private void insertBrand(Marca brand)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Transaction tx = session.beginTransaction();
        session.save(brand);

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
