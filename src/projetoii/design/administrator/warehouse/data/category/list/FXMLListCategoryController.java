package projetoii.design.administrator.warehouse.data.category.list;

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
import projetoii.design.administrator.warehouse.data.category.add.FXMLAddCategoryController;
import projetoii.design.administrator.warehouse.data.category.edit.FXMLEditCategoryController;

public class FXMLListCategoryController implements Initializable {

    /* Variables used for setting up the table content */
    @FXML public TableView<Tipoproduto> categoryTable;
    @FXML private TableColumn<Tipoproduto, Byte> idColumn;
    @FXML private TableColumn<Tipoproduto, String> nameColumn;
    @FXML private TableColumn<Tipoproduto, String> editColumn;
    private ObservableList<Tipoproduto> productTypeObservableList;
    
    /* Text field used to search categories on the table, updating as it searches */
    @FXML private TextField searchCategoryTextField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        /* Initializes and opens the database session using hibernate */
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        /* Retrieves all database product types to an arraylist and initializes the table values if it is not empty */
        List<Tipoproduto> productTypeList = session.createCriteria(Tipoproduto.class).list();
        
        if(!(productTypeList.isEmpty()))
        {
            initializeTable(productTypeList);
        }
        else
        {
            productTypeList = new ArrayList<>();
            initializeTable(productTypeList);
        }
        
        /* Closes the database connection */
        session.close();
    }
    
    /** Initializes all table content for the first time **/
    private void initializeTable(List<Tipoproduto> productTypeList)
    {
        /* Sets column variables to use entity info, empty for a button creation */
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("idtipoproduto"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.editColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        
        /* Sets images for all row buttons and sets the buttons up */
        Image image = new Image(getClass().getResourceAsStream("image/edit.png"));
        Image imageHover = new Image(getClass().getResourceAsStream("image/edit_hover.png"));
        editColumn.setCellFactory(getButtonCell(image, imageHover));
        
        /* Sets the table content */
        productTypeObservableList = FXCollections.observableArrayList(productTypeList);
        setTableItems(productTypeObservableList);
    }
    
    /* * Sets the table items to be the same as the observable list items * */
    private void setTableItems(ObservableList<Tipoproduto> productTypeObservableList)
    {
        this.categoryTable.setItems(productTypeObservableList);
    }
    
    /* Creates a button for each table cell, also setting up an image for each button (with a different hover image and size) */
    private Callback getButtonCell(Image image, Image imageHover)
    {
        Callback<TableColumn<Tipoproduto, String>, TableCell<Tipoproduto, String>> cellFactory;
        cellFactory = new Callback<TableColumn<Tipoproduto, String>, TableCell<Tipoproduto, String>>()
        {
            @Override
            public TableCell call(final TableColumn<Tipoproduto, String> param)
            {
                final TableCell<Tipoproduto, String> cell = new TableCell<Tipoproduto, String>()
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
                                Tipoproduto type = getTableView().getItems().get(getIndex());
                                loadNewEditWindow(FXMLEditCategoryController.class, "FXMLEditCategory.fxml", "Armazém - Editar Categoria", "Não foi possível carregar o ficheiro FXMLEditCategory.fxml", type);
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
        loadNewAddWindow(FXMLAddCategoryController.class, "FXMLAddCategory.fxml", "Armazém - Adicionar Categoria", "Não foi possível carregar o ficheiro FXMLAddCategory.fxml");
    }
    
    /* * Loads a new add window * */
    private void loadNewAddWindow(Class controller, String fileName, String title, String message)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLAddCategoryController addController = (FXMLAddCategoryController) loader.getController();
            addController.initializeOnControllerCall(this, productTypeObservableList);
            
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
    private void loadNewEditWindow(Class controller, String fileName, String title, String message, Tipoproduto type)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLEditCategoryController editController = (FXMLEditCategoryController) loader.getController();
            editController.initializeOnControllerCall(this, productTypeObservableList, type);
            
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
    
    /* * Searches for categories when a key is pressed * */
    @FXML
    void getSearchList()
    {
        List<Tipoproduto> typeList = new ArrayList<>();
            
        /* If something has been typed, tries to find an existent category with the given name or ID */
        if(searchCategoryTextField.getText().length() > 0)
        {
            typeList.clear();
            
            String nonCharacters = "[^\\p{L}\\p{Nd}]";
            
            for(Tipoproduto type : productTypeObservableList)
            {
                String searchString = StringUtils.stripAccents(searchCategoryTextField.getText().replaceAll(nonCharacters, "").toLowerCase());
                
                String categoryName = StringUtils.stripAccents(type.getNome().replaceAll(nonCharacters, "").toLowerCase());
                String categoryID = String.valueOf(type.getIdtipoproduto());
                
                if(categoryName.contains(searchString) || categoryID.contains(searchString))
                {
                    typeList.add(type);
                }
            }
            
            setSearchedTableValues(typeList);
        }
        else /* If nothing has been typed, show full category list */
        {
            typeList.clear();
            
            typeList = productTypeObservableList;
            setSearchedTableValues(typeList);
        }
    }
    
    /* * Sets new table values * */
    public void setSearchedTableValues(List<Tipoproduto> typeList)
    {
        ObservableList<Tipoproduto> typeObservableList;
        typeObservableList = FXCollections.observableArrayList(typeList);
        setTableItems(typeObservableList);
    }
}
