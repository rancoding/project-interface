package projetoii.design.administrator.warehouse.employee.list;

import bll.EmployeeBLL;
import dao.Funcionario;
import hibernate.HibernateUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
import projetoii.design.administrator.menu.top.FXMLAdministratorTopMenuController;
import projetoii.design.administrator.warehouse.employee.add.FXMLAddEmployeeController;
import projetoii.design.administrator.warehouse.employee.edit.FXMLEditEmployeeController;
import projetoii.design.administrator.warehouse.employee.list.detail.FXMLEmployeeDetailController;
import projetoii.design.administrator.warehouse.employee.schedule.list.FXMLEmployeeSchedulePointController;
import services.EmployeeService;

public class FXMLListEmployeeController implements Initializable {

    /* Variables used for setting up the table content */
    @FXML public TableView<EmployeeBLL> employeeTable;
    @FXML private TableColumn<EmployeeBLL, String> nameColumn;
    @FXML private TableColumn<EmployeeBLL, Date> birthdayColumn;
    @FXML private TableColumn<EmployeeBLL, String> genderColumn;
    @FXML private TableColumn<EmployeeBLL, String> workingColumn;
    @FXML private TableColumn<EmployeeBLL, String> editColumn;
    @FXML private TableColumn<EmployeeBLL, String> scheduleColumn;
    @FXML private TableColumn<EmployeeBLL, String> detailColumn;
    private ObservableList<EmployeeBLL> employeeObservableList;
    
    /* Text field used to search employees on the table, updating as it searches */
    @FXML private TextField searchEmployeeTextField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        List<EmployeeBLL> employeeList = EmployeeService.getWorkLocationEmployeeList(FXMLAdministratorTopMenuController.getWorkLocationId());
        
