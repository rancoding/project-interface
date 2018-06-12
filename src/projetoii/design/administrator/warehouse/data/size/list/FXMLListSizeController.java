/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.size.list;

import dao.Tamanho;
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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import projetoii.design.administrator.warehouse.data.size.add.FXMLAddSizeController;
import projetoii.design.administrator.warehouse.data.size.edit.FXMLEditSizeController;

public class FXMLListSizeController implements Initializable {

    /* Variables used for setting up the table content */
    @FXML public TableView<Tamanho> sizeTable;
    @FXML private TableColumn<Tamanho, Byte> idColumn;
    @FXML private TableColumn<Tamanho, String> nameColumn;
    @FXML private TableColumn<Tamanho, String> editColumn;
    private ObservableList<Tamanho> sizeObservableList;
    
    /* Text field used to search sizes on the table, updating as it searches */
    @FXML private TextField searchSizeTextField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        /* Initializes and opens the database session using hibernate */
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        /* Retrieves all database sizes to an arraylist and initializes the table values if it is not empty */
        List<Tamanho> sizeList = session.createCriteria(Tamanho.class).list();
        
        if(!(sizeList.isEmpty()))
        {
            initializeTable(sizeList);
        }
        else
        {
            sizeList = new ArrayList<Tamanho>();
            initializeTable(sizeList);
        }
        
        /* Closes the database connection */
        session.close();
    }   
    
    
    /** Initializes all table content for the first time **/
    private void initializeTable(List<Tamanho> sizeList)
    {
        /* Sets column variables to use entity info, empty for a button creation */
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("idtamanho"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.editColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        
        /* Sets images for all row buttons and sets the buttons up */
        Image image = new Image(getClass().getResourceAsStream("image/edit.png"));
        Image imageHover = new Image(getClass().getResourceAsStream("image/edit_hover.png"));
        editColumn.setCellFactory(getButtonCell(image, imageHover));
        
        /* Sets the table content */
        sizeObservableList = FXCollections.observableArrayList(sizeList);
        setTableItems(sizeObservableList);
    }
    
    /* * Sets the table items to be the same as the observable list items * */
    private void setTableItems(ObservableList<Tamanho> sizeObservableList)
    {
        this.sizeTable.setItems(sizeObservableList);
    }
    
    /* Creates a button for each table cell, also setting up an image for each button (with a different hover image and size) */
    private Callback getButtonCell(Image image, Image imageHover)
    {
        Callback<TableColumn<Tamanho, String>, TableCell<Tamanho, String>> cellFactory;
        cellFactory = new Callback<TableColumn<Tamanho, String>, TableCell<Tamanho, String>>()
        {
            @Override
            public TableCell call(final TableColumn<Tamanho, String> param)
            {
                final TableCell<Tamanho, String> cell = new TableCell<Tamanho, String>()
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
                            /* On edit button, opens an edit size window with the row size info and the list of existent size */
                            button.setOnAction((event) -> {
                                Tamanho size = getTableView().getItems().get(getIndex());
                                loadNewEditWindow(FXMLEditSizeController.class, "FXMLEditSize.fxml", "Armazém - Editar Tamanho", "Não foi possível carregar o ficheiro FXMLEditSize.fxml", size);
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
        loadNewAddWindow(FXMLAddSizeController.class, "FXMLAddSize.fxml", "Armazém - Adicionar Tamanho", "Não foi possível carregar o ficheiro FXMLAddSize.fxml");
    }
    
    /* * Loads a new add window * */
    private void loadNewAddWindow(Class controller, String fileName, String title, String message)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLAddSizeController addController = (FXMLAddSizeController) loader.getController();
            addController.initializeOnControllerCall(this, sizeObservableList);
            
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
    private void loadNewEditWindow(Class controller, String fileName, String title, String message, Tamanho size)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLEditSizeController editController = (FXMLEditSizeController) loader.getController();
            editController.initializeOnControllerCall(this, sizeObservableList, size);
            
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
    
    /* * Searches for sizes when a key is pressed * */
    @FXML
    void getSearchList()
    {
        List<Tamanho> sizeList = new ArrayList<>();
            
        /* If something has been typed, tries to find an existent size with the given name or ID */
        if(searchSizeTextField.getText().length() > 0)
        {
            sizeList.clear();
            
            String nonCharacters = "[^\\p{L}\\p{Nd}]";
            
            for(Tamanho size : sizeObservableList)
            {
                String searchString = StringUtils.stripAccents(searchSizeTextField.getText().replaceAll(nonCharacters, "").toLowerCase());
                
                String sizeName = StringUtils.stripAccents(size.getDescricao().replaceAll(nonCharacters, "").toLowerCase());
                String sizeID = String.valueOf(size.getIdtamanho());
                
                if(sizeName.contains(searchString) || sizeID.contains(searchString))
                {
                    sizeList.add(size);
                }
            }
            
            setSearchedTableValues(sizeList);
        }
        else /* If nothing has been typed, show full category list */
        {
            sizeList.clear();
            
            sizeList = sizeObservableList;
            setSearchedTableValues(sizeList);
        }
    }
    
    /* * Sets new table values * */
    public void setSearchedTableValues(List<Tamanho> sizeList)
    {
        ObservableList<Tamanho> newSizeObservableList;
        newSizeObservableList = FXCollections.observableArrayList(sizeList);
        setTableItems(newSizeObservableList);
    }
}
