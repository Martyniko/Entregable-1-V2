/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.Curso;
import testlibrary.TestLibrary;

/**
 * FXML Controller class
 *
 * @author impre
 */
public class FXMLCursosListController implements Initializable {
    private Stage primaryStage;
    private TestLibrary mainApp;
    private Curso curso;
   
    @FXML private Button Modificar;
    @FXML private Button Borrar;
    @FXML private Button Añadir;
    @FXML private Button Matriculados;
    @FXML private TableView<Curso> cursosList;
    @FXML private TableColumn<Curso, String> aulaColumn;
    @FXML private TableColumn<Curso, String> tituloColumn;
    @FXML private TableColumn<Curso, Integer> maxalumnosColumn;
    @FXML private TableColumn<Curso, String> profesorColumn;
    @FXML private TableColumn<Curso, LocalDate> finiColumn;
    @FXML private TableColumn<Curso, LocalDate> ffinColumn;
    @FXML private TableColumn<Curso, LocalTime> horaColumn;

    @FXML public void initialize(URL url, ResourceBundle rb) {
        tituloColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitulodelcurso()));
        profesorColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProfesorAsignado()));
        //edadColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEdad()));
        maxalumnosColumn.setCellValueFactory(new PropertyValueFactory<>("numeroMaximodeAlumnos"));
        horaColumn.setCellValueFactory(new PropertyValueFactory<>("hora"));
        aulaColumn.setCellValueFactory(new PropertyValueFactory<>("aula"));
        finiColumn.setCellValueFactory(new PropertyValueFactory<>("fechainicio"));
        finiColumn.setCellFactory((TableColumn<Curso, LocalDate> column) -> {
            return new TableCell<Curso, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                  super.updateItem(item, empty);
                  if (item == null || empty) setText(null);
                  else setText(item.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                }
            };
         });
        ffinColumn.setCellValueFactory(new PropertyValueFactory<>("fechafin"));
        ffinColumn.setCellFactory((TableColumn<Curso, LocalDate> column) -> {
            return new TableCell<Curso, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                  super.updateItem(item, empty);
                  if (item == null || empty) setText(null);
                  else setText(item.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                }
            };
         });
        //horaColumn.setCellValueFactory(new PropertyValueFactory<Curso, LocalTime>("hora"));
                  
        BooleanBinding noCursoSelected = Bindings.isEmpty(cursosList.getSelectionModel().getSelectedItems());
        this.Borrar.disableProperty().bind(noCursoSelected);
        this.Modificar.disableProperty().bind(noCursoSelected);
        this.Matriculados.disableProperty().bind(noCursoSelected);
        cursosList.getSelectionModel().selectedItemProperty().addListener((o, oldval, newval) -> {curso = newval;});
    }    
    
    @FXML private void añadir(ActionEvent event) throws ParseException {
        Curso newItem = new Curso();
        boolean okAccion = mainApp.showVentanaCurso(newItem,"Añadir");
        if (okAccion) {
                cursosList.getItems().add(newItem);
                cursosList.getSelectionModel().selectLast();
        }
    }
    
    @FXML private void matriculas(ActionEvent event) {
        mainApp.showMatriculas(this.curso);
        
    }

    @FXML private void modificar(ActionEvent event) throws ParseException {
        boolean okAccion = mainApp.showVentanaCurso(curso,"Modificar");
        if (okAccion) cursosList.refresh();
    }

    @FXML private void borrar(ActionEvent event) throws ParseException {
         boolean okAccion = mainApp.showVentanaCurso(curso,"Borrar");
         if (okAccion) cursosList.getItems().remove(curso);
    }
        
    public void setMain(TestLibrary mainApp) {
        this.mainApp = mainApp;
    }
    
    public void initStage(Stage stage, ObservableList<Curso> lc) {
        primaryStage = stage;
        cursosList.setItems(lc);
    }
}
