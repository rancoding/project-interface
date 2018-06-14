/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.brand.list;

import bll.BrandBLL;
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
import org.apache.commons.lang3.StringUtils;
import projetoii.design.administrator.warehouse.data.brand.add.FXMLAddBrandController;
import projetoii.design.administrator.warehouse.data.brand.edit.FXMLEditBrandController;
import services.BrandService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLListBrandController implements Initializable {

    /* Variables used for setting up the table content */
    @FXML public TableView<BrandBLL> brandTable;
    @FXML private TableColumn<BrandBLL, Byte> idColumn;
    @FXML private TableColumn<BrandBLL, String> nameColumn;
    @FXML private TableColumn<BrandBLL, String> editColumn;
    private ObservableList<BrandBLL> brandObservableList;
    
    /* Text field used to search brands on the table, updating as it searches */
    @FXML private TextField searchBrandTextField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        List<BrandBLL> brandList = BrandService.getConvertedBrandList();
        
        if(!(brandList.isEmpty()))
        {
            initializeTable(brandList);
        }
        else
        {
            brandList = new ArrayList<>();
            initializeTable(brandList);
        }
    }
    
    /** Initializes all table content for the first time **/
    private void initializeTable(List<BrandBLL> brandList)
    {
        /* Sets column variables to use entity info, empty for a button creation */
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("idmarca"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.editColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        
        /* Sets images for all row buttons and sets the buttons up */
        Image image = new Image(getClass().getResourceAsStream("image/edit.png"));
        Image imageHover = new Image(getClass().getResourceAsStream("image/edit_hover.png"));
        editColumn.setCellFactory(getButtonCell(image, imageHover));
        
        /* Sets the table content */
        brandObservableList = FXCollections.observableArrayList(brandList);
        setTableItems(brandObservableList);
    }
    
    /* * Sets the table items to be the same as the observable list items * */
    private void setTableItems(ObservableList<BrandBLL> brandObservableList)
    {
        this.brandTable.setItems(brandObservableList);
    }
    
    /* Creates a button for each table cell, also setting up an image for each button (with a different hover image and size) */
    private Callback getButtonCell(Image image, Image imageHover)
    {
        Callback<TableColumn<BrandBLL, String>, TableCell<BrandBLL, String>> cellFactory;
        cellFactory = new Callback<TableColumn<BrandBLL, String>, TableCell<BrandBLL, String>>()
        {
            @Override
            public TableCell call(final TableColumn<BrandBLL, String> param)
            {
                final TableCell<BrandBLL, String> cell = new TableCell<BrandBLL, String>()
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
                            /* On edit button, opens an edit category window with the row brand info and the list of existent brands */
                            button.setOnAction((event) -> {
                                BrandBLL brand = getTableView().getItems().get(getIndex());
                                loadNewEditWindow(FXMLEditBrandController.class, "FXMLEditBrand.fxml", "Armazém - Editar Marca", "Não foi possível carregar o ficheiro FXMLEditBrand.fxml", brand);
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
        loadNewAddWindow(FXMLAddBrandController.class, "FXMLAddBrand.fxml", "Armazém - Adicionar Marca", "Não foi possível carregar o ficheiro FXMLAddBrand.fxml");
    }
    
    /* * Loads a new add window * */
    private void loadNewAddWindow(Class controller, String fileName, String title, String message)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLAddBrandController addController = (FXMLAddBrandController) loader.getController();
            addController.initializeOnControllerCall(this, brandObservableList);
            
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
    private void loadNewEditWindow(Class controller, String fileName, String title, String message, BrandBLL brand)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLEditBrandController editController = (FXMLEditBrandController) loader.getController();
            editController.initializeOnControllerCall(this, brandObservableList, brand);
            
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
    
    /* * Searches for brands when a key is pressed * */
    @FXML
    void getSearchList()
    {
        List<BrandBLL> brandList = new ArrayList<>();
            
        /* If something has been typed, tries to find an existent brand with the given name or ID */
        if(searchBrandTextField.getText().length() > 0)
        {
            brandList.clear();
            
            String nonCharacters = "[^\\p{L}\\p{Nd}]";
            
            for(BrandBLL brand : brandObservableList)
            {
                String searchString = StringUtils.stripAccents(searchBrandTextField.getText().replaceAll(nonCharacters, "").toLowerCase());
                
                String brandName = StringUtils.stripAccents(brand.getNome().replaceAll(nonCharacters, "").toLowerCase());
                String brandID = String.valueOf(brand.getIdmarca());
                
                if(brandName.contains(searchString) || brandID.contains(searchString))
                {
                    brandList.add(brand);
                }
            }
            
            setSearchedTableValues(brandList);
        }
        else /* If nothing has been typed, show full category list */
        {
            brandList.clear();
            
            brandList = brandObservableList;
            setSearchedTableValues(brandList);
        }
    }
    
    /* * Sets new table values * */
    public void setSearchedTableValues(List<BrandBLL> brandList)
    {
        ObservableList<BrandBLL> newBrandObservableList;
        newBrandObservableList = FXCollections.observableArrayList(brandList);
        setTableItems(newBrandObservableList);
    }
}
