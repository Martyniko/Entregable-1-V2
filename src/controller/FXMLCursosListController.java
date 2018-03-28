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
import javafx.collections.FXCollections;
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
import modelo.Alumno;
import modelo.Curso;
import modelo.Matricula;
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
    
    private Matricula matricula;
    private ObservableList<Matricula> matriculasObsList;
    BooleanBinding noCursoSelected;
    @FXML private Button mAñadir;
    @FXML private Button mModificar;
    @FXML private Button mBorrar;
    @FXML private TableView<Matricula> matriculasList;
    @FXML private TableColumn<Matricula, String> alumnoColumn;
    @FXML private TableColumn<Matricula, LocalDate> fechaColumn;
    @FXML private Button Modificar;
    @FXML private Button Borrar;
    @FXML private Button Añadir;
    @FXML private TableView<Curso> cursosList;
    @FXML private TableColumn<Curso, String> aulaColumn;
    @FXML private TableColumn<Curso, String> diasColumn;
    @FXML private TableColumn<Curso, String> tituloColumn;
    @FXML private TableColumn<Curso, Integer> maxalumnosColumn;
    @FXML private TableColumn<Curso, String> profesorColumn;
    @FXML private TableColumn<Curso, LocalDate> finiColumn;
    @FXML private TableColumn<Curso, LocalDate> ffinColumn;
    @FXML private TableColumn<Curso, LocalTime> horaColumn;

    @FXML@Override
    public void initialize(URL url, ResourceBundle rb) {
        tituloColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitulodelcurso()));
        diasColumn.setCellValueFactory(c -> new SimpleStringProperty(TestLibrary.diasToString(c.getValue().getDiasimparte())));
        profesorColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProfesorAsignado()));
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
                  
        noCursoSelected = Bindings.isEmpty(cursosList.getSelectionModel().getSelectedItems());
        BooleanBinding cursoCompleto;
        
        this.Borrar.disableProperty().bind(noCursoSelected);
        this.Modificar.disableProperty().bind(noCursoSelected);
        cursosList.getSelectionModel().selectedItemProperty().addListener((o, oldval, newval) -> {curso = newval;verMatriculas();});
        this.mBorrar.disableProperty().bind(noCursoSelected);
        this.mModificar.disableProperty().bind(noCursoSelected);
