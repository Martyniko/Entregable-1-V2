/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import modelo.Curso;
import testlibrary.TestLibrary;

/**
 * FXML Controller class
 *
 * @author impre
 */
public class FXMLCursoViewController implements Initializable {
    private TestLibrary mainApp;
    private boolean okAccion = false;
    private Curso curso;
    private String accion;
    private Stage modalStage;
    @FXML private GridPane panelGrid;
    @FXML private TextField titulo;
    @FXML private TextField profesor;
    @FXML private TextField maxalumnos;
    @FXML private TextField hora;
    @FXML private TextField fechainicio;
    @FXML private TextField fechafin;
    @FXML private TextField dias;
    @FXML private TextField aula;
    @FXML private Label tituloMsgError;
    @FXML private Label profesorMsgError;
    @FXML private Label maxalumnosMsgError;
    @FXML private Label horaMsgError;
    @FXML private Label fechainicioMsgError;
    @FXML private Label fechafinMsgError;
    @FXML private Label imagenMsgError;
    @FXML private Button baceptar;
    @FXML private Button bcancelar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        maxalumnos.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) maxalumnos.setText(newValue.replaceAll("[^\\d]", "")); });
    }    
    
    public boolean isOkAccion() {return okAccion;}
    
    public void initStage(Stage stage, Curso curso, String a) {
        
        modalStage = stage;
        this.curso=curso;
        accion=a;
        modalStage.setTitle(accion);
        
        if ("Añadir".equals(accion)) {
            this.titulo.setText("");
            this.profesor.setText("");
            this.maxalumnos.setText("");
            this.fechainicio.setText("");
            this.fechafin.setText("");
            this.hora.setText("");
            this.dias.setText("");
            this.aula.setText("");
            
        }
        else {
            if ("Borrar".equals(accion)) {panelGrid.disableProperty().setValue(true);}
            
            this.titulo.setText(this.curso.getTitulodelcurso());
            this.profesor.setText(this.curso.getProfesorAsignado());
            this.maxalumnos.setText(this.curso.getNumeroMaximodeAlumnos()+"");
            this.fechainicio.setText(TestLibrary.parseFechaDMA(this.curso.getFechainicio()));
            this.fechafin.setText(TestLibrary.parseFechaDMA(this.curso.getFechafin()));
            
            this.hora.setText(""+this.curso.getHora());
            this.aula.setText(this.curso.getAula());
        }
    }

    @FXML private void aceptar(ActionEvent event) throws ParseException {
        
        if ("Borrar".equals(accion)) 
            okAccion = true;
        else
            if (isInputValid()) {
                this.curso.setTitulodelcurso(titulo.getText());
                this.curso.setProfesorAsignado(profesor.getText());
                this.curso.setFechainicio(TestLibrary.parseFechaAMD(this.fechainicio.getText()));
                this.curso.setFechafin(TestLibrary.parseFechaAMD(this.fechafin.getText()));
                this.curso.setHora(TestLibrary.parseHoraHM(hora.getText()));
                this.curso.setAula(aula.getText());
                this.curso.setNumeroMaximodeAlumnos(Integer.parseInt(maxalumnos.getText()));
                okAccion = true;
            }
        if(okAccion) modalStage.close();
    }

    @FXML private void cancelar(ActionEvent event) {
        modalStage.close();
    }
    
    private boolean isInputValid() {
        Boolean isValid = true;
        
        if (titulo.getText() == null || titulo.getText().length() == 0) {
            tituloMsgError.setText("Título No valido! ");
            isValid=false;
        } else tituloMsgError.setText("");
        
        if (profesor.getText() == null || profesor.getText().length() == 0) {
            profesorMsgError.setText("Profesor No valido! ");
            isValid=false;
        } else profesorMsgError.setText("");
        
        if (maxalumnos.getText() == null || maxalumnos.getText().length() == 0) {
            maxalumnosMsgError.setText("Máximo de alumnos No valido! ");
            isValid=false;
        } else maxalumnosMsgError.setText("");
        
        if (hora.getText() == null || hora.getText().length() == 0) {
            horaMsgError.setText("Hora No valido! ");
            //isValid=false;
        } else horaMsgError.setText("");
        
        if (fechainicio.getText() == null || fechainicio.getText().length() == 0) {
            fechainicioMsgError.setText("Fecha de inicio No valido! ");
            //isValid=false;
        } else fechainicioMsgError.setText("");
        
        if (fechafin.getText() == null || fechafin.getText().length() == 0) {
            fechafinMsgError.setText("Fecha de inicio No valido! ");
            //isValid=false;
        } else fechafinMsgError.setText("");
                      
        return isValid;
    }
}
