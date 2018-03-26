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
import javafx.collections.FXCollections;
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
import modelo.Alumno;
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
    private ObservableList<Matricula> matriculasObsList;
    
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
            if(isAlumnoEnCurso(newItem.getAlumno()))
              mainApp.loadAviso("Matricular Alumno","No se puede matricular el alumno "+newItem.getAlumno().getNombre(),"Este alumno ya está matriculado en este curso");
            else {
                TestLibrary.matriculasObsListTodas.add(newItem);
                matriculasList.getItems().add(newItem);
                matriculasList.getSelectionModel().selectLast();
                this.Añadir.setDisable(isCursoCompleto(curso));
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
            matriculasList.getItems().remove(matricula);
            this.Añadir.setDisable(isCursoCompleto(curso));
        }
    }

    private Boolean isAlumnoEnCurso(Alumno alumno) {
        Boolean isOk=false;
        for (Matricula mm: matriculasObsList) {
            if(alumno.getDni().equals(mm.getAlumno().getDni())) isOk=true;
        }
        return isOk;
    }
    
    private Boolean isCursoCompleto(Curso mcurso) {
        if(matriculasObsList.isEmpty()) return false;
        else return (mcurso.getNumeroMaximodeAlumnos() == matriculasObsList.size());
    }    
    
    public void setMain(TestLibrary mainApp) {this.mainApp = mainApp;}
    
    public void initStage(Stage stage,Curso curso) {
        this.curso=curso;
        this.tituloCurso.setText(this.curso.getTitulodelcurso());
        this.primaryStage = stage;
        matriculasObsList= FXCollections.observableList(TestLibrary.acceso.getMatriculasDeCurso(curso));
        matriculasList.setItems(matriculasObsList);
        this.Añadir.setDisable(isCursoCompleto(curso));
    }
}