        if(!(employeeList.isEmpty()))
        {
            /*for(Funcionario e : employeeList)
            {
                Hibernate.initialize(e.getHorario());
            }*/
            
            initializeTable(employeeList);
        }
        else
        {
            employeeList = new ArrayList<>();
            initializeTable(employeeList);
        }
    }
    
    /** Initializes all table content for the first time **/
    private void initializeTable(List<EmployeeBLL> employeeList)
    {
        /* Sets column variables to use entity info, empty for a button creation */
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("datanascimento"));
        this.genderColumn.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        this.workingColumn.setCellValueFactory(new PropertyValueFactory<>("activo"));
        this.editColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        this.scheduleColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        this.detailColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        
        /* Sets images for all row buttons and sets the buttons up */
        Image image = new Image(getClass().getResourceAsStream("image/edit.png"));
        Image imageHover = new Image(getClass().getResourceAsStream("image/edit_hover.png"));
        editColumn.setCellFactory(getButtonCell(FXMLEditEmployeeController.class, "FXMLEditEmployee.fxml", "Armazém - Editar Funcionário", "Não foi possível carregar o ficheiro FXMLEditEmployee.fxml", image, imageHover));
        
        image = new Image(getClass().getResourceAsStream("image/schedule.png"));
        imageHover = new Image(getClass().getResourceAsStream("image/schedule_hover.png"));
        scheduleColumn.setCellFactory(getButtonCell(FXMLEmployeeSchedulePointController.class, "FXMLEmployeeSchedulePoint.fxml", "Armazém - Horário Funcionário", "Não foi possível carregar o ficheiro FXMLEmployeeSchedulePoint.fxml", image, imageHover));
        
        image = new Image(getClass().getResourceAsStream("image/detail.png"));
        imageHover = new Image(getClass().getResourceAsStream("image/detail_hover.png"));
        detailColumn.setCellFactory(getButtonCell(FXMLEmployeeDetailController.class, "FXMLEmployeeDetail.fxml", "Armazém - Detalhes Funcionário", "Não foi possível carregar o ficheiro FXMLEmployeeDetail.fxml", image, imageHover));
        
        /* Sets the table content */
        employeeObservableList = FXCollections.observableArrayList(employeeList);
        setTableItems(employeeObservableList);
    }
    
    /* * Sets the table items to be the same as the observable list items * */
    private void setTableItems(ObservableList<EmployeeBLL> employeeObservableList)
    {
        this.employeeTable.setItems(employeeObservableList);
    }
    
    /* Creates a button for each table cell, also setting up an image for each button (with a different hover image and size) */
    private Callback getButtonCell(Class controller, String file, String title, String message, Image image, Image imageHover)
    {
        Callback<TableColumn<EmployeeBLL, String>, TableCell<EmployeeBLL, String>> cellFactory;
        cellFactory = new Callback<TableColumn<EmployeeBLL, String>, TableCell<EmployeeBLL, String>>()
        {
            @Override
            public TableCell call(final TableColumn<EmployeeBLL, String> param)
            {
                final TableCell<EmployeeBLL, String> cell = new TableCell<EmployeeBLL, String>()
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
                            /* Sets an action depending on the passed controller */
                            button.setOnAction((event) -> {
                                EmployeeBLL employee = getTableView().getItems().get(getIndex());
                                
                                if(controller.equals(FXMLEditEmployeeController.class))
                                {
                                    loadNewEditWindow(controller, file, title, message, employee);
                                }
                                else if(controller.equals(FXMLEmployeeSchedulePointController.class))
                                {
                                    loadNewScheduleWindow(controller, file, title, message, employee);
                                }
                                else if(controller.equals(FXMLEmployeeDetailController.class))
                                {
                                    loadNewDetailWindow(controller, file, title, message, employee);
                                }
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
        loadNewAddWindow(FXMLAddEmployeeController.class, "FXMLAddEmployee.fxml", "Armazém - Adicionar Funcionário", "Não foi possível carregar o ficheiro FXMLAddEmployee.fxml");
    }
    
    /* * Loads a new add window * */
    private void loadNewAddWindow(Class controller, String fileName, String title, String message)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLAddEmployeeController addController = (FXMLAddEmployeeController) loader.getController();
            //addController.initializeOnControllerCall(this, employeeObservableList);
            
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
    private void loadNewEditWindow(Class controller, String fileName, String title, String message, EmployeeBLL employee)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLEditEmployeeController editController = (FXMLEditEmployeeController) loader.getController();
            editController.initializeOnControllerCall(this, employeeObservableList, employee);
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //System.out.println(message);
        }
    }
    
    /* * Loads a new schedule window * */
    private void loadNewScheduleWindow(Class controller, String fileName, String title, String message, Funcionario employee)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLEmployeeSchedulePointController scheduleController = (FXMLEmployeeSchedulePointController) loader.getController();
            //scheduleController.initializeOnControllerCall(this, employeeObservableList, type);
            
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
    private void loadNewDetailWindow(Class controller, String fileName, String title, String message, Funcionario employee)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLEmployeeDetailController detailController = (FXMLEmployeeDetailController) loader.getController();
            detailController.initializeOnControllerCall(employee);
            
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
    
    /* * Searches for employees when a key is pressed * */
    @FXML
    void getSearchList()
    {
        List<EmployeeBLL> employeeList = new ArrayList<>();
            
        /* If something has been typed, tries to find an existent employee with the given name or ID */
        if(searchEmployeeTextField.getText().length() > 0)
        {
            employeeList.clear();
            
            String nonCharacters = "[^\\p{L}\\p{Nd}]";
            
            for(EmployeeBLL employee : employeeObservableList)
            {
                String searchString = StringUtils.stripAccents(searchEmployeeTextField.getText().replaceAll(nonCharacters, "").toLowerCase());
                
                String employeeName = StringUtils.stripAccents(employee.getNome().replaceAll(nonCharacters, "").toLowerCase());
                String employeeID = String.valueOf(employee.getIdfuncionario());
                
                if(employeeName.contains(searchString) || employeeID.contains(searchString))
                {
                    employeeList.add(employee);
                }
            }
            
            setSearchedTableValues(employeeList);
        }
        else /* If nothing has been typed, show full employee list */
        {
            employeeList.clear();
            
            employeeList = employeeObservableList;
            setSearchedTableValues(employeeList);
        }
    }
    
    /* * Sets new table values * */
    public void setSearchedTableValues(List<EmployeeBLL> employeeList)
    {
        ObservableList<EmployeeBLL> newEmployeeObservableList;
        newEmployeeObservableList = FXCollections.observableArrayList(employeeList);
        setTableItems(newEmployeeObservableList);
    }  
    
}
