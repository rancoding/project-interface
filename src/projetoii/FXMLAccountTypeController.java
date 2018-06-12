package projetoii;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FXMLAccountTypeController implements Initializable {

    @FXML private Button userButton;
    @FXML private Button adminButton;
    @FXML private Pane userPane;
    @FXML private Pane adminPane;
    @FXML private Label userButtonLabel;
    @FXML private Label adminButtonLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        userPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event->{
            onButtonAction(event);
        });
        
        adminPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event->{
            onButtonAction(event);
        });
    }
    
    @FXML private void onButtonAction(MouseEvent event)
    {
        switch(((Pane) event.getSource()).getId())
        {
            case "adminPane":
            {
                openWindow("design/administrator/menu/top/FXMLAdministratorTopMenu.fxml", "Administrador");
                break;
            }
            
            case "userPane":
            {
                openWindow("design/user/work/login/FXMLLogin.fxml", "Funcionário");
                break;
            }
        }
    }
    
    private void openWindow(String file, String title)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException ex)
        {
            System.out.println("Não foi possível abrir o ficheiro " + file);
        }
    }
    
    @FXML private void setUserButtonVisibleOnHover()
    {
        userButton.setVisible(true);
        userButtonLabel.setVisible(false);
    }
    
    @FXML private void setAdminButtonVisibleOnHover()
    {
        adminButton.setVisible(true);
        adminButtonLabel.setVisible(false);
    }
    
    @FXML private void setUserButtonInvisibleOnHover()
    {
        userButton.setVisible(false);
        userButtonLabel.setVisible(true);
    }
    
    @FXML private void setAdminButtonInvisibleOnHover()
    {
        adminButton.setVisible(false);
        adminButtonLabel.setVisible(true);
    }
    
}