//        //this.mAñadir.disableProperty().bind(noCursoSelected);
    }    
    
    @FXML private void añadir(ActionEvent event) throws ParseException {
        Curso newItem = new Curso();
        boolean okAccion = mainApp.loadVentanaCurso(newItem,"Añadir");
        if (okAccion) {
                cursosList.getItems().add(newItem);
                cursosList.getSelectionModel().selectLast();
        }
    }
    
    @FXML private void modificar(ActionEvent event) throws ParseException {
        Curso newCurso=new Curso();
        changeCurso(newCurso,curso);
        boolean okAccion = mainApp.loadVentanaCurso(newCurso,"Modificar");
        if (okAccion) {
            TestLibrary.matriculasObsListTodas.stream().filter((matricula) -> (curso.equals(matricula.getCurso()))).forEachOrdered((matricula) -> {
                changeCurso(matricula.getCurso(),newCurso);
            });
            changeCurso(curso,newCurso);
            cursosList.refresh();
        }
    }
    
    private void changeCurso(Curso chgCurso,Curso newCurso){
        chgCurso.setTitulodelcurso(newCurso.getTitulodelcurso());
        chgCurso.setProfesorAsignado(newCurso.getProfesorAsignado());
        chgCurso.setNumeroMaximodeAlumnos(newCurso.getNumeroMaximodeAlumnos());
        chgCurso.setFechainicio(newCurso.getFechainicio());
        chgCurso.setFechafin(newCurso.getFechafin());
        chgCurso.setHora(newCurso.getHora());
        chgCurso.setDiasimparte(newCurso.getDiasimparte());
        chgCurso.setAula(newCurso.getAula());
    }
    
    @FXML private void borrar(ActionEvent event) throws ParseException {
        if (tieneAlumnosMatriculados(curso)) 
            mainApp.loadAviso("Borrar Curso","No se puede borrar el curso: "+curso.getTitulodelcurso(),"Para borrar el curso debe primero borrar los alumnos que estuvieran matriculados");
        else {
            boolean okAccion = mainApp.loadVentanaCurso(curso,"Borrar");
            if (okAccion) cursosList.getItems().remove(curso);
        }
    }
    
    @FXML private void mañadir(ActionEvent event) throws ParseException {
        if (isCursoCompleto(curso)) 
            mainApp.loadAviso("Curso Completo","No se puede matricular alumnos. El curso "+curso.getTitulodelcurso()," está completo");
        else {    
            Matricula newItem = new Matricula();
            boolean okAccion = mainApp.loadVentanaMatricula(curso,newItem,"Añadir");
            if (okAccion) {
                if(isAlumnoEnCurso(newItem.getAlumno()))
                  mainApp.loadAviso("Matricular Alumno","No se puede matricular el alumno "+newItem.getAlumno().getNombre(),"Este alumno ya está matriculado en este curso");
                else {
                    TestLibrary.matriculasObsListTodas.add(newItem);
                    matriculasList.getItems().add(newItem);
                    matriculasList.getSelectionModel().selectLast();
                    this.mAñadir.setDisable(isCursoCompleto(curso));
                }
            }
        }
    }
    
    @FXML private void mmodificar(ActionEvent event) throws ParseException {
        boolean okAccion = mainApp.loadVentanaMatricula(curso,matricula,"Modificar");
        if (okAccion) matriculasList.refresh();
    }

    @FXML private void mborrar(ActionEvent event) throws ParseException {
        boolean okAccion = mainApp.loadVentanaMatricula(curso,matricula,"Borrar");
        if (okAccion) {
            TestLibrary.matriculasObsListTodas.remove(matricula);
            matriculasList.getItems().remove(matricula);
            this.Añadir.setDisable(isCursoCompleto(curso));
        }
    }
            
    public Boolean tieneAlumnosMatriculados(Curso curso) {
        Boolean isOk = false;
        if (curso!=null)
            for (Matricula mm: TestLibrary.matriculasObsListTodas) {
                if(curso.equals(mm.getCurso())) isOk=true;
            }
        return isOk;
    }
    
    private Boolean isAlumnoEnCurso(Alumno alumno) {
        Boolean isOk=false;
        for (Matricula mm: matriculasObsList) {
            if(alumno.getDni().equals(mm.getAlumno().getDni())) isOk=true;
//            if(alumno.equals(mm.getAlumno())) isOk=true;
        }
        return isOk;
    }
    
    private Boolean isCursoCompleto(Curso mcurso) {
        if(matriculasObsList.isEmpty()) return false;
        else return (mcurso.getNumeroMaximodeAlumnos() == matriculasObsList.size());
    }    
    
    public void verMatriculas() {
        
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
        this.mBorrar.disableProperty().bind(noCursoSelected.or(noMatriculaSelected));
        this.mModificar.disableProperty().bind(noCursoSelected.or(noMatriculaSelected));
        matriculasList.getSelectionModel().selectedItemProperty().addListener((o, oldval, newval) -> {matricula = newval;});
        matriculasObsList= FXCollections.observableList(TestLibrary.acceso.getMatriculasDeCurso(curso));
        matriculasList.setItems(matriculasObsList);
        this.mAñadir.setDisable(isCursoCompleto(curso));
    }
    
    public void setMain(TestLibrary mainApp) {this.mainApp = mainApp;}
    
    public void initStage(Stage stage, ObservableList<Curso> lc) {
        primaryStage = stage;
        cursosList.setItems(lc);
    }
}
