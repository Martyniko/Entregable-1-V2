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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Matricula;
import testlibrary.TestLibrary;

/**
 * FXML Controller class
 *
 * @author  Martin Romero
 */
public class FXMLAlumnosListController implements Initializable {
    private Stage primaryStage;
    private TestLibrary mainApp;
    private Alumno alumno;
    @FXML private Button Añadir;
    @FXML private Button Modificar;
    @FXML private Button Borrar;
    @FXML private TableView<Alumno> alumnosList;
    @FXML private TableColumn<Alumno, String> dniColumn;
    @FXML private TableColumn<Alumno, Integer> edadColumn;
    @FXML private TableColumn<Alumno, String> nombreColumn;
    @FXML private TableColumn<Alumno, String> direccionColumn;
    @FXML private TableColumn<Alumno, LocalDate> altaColumn;
    @FXML private TableColumn<Alumno, Image> fotoColumn;
    /**
     * Initializes the controller class.
     */
    @FXML@Override
    public void initialize(URL url, ResourceBundle rb) {
        dniColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDni()));
        nombreColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        direccionColumn.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getDireccion()));
        altaColumn.setCellValueFactory(new PropertyValueFactory<>("fechadealta"));
        altaColumn.setCellFactory((TableColumn<Alumno, LocalDate> column) -> {
            return new TableCell<Alumno, LocalDate>() {
               @Override
               protected void updateItem(LocalDate item, boolean empty) {
                  super.updateItem(item, empty);
                  if (item == null || empty) {
                     setText(null);
                  } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                  }
               }
            };
         });
        fotoColumn.setCellValueFactory(new PropertyValueFactory<>("foto"));
        fotoColumn.setCellFactory(columna -> {
            return new TableCell<Alumno, Image> () {
                @Override
                protected void updateItem(Image item, boolean empty) {
                    setText(null);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    ImageView imageView = new ImageView(item);
                    imageView.setFitWidth(40);imageView.setFitHeight(40);
                    setGraphic(imageView);    
                }
            };
        });        
        BooleanBinding noAlumnoSelected = Bindings.isEmpty(alumnosList.getSelectionModel().getSelectedItems());
        this.Borrar.disableProperty().bind(noAlumnoSelected);
        this.Modificar.disableProperty().bind(noAlumnoSelected);
        alumnosList.getSelectionModel().selectedItemProperty().addListener((o, oldval, newval) -> {alumno = newval;});
    }    
    
    @FXML private void añadir(ActionEvent event) throws ParseException {
        Alumno newItem = new Alumno();
        boolean okAccion = mainApp.loadVentanaAlumno(newItem,"Añadir");
        if (okAccion) {
                alumnosList.getItems().add(newItem);
                alumnosList.getSelectionModel().selectLast();
        }
    }
    
    @FXML private void modificar(ActionEvent event) throws ParseException {
        Alumno newAlumno=new Alumno();
        changeAlumno(newAlumno, alumno);
        boolean okAccion = mainApp.loadVentanaAlumno(newAlumno,"Modificar");
        if (okAccion) {
            TestLibrary.matriculasObsListTodas.stream().filter((matricula) -> (alumno.equals(matricula.getAlumno()))).forEachOrdered((matricula) -> {
                changeAlumno(matricula.getAlumno(),newAlumno);
            });
            changeAlumno(alumno,newAlumno);
            alumnosList.refresh();
        }
    }
    
    private void changeAlumno(Alumno chgAlumno,Alumno newAlumno){
        chgAlumno.setDni(newAlumno.getDni());
        chgAlumno.setNombre(newAlumno.getNombre());
        chgAlumno.setEdad(newAlumno.getEdad());
        chgAlumno.setDireccion(newAlumno.getDireccion());
        chgAlumno.setFechadealta(newAlumno.getFechadealta());
        chgAlumno.setFoto(newAlumno.getFoto());
    }
    
    @FXML private void borrar(ActionEvent event) throws ParseException {
        if (TestLibrary.AlumnoMatriculado(alumno))
           mainApp.loadAviso("Borrar Alumno","No se puede borrar el alumno "+alumno.getNombre(),"Para borrar el alumno debe primero borrarlo de los cursos en los que estuviera matriculado");
        else {
            boolean okAccion = mainApp.loadVentanaAlumno(alumno,"Borrar");
            if (okAccion) alumnosList.getItems().remove(alumno);
        } 
    }
        
    public void setMain(TestLibrary mainApp) {this.mainApp = mainApp;}
    
    public void initStage(Stage stage, ObservableList<Alumno> la) {
        primaryStage = stage;
        alumnosList.setItems(la);
    }
}
