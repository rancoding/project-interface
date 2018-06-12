/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.product.edit;

import dao.Cor;
import dao.Marca;
import dao.Produto;
import dao.Tamanho;
import dao.Tipoproduto;
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
import org.hibernate.Session;
import org.hibernate.Transaction;
import projetoii.design.administrator.warehouse.data.product.list.FXMLListProductController;

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
    private ObservableList<Produto> productList;
    private Produto product;
    
    ObservableList<Marca> marcaObservableList;
    ObservableList<Tipoproduto> tipoProdutoObservableList;
    ObservableList<Tamanho> tamanhoObservableList;
    ObservableList<Cor> corObservableList;
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Marca> marcaList = session.createCriteria(Marca.class).list();
        this.marcaObservableList = FXCollections.observableArrayList(marcaList);

        fillGenderComboBox();
        fillColorComboBox();
        fillTypeProductComboBox();
        fillSizeComboBox();
        fillBrandComboBox();
        brandComboBox.getSelectionModel().select(0);
        setBrandComboBoxConverter();
        
        
        session.close();
    }    
    
    public void initializeOnControllerCall(FXMLListProductController listProductsController, ObservableList<Produto> productList, Produto product)
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
    
    private void setProductList(ObservableList<Produto> productList)
    {
        this.productList = productList;
    }
    
    private void setProduct(Produto product)
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
        
    private void setBrandComboBoxConverter(){
        brandComboBox.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                return ((Marca) object).getNome();
            }

            @Override
            public Object fromString(String string) {
                return "";
            }
        });
    
    }    
    public void fillTypeProductComboBox(){
        
        Session session = HibernateUtil.getSessionFactory().openSession();
       
        
        

        List<Tipoproduto> tipoProdutoList = session.createCriteria(Tipoproduto.class).list();
        this.tipoProdutoObservableList = FXCollections.observableArrayList(tipoProdutoList);

     
        typeComboBox.setItems(this.tipoProdutoObservableList);
        typeComboBox.setConverter(new StringConverter<Tipoproduto>()
        {
                    @Override
                    public String toString(Tipoproduto object) {
                        return object.getNome();
                    }

                    @Override
                    public Tipoproduto fromString(String string) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
        });    
        session.close();

    }
    
    public void fillSizeComboBox(){
        
        Session session = HibernateUtil.getSessionFactory().openSession();
       
        
        

        List<Tamanho> tamanhoList = session.createCriteria(Tamanho.class).list();
        this.tamanhoObservableList = FXCollections.observableArrayList(tamanhoList);

        
        sizeComboBox.setItems(this.tamanhoObservableList);
        sizeComboBox.setConverter(new StringConverter<Tamanho>()
        {
                    @Override
                    public String toString(Tamanho object) {
                        return object.getDescricao();
                    }

                    @Override
                    public Tamanho fromString(String string) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
        });  
        session.close();
    }
    
    public void fillGenderComboBox(){
    
        
        ObservableList genderObservableList;

        List genderList = new ArrayList<>();
        
        genderList.add("UniSexo");
        genderList.add("Masculino");
        genderList.add("Feminino");
        
        genderObservableList = FXCollections.observableArrayList(genderList);
        
        
    
        genderComboBox.setItems(genderObservableList);
        
        
    }
    
    public void fillColorComboBox(){
        
        Session session = HibernateUtil.getSessionFactory().openSession();
       
        
        

        List<Cor> corList = session.createCriteria(Cor.class).list();
        this.corObservableList = FXCollections.observableArrayList(corList);

     
        colorComboBox.setItems(this.corObservableList);
        colorComboBox.setConverter(new StringConverter<Cor>()
        {
                    @Override
                    public String toString(Cor object) {
                        return object.getNome();
                    }

                    @Override
                    public Cor fromString(String string) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
        });  
        session.close();
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
     
     private void setSelectedGenderComboBox(char genero) {
        switch(genero){
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
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Transaction tx = session.beginTransaction();
        
        Produto productUpdate = (Produto)session.get(Produto.class, product.getCodbarras());

  
        
        productUpdate.setCodbarras(Long.parseLong(barCodeText.getText()));
        productUpdate.setDescricao(WordUtils.capitalizeFully(nameText.getText()));
        productUpdate.setMarca((Marca) brandComboBox.getSelectionModel().getSelectedItem());
        productUpdate.setTamanho((Tamanho) sizeComboBox.getSelectionModel().getSelectedItem());
        productUpdate.setTipoproduto((Tipoproduto) typeComboBox.getSelectionModel().getSelectedItem());
        productUpdate.setGenero(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex()));
        productUpdate.setCor((Cor) colorComboBox.getSelectionModel().getSelectedItem());
        productUpdate.setPrecocompra(Double.parseDouble(buyPriceText.getText()));
        productUpdate.setPrecovenda(Double.parseDouble(sellPriceText.getText()));
        
        
        session.update(productUpdate);
        tx.commit();
        
        session.close();
    }
    
    private void updateProductList(){
        if(!(product.getDescricao().equals(nameText.getText()))){
            product.setDescricao(WordUtils.capitalizeFully(nameText.getText()));
        }
        if(!(product.getCodbarras()==(Long.valueOf(barCodeText.getText())))){
           for(Produto prod : productList){
               if(prod.getCodbarras()==(Long.valueOf(barCodeText.getText()))){
                   
               }else{   
                   product.setCodbarras(Long.valueOf(barCodeText.getText()));
               }
           }    
        }
        if(!(product.getTipoproduto()==((Tipoproduto) typeComboBox.getSelectionModel().getSelectedItem()))){
            product.setTipoproduto((Tipoproduto) typeComboBox.getSelectionModel().getSelectedItem());
        }
        if(!(product.getGenero()==(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex())))){
            product.setGenero(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex()));
        } else {
        }
        if(!(product.getMarca()==((Marca) brandComboBox.getSelectionModel().getSelectedItem()))){
            product.setMarca((Marca) brandComboBox.getSelectionModel().getSelectedItem());
        }
        if(!(product.getCor()==((Cor) colorComboBox.getSelectionModel().getSelectedItem()))){
            product.setCor((Cor) colorComboBox.getSelectionModel().getSelectedItem());
        }
        if(!(product.getTamanho()==((Tamanho) sizeComboBox.getSelectionModel().getSelectedItem()))){
            product.setTamanho((Tamanho) sizeComboBox.getSelectionModel().getSelectedItem());
        }
        if(!(product.getPrecocompra()==(Double.parseDouble(buyPriceText.getText())))){
            product.setPrecocompra(Double.parseDouble(buyPriceText.getText()));
        }
        
        if(!(product.getPrecovenda()==(Double.parseDouble(sellPriceText.getText())))){
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
