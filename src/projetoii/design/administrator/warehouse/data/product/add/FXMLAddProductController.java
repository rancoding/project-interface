/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.product.add;

import bll.BrandBLL;
import bll.CategoryBLL;
import bll.ColorBLL;
import bll.ProductBLL;
import bll.SizeBLL;
import hibernate.HibernateGenericLibrary;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.commons.lang3.text.WordUtils;
import projetoii.design.administrator.warehouse.data.brand.add.FXMLAddBrandController;
import projetoii.design.administrator.warehouse.data.category.add.FXMLAddCategoryController;
import projetoii.design.administrator.warehouse.data.color.add.FXMLAddColorController;
import projetoii.design.administrator.warehouse.data.product.list.FXMLListProductController;
import projetoii.design.administrator.warehouse.data.size.add.FXMLAddSizeController;
import services.BrandService;
import services.CategoryService;
import services.ColorService;
import services.ProductService;
import services.SizeService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLAddProductController implements Initializable {
    
    @FXML private TextField barCodeText;
    @FXML private TextField nameText;
    @FXML public ComboBox brandComboBox;
    @FXML public ComboBox typeComboBox;
    @FXML public ComboBox sizeComboBox;
    @FXML public ComboBox genderComboBox;
    @FXML public ComboBox colorComboBox;
    @FXML private TextField buyPriceText;
    @FXML private TextField sellPriceText;
    @FXML private Button addProductButton;
    @FXML private Label errorLabel;
    
    ObservableList<BrandBLL> marcaObservableList;
    ObservableList<CategoryBLL> tipoProdutoObservableList;
    ObservableList<SizeBLL> tamanhoObservableList;
    ObservableList<ColorBLL> corObservableList;
    
    private FXMLListProductController listProductController;
    private ObservableList<ProductBLL> productList;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        addProductButton.setDisable(true);
        fillBrandComboBox();
        fillTypeProductComboBox();
        fillSizeComboBox();
        fillGenderComboBox();
        fillColorComboBox();
    }    
    
    public void initializeOnControllerCall(FXMLListProductController listProductController, ObservableList<ProductBLL> productList)
    {
        setListController(listProductController);
        setObservableList(productList);
    }
    
     /* * Sets list controller * */
    private void setListController(FXMLListProductController listProductController)
    {
        this.listProductController = listProductController;
    }
    
    /* * Sets observable list from a given observable list * */
    private void setObservableList(ObservableList<ProductBLL> productList)
    {
        this.productList = productList;
    }

    
    @FXML void addProduto(ActionEvent event){

        String digits = "[^0-9.]";
        ProductBLL prod = new ProductBLL();
        
        prod.setCodbarras(Long.parseLong(barCodeText.getText()));
        prod.setDescricao(WordUtils.capitalizeFully(nameText.getText()));
        prod.setMarca((BrandBLL) brandComboBox.getSelectionModel().getSelectedItem());
        prod.setTamanho((SizeBLL) sizeComboBox.getSelectionModel().getSelectedItem());
        prod.setTipoproduto((CategoryBLL) typeComboBox.getSelectionModel().getSelectedItem());
        prod.setGenero(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex()));
        prod.setCor((ColorBLL) colorComboBox.getSelectionModel().getSelectedItem());
        prod.setPrecocompra(Double.parseDouble(buyPriceText.getText().replaceAll(digits, "")));
        prod.setPrecovenda(Double.parseDouble(sellPriceText.getText().replaceAll(digits, "")));

        productList.add(prod);
        insertProduct(prod);

        if(this.listProductController != null)
        {
            this.listProductController.setSearchedTableValues(productList);
            this.listProductController.productTable.refresh();
        }

        
        closeStage(event);
        
    }
    
    private boolean showConfirmation(String title, String header){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        
        Optional<ButtonType> option = alert.showAndWait();
        return option.get() == ButtonType.OK;
    }
    
    public boolean emptyVariables(){
        if(barCodeText.getText().isEmpty() || 
                nameText.getText().isEmpty() ||
                brandComboBox.getSelectionModel().isEmpty() || 
                sizeComboBox.getSelectionModel().isEmpty() || 
                genderComboBox.getSelectionModel().isEmpty() || 
                typeComboBox.getSelectionModel().isEmpty() ||
                colorComboBox.getSelectionModel().isEmpty() ||
                buyPriceText.getText().isEmpty() || sellPriceText.getText().isEmpty() ) {
            
            return true;
        }
        
        return false;
    }
    
    public void resetInputs(ActionEvent event){
        this.barCodeText.setText(null);
        this.nameText.setText(null);
        this.brandComboBox.setValue(null);
        this.typeComboBox.setValue(null);
        this.sizeComboBox.setValue(null);
        this.colorComboBox.setValue(null);
        this.buyPriceText.setText(null);
        this.sellPriceText.setText(null);  
    }
    
     /* * Inserts entity into the database * */
    private void insertProduct(ProductBLL product)
    {
        HibernateGenericLibrary.saveObject(product);
    }
    
    /* * Enables or disables the button * */
    @FXML void setAddButtonUsability()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String digits = "[^0-9.]";
                
        String newProductCode = barCodeText.getText();
       
        boolean exists = ProductService.checkForExistentProduct(productList, newProductCode);
        boolean empty = emptyVariables();
        
        if(!empty)
        {
            if(barCodeText.getText().isEmpty())
            {
                addProductButton.setDisable(true);
            }
            else
            {
                if(exists)
                {
                    errorLabel.setText("O produto que inseriu já existe");
                    addProductButton.setDisable(true);
                }
                else
                {
                    addProductButton.setDisable(false);
                    errorLabel.setText("");
                }
            }

            String buyPrice = buyPriceText.getText().replaceAll(digits, "");
            String sellPrice = sellPriceText.getText().replaceAll(digits, "");

            if(buyPrice.isEmpty() || sellPrice.isEmpty())
            {
                addProductButton.setDisable(true);
            }
            else
            {
                if((Double.parseDouble(buyPrice)) >= (Double.parseDouble(sellPrice)))
                { 
                    errorLabel.setText("O preço de compra não pode ser maior ou igual ao preço de venda");
                    addProductButton.setDisable(true);
                }
                else{
                    addProductButton.setDisable(false);
                    errorLabel.setText("");
                }
            }
        }
        else
        {
            addProductButton.setDisable(true);
            errorLabel.setText("Variável vazia"); 
        }
    }
    
    public void fillBrandComboBox()
    {
        List<BrandBLL> brandList = BrandService.getConvertedBrandList();
        this.marcaObservableList = FXCollections.observableArrayList(brandList);

        
        brandComboBox.setItems(this.marcaObservableList);   
    }
    
    public void fillTypeProductComboBox()
    {
        List<CategoryBLL> tipoProdutoList = CategoryService.getConvertedCategoryList();
        this.tipoProdutoObservableList = FXCollections.observableArrayList(tipoProdutoList);
        
        typeComboBox.setItems(this.tipoProdutoObservableList);
    }
    
    public void fillSizeComboBox()
    {
        List<SizeBLL> tamanhoList = SizeService.getConvertedSizeList();
        this.tamanhoObservableList = FXCollections.observableArrayList(tamanhoList);
        
        sizeComboBox.setItems(this.tamanhoObservableList);
    }
    
    public void fillGenderComboBox()
    {
        ObservableList genderObservableList;

        List genderList = new ArrayList<>();
        
        genderList.add("UniSexo");
        genderList.add("Masculino");
        genderList.add("Feminino");
        
        genderObservableList = FXCollections.observableArrayList(genderList);
        genderComboBox.setItems(genderObservableList);
    }
    
    public void fillColorComboBox()
    {
        List<ColorBLL> corList = ColorService.getConvertedColorList();
        this.corObservableList = FXCollections.observableArrayList(corList);
     
        colorComboBox.setItems(this.corObservableList);
    }
    
    private char getComboBoxGender(int posicao)
    {
        switch(posicao)
        {
            case 0: 
                return 'U';
            case 1: 
                return 'M';
            case 2: 
                return 'F';
        }    
        
        return 0;
    }
    
    @FXML
    private void changetoAddBrand(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(FXMLAddBrandController.class.getResource("FXMLAddBrand.fxml"));
        Parent root = (Parent) loader.load();
        
        FXMLAddBrandController addController = (FXMLAddBrandController) loader.getController();
        addController.initializeOnAddProductControllerCall(this,marcaObservableList);
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    @FXML
    private void changetoAddSize(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(FXMLAddSizeController.class.getResource("FXMLAddSize.fxml"));
        Parent root = (Parent) loader.load();
        
        FXMLAddSizeController addController = (FXMLAddSizeController) loader.getController();
        addController.initializeOnAddProductControllerCall(this,tamanhoObservableList);
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    @FXML
    private void changetoAddCategory(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(FXMLAddCategoryController.class.getResource("FXMLAddCategory.fxml"));
        Parent root = (Parent) loader.load();
        
        FXMLAddCategoryController addController = (FXMLAddCategoryController) loader.getController();
        addController.initializeOnAddProductControllerCall(this, tipoProdutoObservableList);
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void changetoAddColor(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(FXMLAddColorController.class.getResource("FXMLAddColor.fxml"));
        Parent root = (Parent) loader.load();
        
        FXMLAddColorController addController = (FXMLAddColorController) loader.getController();
        addController.initializeOnAddProductControllerCall(this, corObservableList);
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
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
