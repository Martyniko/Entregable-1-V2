/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.Curso;
import modelo.Matricula;
import testlibrary.TestLibrary;

/**
 * FXML Controller class
 *
 * @author  Martin Romero
 */
public class FXMLMatriculasListController implements Initializable {
    private Stage primaryStage;
    private TestLibrary mainApp;
    private Curso curso;
    private Matricula matricula;
    
    @FXML private Button Añadir;
    @FXML private Button Modificar;
    @FXML private Button Borrar;
    @FXML private Label tituloCurso;
    @FXML private TableView<Matricula> matriculasList;
    @FXML private TableColumn<Matricula, String> alumnoColumn;
    @FXML private TableColumn<Matricula, LocalDate> fechaColumn;
    /**
     * Initializes the controller class.
     */
    @FXML@Override
    public void initialize(URL url, ResourceBundle rb) {
        alumnoColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAlumno().getNombre()));
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        fechaColumn.setCellFactory((TableColumn<Matricula, LocalDate> column) -> {
            return new TableCell<Matricula, LocalDate>() {
               @Override
               protected void updateItem(LocalDate item, boolean empty) {
                  super.updateItem(item, empty);
                  if (item == null || empty) {setText(null);} 
                  else {setText(item.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));}
               }
            };
         });
        
        BooleanBinding noMatriculaSelected = Bindings.isEmpty(matriculasList.getSelectionModel().getSelectedItems());
        this.Borrar.disableProperty().bind(noMatriculaSelected);
        this.Modificar.disableProperty().bind(noMatriculaSelected);
        matriculasList.getSelectionModel().selectedItemProperty().addListener((o, oldval, newval) -> {matricula = newval;});
    }    
    
    @FXML private void añadir(ActionEvent event) throws ParseException {
        Matricula newItem = new Matricula();
        boolean okAccion = mainApp.loadVentanaMatricula(curso,newItem,"Añadir");
        if (okAccion) {
            if(!TestLibrary.isAlumnoEnCurso(newItem.getAlumno())){
                TestLibrary.matriculasObsListTodas.add(newItem);
                TestLibrary.matriculasObsList.add(newItem);
                matriculasList.getSelectionModel().selectLast();
                this.Añadir.setDisable(TestLibrary.isCursoCompleto(curso));
            }
        }
    }
    
    @FXML private void modificar(ActionEvent event) throws ParseException {
        boolean okAccion = mainApp.loadVentanaMatricula(curso,matricula,"Modificar");
        if (okAccion) matriculasList.refresh();
    }

    @FXML private void borrar(ActionEvent event) throws ParseException {
        boolean okAccion = mainApp.loadVentanaMatricula(curso,matricula,"Borrar");
        if (okAccion) {
            TestLibrary.matriculasObsListTodas.remove(matricula);
            TestLibrary.matriculasObsList.remove(matricula);
            this.Añadir.setDisable(TestLibrary.isCursoCompleto(curso));
        }
    }
        
    public void setMain(TestLibrary mainApp) {this.mainApp = mainApp;}
    
    public void initStage(Stage stage,Curso curso, ObservableList<Matricula> lm) {
        this.curso=curso;
        this.tituloCurso.setText(this.curso.getTitulodelcurso());
        this.primaryStage = stage;
        matriculasList.setItems(lm);
        this.Añadir.setDisable(TestLibrary.isCursoCompleto(curso));
    }
}
