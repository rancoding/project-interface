/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.product.list;

import dao.Produto;
import dao.Tipoproduto;
import hibernate.HibernateUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.apache.commons.lang3.StringUtils;
import projetoii.design.administrator.warehouse.box.list.FXMLListBoxController;
import projetoii.design.administrator.warehouse.data.product.add.FXMLAddProductController;
import projetoii.design.administrator.warehouse.data.product.detail.FXMLProductDetailController;
import projetoii.design.administrator.warehouse.data.product.edit.FXMLEditProductController;


/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLListProductController implements Initializable {
    
     /* Variables used for setting up the table content */
    @FXML public TableView<Produto> productTable;
    @FXML private TableColumn<Produto, String> barCodeColumn;
    @FXML private TableColumn<Produto, String> nameColumn;
    @FXML private TableColumn<Produto, String> categoryColumn;
    @FXML private TableColumn<Produto, String> sellPriceColumn;
    @FXML private TableColumn<Produto, String> editColumn;
    @FXML private TableColumn<Produto, String> boxColumn;
    @FXML private TableColumn<Produto, String> detailColumn;
    private ObservableList<Produto> productObservableList;
    
    /* Text field used to search categories on the table, updating as it searches */
    @FXML private TextField searchProductTextField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        /* Initializes and opens the database session using hibernate */
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        /* Retrieves all database product types to an arraylist and initializes the table values if it is not empty */
        List<Produto> productList = session.createCriteria(Produto.class).list();
        
        
        
        if(!(productList.isEmpty()))
        {
            initializeTable(productList);
        }
        else
        {
            productList = new ArrayList<Produto>();
            initializeTable(productList);
        }
        
        /* Closes the database connection */
       // session.close();
        
        
    }    
    
    /** Initializes all table content for the first time **/
    private void initializeTable(List<Produto> productList)
    {
        /* Sets column variables to use entity info, empty for a button creation */
        this.barCodeColumn.setCellValueFactory(new PropertyValueFactory<>("codbarras"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.categoryColumn.setCellValueFactory(new PropertyValueFactory<>("tipoproduto"));
        this.sellPriceColumn.setCellValueFactory(new PropertyValueFactory<>("precovenda"));

        this.editColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        this.boxColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        this.detailColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        
        /* Sets images for all row buttons and sets the buttons up */
        Image image = new Image(getClass().getResourceAsStream("image/edit.png"));
        Image imageHover = new Image(getClass().getResourceAsStream("image/edit_hover.png"));
        editColumn.setCellFactory(getButtonEditCell(image, imageHover));
        
        /* Sets images for all row buttons and sets the buttons up */
        Image imageBox = new Image(getClass().getResourceAsStream("image/box.png"));
        Image imageHoverBox = new Image(getClass().getResourceAsStream("image/box_hover.png"));
        boxColumn.setCellFactory(getButtonBoxCell(imageBox, imageHoverBox));
        
        /* Sets images for all row buttons and sets the buttons up */
        Image imageDetail = new Image(getClass().getResourceAsStream("image/detail.png"));
        Image imageHoverDetail = new Image(getClass().getResourceAsStream("image/detail_hover.png"));
        detailColumn.setCellFactory(getButtonDetailCell(imageDetail, imageHoverDetail));
        
        /* Sets the table content */
        productObservableList = FXCollections.observableArrayList(productList);
        setTableItems(productObservableList);
    }
    
    /* * Sets the table items to be the same as the observable list items * */
    private void setTableItems(ObservableList<Produto> productObservableList)
    {
        this.productTable.setItems(productObservableList);
    }
    
    /* Creates a button for each table cell, also setting up an image for each button (with a different hover image and size) */
    private Callback getButtonEditCell(Image image, Image imageHover)
    {
        Callback<TableColumn<Produto, String>, TableCell<Produto, String>> cellFactory;
        cellFactory = new Callback<TableColumn<Produto, String>, TableCell<Produto, String>>()
        {
            @Override
            public TableCell call(final TableColumn<Produto, String> param)
            {
                final TableCell<Produto, String> cell = new TableCell<Produto, String>()
                {
                    final Button button = new Button();
                    
                    @Override
                    public void updateItem(String item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if(empty)
                        {
                            setGraphic(null);
                            setText(null);
                        }
                        else
                        {
                            /* On edit button, opens an edit category window with the row category info and the list of existent categories */
                            button.setOnAction((event) -> {
                                Produto type = getTableView().getItems().get(getIndex());
                                loadNewEditWindow(FXMLEditProductController.class, "FXMLEditProduct.fxml", "Armazém - Editar Produto", "Não foi possível carregar o ficheiro FXMLEditProduct.fxml", type);
                            });
                            
                            setGraphic(button);
                            setText(null);
                            
                            ImageView imageView = new ImageView();
                            setButtonImageView(imageView, image, 12, 12);
                            setRowButton(button, imageView, image, imageHover);
                        }
                    }
                };
                
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        };
        
        return cellFactory;
    }
    
     /* Creates a button for each table cell, also setting up an image for each button (with a different hover image and size) */
    private Callback getButtonBoxCell(Image image, Image imageHover)
    {
        Callback<TableColumn<Produto, String>, TableCell<Produto, String>> cellFactory;
        cellFactory = new Callback<TableColumn<Produto, String>, TableCell<Produto, String>>()
        {
            @Override
            public TableCell call(final TableColumn<Produto, String> param)
            {
                final TableCell<Produto, String> cell = new TableCell<Produto, String>()
                {
                    final Button button = new Button();
                    
                    @Override
                    public void updateItem(String item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if(empty)
                        {
                            setGraphic(null);
                            setText(null);
                        }
                        else
                        {
                            /* On edit button, opens an edit category window with the row category info and the list of existent categories */
                            button.setOnAction((event) -> {
                                Produto type = getTableView().getItems().get(getIndex());
                                loadNewBoxWindow(FXMLListBoxController.class, "FXMLListBox.fxml", "Armazém - Editar Produto", "Não foi possível carregar o ficheiro FXMLListBox.fxml", type);
                            });
                            
                            setGraphic(button);
                            setText(null);
                            
                            ImageView imageView = new ImageView();
                            setButtonImageView(imageView, image, 12, 12);
                            setRowButton(button, imageView, image, imageHover);
                        }
                    }
                };
                
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        };
        
        return cellFactory;
    }
    
     /* Creates a button for each table cell, also setting up an image for each button (with a different hover image and size) */
    private Callback getButtonDetailCell(Image image, Image imageHover)
    {
        Callback<TableColumn<Produto, String>, TableCell<Produto, String>> cellFactory;
        cellFactory = new Callback<TableColumn<Produto, String>, TableCell<Produto, String>>()
        {
            @Override
            public TableCell call(final TableColumn<Produto, String> param)
            {
                final TableCell<Produto, String> cell = new TableCell<Produto, String>()
                {
                    final Button button = new Button();
                    
                    @Override
                    public void updateItem(String item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if(empty)
                        {
                            setGraphic(null);
                            setText(null);
                        }
                        else
                        {
                            /* On edit button, opens an edit category window with the row category info and the list of existent categories */
                            button.setOnAction((event) -> {
                                Produto product = getTableView().getItems().get(getIndex());
                                System.out.println(product);
                                loadNewDetailWindow(FXMLProductDetailController.class, "FXMLProductDetail.fxml", "Armazém - Detalhe Produto", "Não foi possível carregar o ficheiro FXMLProductDetail.fxml", product);
                            });
                            
                            setGraphic(button);
                            setText(null);
                            
                            ImageView imageView = new ImageView();
                            setButtonImageView(imageView, image, 12, 12);
                            setRowButton(button, imageView, image, imageHover);
                        }
                    }
                };
                
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        };
        
        return cellFactory;
    }
    
     /* * Sets the button image and size * */
    private void setButtonImageView(ImageView imageView, Image image, double width, double height)
    {
        imageView.setImage(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }
    
    /* * Sets a button image for each button and its hover * */
    private void setRowButton(Button button, ImageView imageView, Image image, Image imageHover)
    {
        button.setBackground(Background.EMPTY);
        button.setGraphic(imageView);

        button.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if(newValue) // On hover
            {
                setButtonImageView(imageView, imageHover, 14, 14);
                button.setGraphic(imageView);
            }
            else // Not on hover
            {
                setButtonImageView(imageView, image, 12, 12);
                button.setGraphic(imageView);
            }
        });
    }
    
    /* * Loads a new window on button click * */
    @FXML
    void handleAddButtonAction(ActionEvent event)
    {
        loadNewAddWindow(FXMLAddProductController.class, "FXMLAddProduct.fxml", "Armazém - Adicionar Produto", "Não foi possível carregar o ficheiro FXMLAddProduct.fxml");
    }
    
    /* * Loads a new add window * */
    private void loadNewAddWindow(Class controller, String fileName, String title, String message)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
             FXMLAddProductController addController = (FXMLAddProductController) loader.getController();
            addController.initializeOnControllerCall(this, productObservableList);
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e)
        {
            System.out.println(message);
        }
    }
    
    /* * Loads a new edit window * */
    private void loadNewEditWindow(Class controller, String fileName, String title, String message, Produto type)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLEditProductController editController = (FXMLEditProductController) loader.getController();
            editController.initializeOnControllerCall(this, productObservableList, type);
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e)
        {
            System.out.println(message);
        }
    }
    
    /* * Loads a new edit window * */
    private void loadNewBoxWindow(Class controller, String fileName, String title, String message, Produto type)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLListBoxController listController = (FXMLListBoxController) loader.getController();
            listController.initializeOnControllerCall(this, productObservableList, type);
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e)
        {
            System.out.println(message);
        }
    }
    
    /* * Loads a new detail window * */
    private void loadNewDetailWindow(Class controller, String fileName, String title, String message, Produto product)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLProductDetailController detailController = (FXMLProductDetailController) loader.getController();
            detailController.initializeOnControllerCall(product);
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e)
        {
            System.out.println(message);
        }
    }
    
    /* * Searches for categories when a key is released * */
    @FXML
    void getSearchList()
    {
        List<Produto> typeList = new ArrayList<>();
            
        /* If something has been typed, tries to find an existent category with the given name or ID */
        if(searchProductTextField.getText().length() > 0)
        {
            typeList.clear();
            Tipoproduto type = new Tipoproduto();
            
            String nonCharacters = "[^\\p{L}\\p{Nd}]";
            
            for(Produto prod : productObservableList)
            {
                String searchString = StringUtils.stripAccents(searchProductTextField.getText().replaceAll(nonCharacters, "").toLowerCase());
                
                String productName = StringUtils.stripAccents(prod.getDescricao().replaceAll(nonCharacters, "").toLowerCase());
                String productBarCode = String.valueOf(prod.getCodbarras());
                //String productCategory = StringUtils.stripAccents(type.toString().replaceAll(nonCharacters, "").toLowerCase());
                
                if(productName.contains(searchString) || productBarCode.contains(searchString))
                {
                    typeList.add(prod);
                }
            }
            
            setSearchedTableValues(typeList);
        }
        else /* If nothing has been typed, show full category list */
        {
            typeList.clear();
            
            typeList = productObservableList;
            setSearchedTableValues(typeList);
        }
    }
    
    /* * Sets new table values * */
    public void setSearchedTableValues(List<Produto> productList)
    {
        ObservableList<Produto> productObservableList;
        productObservableList = FXCollections.observableArrayList(productList);
        setTableItems(productObservableList);
    }
   
}
