/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @FXML private AnchorPane AnchorPane;
    @FXML private MenuBar menuBar;
    @FXML private MenuItem mAlumnos;
    @FXML private MenuItem mCursos;
    @FXML private Menu menuOpciones;
    @FXML private Label estado;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    

    @FXML private void Salir(ActionEvent event) {TestLibrary.salvar(); System.exit(0);}
    
    @FXML private void irAAlumnos(ActionEvent event) {mainApp.loadAlumnos();}

    @FXML private void irACursos(ActionEvent event) {mainApp.loadCursos();}
    
    public void initStage(Stage stage) {primaryStage = stage;}
            
    public void setMain(TestLibrary mainApp) {this.mainApp = mainApp;}
}
