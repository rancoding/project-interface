/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.product.edit;

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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.commons.lang3.text.WordUtils;
import projetoii.design.administrator.warehouse.data.product.list.FXMLListProductController;
import services.BrandService;
import services.CategoryService;
import services.ColorService;
import services.SizeService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLEditProductController implements Initializable {

    @FXML private TextField barCodeText;
    @FXML private TextField nameText;
    @FXML private ComboBox brandComboBox;
    @FXML private ComboBox typeComboBox;
    @FXML private ComboBox sizeComboBox;
    @FXML private ComboBox genderComboBox;
    @FXML private ComboBox colorComboBox;
    @FXML private TextField buyPriceText;
    @FXML private TextField sellPriceText;
    @FXML private Button updateButton;
    @FXML private Button CancelButton;
    
    private FXMLListProductController listProductController;
    private ObservableList<ProductBLL> productList;
    private ProductBLL product;
    
    ObservableList<BrandBLL> marcaObservableList;
    ObservableList<CategoryBLL> tipoProdutoObservableList;
    ObservableList<SizeBLL> tamanhoObservableList;
    ObservableList<ColorBLL> corObservableList;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        List<BrandBLL> marcaList = BrandService.getConvertedBrandList();
        this.marcaObservableList = FXCollections.observableArrayList(marcaList);

        fillGenderComboBox();
        fillColorComboBox();
        fillTypeProductComboBox();
        fillSizeComboBox();
        fillBrandComboBox();
        brandComboBox.getSelectionModel().select(0);
    }    
    
    public void initializeOnControllerCall(FXMLListProductController listProductsController, ObservableList<ProductBLL> productList, ProductBLL product)
    {
        /* Sets all variables accordingly to received parameters */
        setListProductController(listProductsController);
        setProductList(productList);
        setProduct(product);
        setField();
                
        setSelectedGenderComboBox(product.getGenero());
    }
    
    private void setListProductController(FXMLListProductController listProductController)
    {
        this.listProductController = listProductController;
    }
    
    private void setProductList(ObservableList<ProductBLL> productList)
    {
        this.productList = productList;
    }
    
    private void setProduct(ProductBLL product)
    {
        this.product = product;
    }
    
    private void setField()
    {
        this.barCodeText.setText(String.valueOf(product.getCodbarras()));
        this.nameText.setText(product.getDescricao());
        this.brandComboBox.setValue(product.getMarca());
        this.sizeComboBox.setValue(product.getTamanho());
        this.typeComboBox.setValue(product.getTipoproduto());
        this.genderComboBox.setValue(product.getGenero());
        this.colorComboBox.setValue(product.getCor());
        this.buyPriceText.setText(String.valueOf(product.getPrecocompra()));
        this.sellPriceText.setText(String.valueOf(product.getPrecovenda()));     
    }
    
    
  ///////////////////////////// Fill Combo Boxes ///////////////////////////////
    
    public void fillBrandComboBox(){
        brandComboBox.getItems().addAll(this.marcaObservableList);
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
    
    ////////////////////////////////////////////////////////////////////////////
    
     private char getComboBoxGender(int posicao) {
        switch(posicao){
            case 0 : 
                return 'U';
            case 1: 
                return 'M';
            case 2: 
                return 'F';
        }    
        return 0;
    }
     
     private void setSelectedGenderComboBox(char genero)
     {
        switch(genero)
        {
            case 'U' : 
                genderComboBox.getSelectionModel().select(0);
            case 'M': 
                genderComboBox.getSelectionModel().select(1);
            case 'F': 
                genderComboBox.getSelectionModel().select(2);
        }    
    }
    
    /* * Sets the new product name, updates in the database, refreshes the list controller table and closes current window * */
    @FXML
    void onEditButtonClick(ActionEvent event) throws IOException
    {
        updateProductList();
        updateProduct();
        
        this.listProductController.productTable.refresh();
        closeStage(event);
    }
    
    /* * Updates entity on database * */
    private void updateProduct()
    {
        List<ProductBLL> products = HibernateGenericLibrary.executeHQLQuery("FROM Produto WHERE codbarras = " + product.getCodbarras());
        
        ProductBLL oldProduct = products.get(0);
        oldProduct.setCodbarras(Long.parseLong(barCodeText.getText()));
        oldProduct.setDescricao(WordUtils.capitalizeFully(nameText.getText()));
        oldProduct.setMarca((BrandBLL) brandComboBox.getSelectionModel().getSelectedItem());
        oldProduct.setTamanho((SizeBLL) sizeComboBox.getSelectionModel().getSelectedItem());
        oldProduct.setTipoproduto((CategoryBLL) typeComboBox.getSelectionModel().getSelectedItem());
        oldProduct.setGenero(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex()));
        oldProduct.setCor((ColorBLL) colorComboBox.getSelectionModel().getSelectedItem());
        oldProduct.setPrecocompra(Double.parseDouble(buyPriceText.getText()));
        oldProduct.setPrecovenda(Double.parseDouble(sellPriceText.getText()));
        
        HibernateGenericLibrary.updateObject(oldProduct);
    }
    
    private void updateProductList()
    {
        if(!(product.getDescricao().equals(nameText.getText())))
        {
            product.setDescricao(WordUtils.capitalizeFully(nameText.getText()));
        }
        
        if(!(product.getCodbarras()==(Long.valueOf(barCodeText.getText()))))
        {
           for(ProductBLL prod : productList)
           {
               if(prod.getCodbarras()==(Long.valueOf(barCodeText.getText())))
               {
                   
               }
               else
               {   
                   product.setCodbarras(Long.valueOf(barCodeText.getText()));
               }
           }    
        }
        
        if(!(product.getTipoproduto()==((CategoryBLL) typeComboBox.getSelectionModel().getSelectedItem())))
        {
            product.setTipoproduto((CategoryBLL) typeComboBox.getSelectionModel().getSelectedItem());
        }
        
        if(!(product.getGenero()==(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex()))))
        {
            product.setGenero(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex()));
        }
        else
        {
        }
        
        if(!(product.getMarca()==((BrandBLL) brandComboBox.getSelectionModel().getSelectedItem())))
        {
            product.setMarca((BrandBLL) brandComboBox.getSelectionModel().getSelectedItem());
        }
        
        if(!(product.getCor()==((ColorBLL) colorComboBox.getSelectionModel().getSelectedItem())))
        {
            product.setCor((ColorBLL) colorComboBox.getSelectionModel().getSelectedItem());
        }
        
        if(!(product.getTamanho()==((SizeBLL) sizeComboBox.getSelectionModel().getSelectedItem())))
        {
            product.setTamanho((SizeBLL) sizeComboBox.getSelectionModel().getSelectedItem());
        }
        
        if(!(product.getPrecocompra()==(Double.parseDouble(buyPriceText.getText()))))
        {
            product.setPrecocompra(Double.parseDouble(buyPriceText.getText()));
        }
        
        if(!(product.getPrecovenda()==(Double.parseDouble(sellPriceText.getText()))))
        {
            product.setPrecovenda(Double.parseDouble(sellPriceText.getText()));
        }
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
