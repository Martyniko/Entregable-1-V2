/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import testlibrary.TestLibrary;
/**
 *
 * @author impre
 */
public class FXMLMenuController implements Initializable {
    
    private Stage primaryStage;
    private TestLibrary mainApp;

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem mAlumnos;
    @FXML
    private MenuItem mCursos;
    @FXML
    private Menu menuOpciones;
    @FXML
    private Label estado;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void Salir(ActionEvent event) {
        mainApp.salvar();
        System.exit(0);
    }

    
    @FXML
    private void irAAlumnos(ActionEvent event) {
        estado.setText("Lista de Alumnos ");
        mainApp.showAlumnos();
    }

    @FXML
    private void irACursos(ActionEvent event) {
        estado.setText("Lista de Cursos ");
        mainApp.showCursos();
        
    }
    
    private void compraOk(String tienda){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Compra realizada correctamente");
        alert.setContentText("Has comprado en "+tienda);
        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        Optional<ButtonType> result = alert.showAndWait();
    }
    
    private void compraError(String tienda){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Error en la selección");
        alert.setHeaderText("No puede comprar en "+tienda);
        alert.setContentText("Por favor, cambie la selección en el menú Opciones");
        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        Optional<ButtonType> result = alert.showAndWait();
    }
    
    @FXML
    private void Blogger(ActionEvent event) {
        ObservableList<String> choices = FXCollections.observableArrayList();
        choices.addAll("el blog de Porthos","el blog de Athos","el blog de Aramis");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
        dialog.setTitle("Selecciona un blog");
        dialog.setHeaderText("Que blog quieres visitar?");
        dialog.setContentText("Elige:");
        Optional<String> result = dialog.showAndWait();
        // Obteniendo el resultado con una lambda
        result.ifPresent(number-> estado.setText("Visitando " + result.get()));
    }

    public void initStage(Stage stage) {
     primaryStage = stage;   
    }
            
    public void setMain(TestLibrary mainApp) {
        this.mainApp = mainApp;
    }
    
}
