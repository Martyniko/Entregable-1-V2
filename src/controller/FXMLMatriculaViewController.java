/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import modelo.Alumno;
import modelo.Curso;
import modelo.Matricula;
import testlibrary.TestLibrary;


/**
 * FXML Controller class
 *
 * @author impre
 */
public class FXMLMatriculaViewController implements Initializable {
    private TestLibrary mainApp;
    private boolean okAccion = false;
    private Matricula matricula;
    private Curso curso;
    private Alumno alumno;
    private String accion;
    private Stage modalStage;
    @FXML private GridPane panelGrid;
    @FXML private ComboBox<Alumno> nombreAlumno;
    @FXML private TextField fecha;
    @FXML private Label nombreAlumnoMsgError;
    @FXML private Label tituloCurso;
    @FXML private Label fechaMsgError;
    @FXML private Button baceptar;
    @FXML private Button bcancelar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nombreAlumno.getSelectionModel().selectedItemProperty().addListener((o, oldval, newval) -> {alumno = newval;});
    }    
    
    public boolean isOkAccion() {return okAccion;}
    
    public void initStage(Stage stage,Curso curso, Matricula matricula, String a) throws ParseException {
        System.out.println("matricular 5");
        this.curso=curso;    
        modalStage = stage;
        this.matricula=matricula;
        accion=a;
        modalStage.setTitle(accion);
        this.tituloCurso.setText(this.curso.getTitulodelcurso());
        nombreAlumno.setItems(mainApp.alumnosObsList);
        Callback<ListView<Alumno>, ListCell<Alumno>> factory = lv -> new ListCell<Alumno>() {
            @Override
            protected void updateItem(Alumno item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNombre());
            }
        };
        nombreAlumno.setCellFactory(factory);
        nombreAlumno.setButtonCell(factory.call(null));
        if ("Añadir".equals(accion)) {
            //this.nombreAlumno.setText("");
            this.fecha.setText(mainApp.parseFechaDMA(LocalDate.now()));
        }
        else {
            if ("Borrar".equals(accion)) {panelGrid.disableProperty().setValue(true);}
            this.fecha.setText(mainApp.parseFechaDMA(this.matricula.getFecha()));
            this.nombreAlumno.getSelectionModel().select(this.matricula.getAlumno());
        }
    }

    @FXML private void aceptar(ActionEvent event) throws ParseException {
        
        if ("Borrar".equals(accion)) 
            okAccion = true;
        else
            if (isInputValid()) {
                this.matricula.setCurso(curso);
                this.matricula.setAlumno(alumno);
                this.matricula.setFecha(mainApp.parseFechaAMD(this.fecha.getText()));
                okAccion = true;
            }
        if(okAccion) modalStage.close();
    }

    @FXML private void cancelar(ActionEvent event) {
        modalStage.close();
    }
      
    public void setMain(TestLibrary mainApp) {
        this.mainApp = mainApp;
    }
    
    private boolean isInputValid() {
        Boolean isValid = true;
        
        if (fecha.getText() == null || fecha.getText().length() == 0) {
            fechaMsgError.setText("Fecha de alta No valido! ");
            //isValid=false;
        }
        else fechaMsgError.setText("");
                      
        return isValid;
    }
}
