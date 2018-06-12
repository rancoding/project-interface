/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.box.list;

import dao.Produto;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import projetoii.design.administrator.warehouse.data.product.list.FXMLListProductController;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLListBoxController implements Initializable {

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
    
    public void initializeOnControllerCall(FXMLListProductController listProductController, ObservableList<Produto> productList, Produto product)
    {
        /* Sets all variables accordingly to received parameters */
        setListCategoryController(listProductController);
        setProductTypeList(productList);
        setProductType(product);
        setField();
    }
    
    private void setListCategoryController(FXMLListProductController listProductController)
    {
        this.listProductController = listProductController;
    }
    
    private void setProductTypeList(ObservableList<Produto> productList)
    {
        this.productList = productList;
    }
    
    private void setProductType(Produto product)
    {
        this.product = product;
    }
    
    private void setField()
    {
        
    }
    
}
