/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.box.list;

import bll.ProductBLL;
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
    private ObservableList<ProductBLL> productList;
    private ProductBLL product;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void initializeOnControllerCall(FXMLListProductController listProductController, ObservableList<ProductBLL> productList, ProductBLL product)
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
    
    private void setProductTypeList(ObservableList<ProductBLL> productList)
    {
        this.productList = productList;
    }
    
    private void setProductType(ProductBLL product)
    {
        this.product = product;
    }
    
    private void setField()
    {
        
    }
    
}
