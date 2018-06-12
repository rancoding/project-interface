/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.product.detail;

import dao.Produto;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import projetoii.design.administrator.warehouse.data.product.list.FXMLListProductController;


/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLProductDetailController implements Initializable {

    @FXML private TextField barCodeText;
    @FXML private TextField nameText;
    @FXML private ComboBox brandComboBox;
    @FXML private ComboBox typeComboBox;
    @FXML private ComboBox sizeComboBox;
    @FXML private ComboBox genderComboBox;
    @FXML private ComboBox colorComboBox;
    @FXML private TextField buyPriceText;
    @FXML private TextField sellPriceText;
    
      private FXMLListProductController listProductController;
    private ObservableList<Produto> productList;
    private Produto product;
    
    
   
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
     /* * To be called when needing to initialize values from the list category controller * */
    public void initializeOnControllerCall(Produto product)
    {
        /* Sets all variables accordingly to received parameters */
        
        setProduct(product);
        setField();
    }
    
    
    private void setProduct(Produto product)
    {
        this.product = product;
    }
    
    private void setField()
    {
        this.barCodeText.setText(String.valueOf(product.getCodbarras()));
        this.nameText.setText(product.getDescricao());
        this.brandComboBox.setValue(product.getMarca().getNome());
        this.sizeComboBox.setValue(product.getTamanho().getDescricao());
        this.typeComboBox.setValue(product.getTipoproduto().getNome());
        this.genderComboBox.setValue(product.getGenero());
        this.colorComboBox.setValue(product.getCor().getNome());
        this.buyPriceText.setText(String.valueOf(product.getPrecocompra()));
        this.sellPriceText.setText(String.valueOf(product.getPrecovenda()));     
    }
    
}
