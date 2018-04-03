/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import modelo.Curso;
import modelo.Dias;
import modeloaux.Item;
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
    @FXML private ListView<Item> lwdias;
    @FXML private TextField aula;
    @FXML private Label tituloMsgError;
    @FXML private Label profesorMsgError;
    @FXML private Label maxalumnosMsgError;
    @FXML private Label horaMsgError;
    @FXML private Label diasMsgError;
    @FXML private Label fechainicioMsgError;
    @FXML private Label fechafinMsgError;
    @FXML private Label imagenMsgError;
    @FXML private Button baceptar;
    @FXML private Button bcancelar;

    private final ObservableList<Item> diasImparte=FXCollections.observableArrayList();
    private final ArrayList<Dias> nDias=new ArrayList<>();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iniDias();
        lwdias.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lwdias.setCellFactory(CheckBoxListCell.forListView(new Callback<Item, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Item item) {return item.onProperty();}
        }));

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
            this.aula.setText("");
        } else {
            if ("Borrar".equals(accion)) {panelGrid.disableProperty().setValue(true);}
            this.titulo.setText(this.curso.getTitulodelcurso());
            this.profesor.setText(this.curso.getProfesorAsignado());
            this.maxalumnos.setText(this.curso.getNumeroMaximodeAlumnos()+"");
            this.fechainicio.setText(TestLibrary.parseFechaDMA(this.curso.getFechainicio()));
            this.fechafin.setText(TestLibrary.parseFechaDMA(this.curso.getFechafin()));
            loadDias(this.curso.getDiasimparte());
            this.hora.setText(""+this.curso.getHora());
            this.aula.setText(this.curso.getAula());
        }
    }
    
    private void iniDias(){
        diasImparte.add(new Item("Lunes"));
        diasImparte.add(new Item("Martes"));
        diasImparte.add(new Item("Miercoles"));
        diasImparte.add(new Item("Jueves"));
        diasImparte.add(new Item("Viernes"));
        diasImparte.add(new Item("Sabado"));
        diasImparte.add(new Item("Domingo"));
        lwdias.setItems(diasImparte);
    }
    
    private void loadDias(ArrayList<Dias> inDias){
        if(inDias!=null)  inDias.forEach((dia) -> {diasImparte.get(dia.ordinal()).setOn(true);});
    }
    
    private void saveDias(){
        diasImparte.forEach((dia)->{if (dia.isOn()) nDias.add(Dias.valueOf(dia.getName()));});
    }
    
    @FXML private void aceptar(ActionEvent event) throws ParseException {
        
        if ("Borrar".equals(accion)) 
            okAccion = true;
        else
            saveDias();
            if (isInputValid()) {
                this.curso.setTitulodelcurso(titulo.getText());
                this.curso.setProfesorAsignado(profesor.getText());
                this.curso.setFechainicio(TestLibrary.parseFechaAMD(this.fechainicio.getText()));
                this.curso.setFechafin(TestLibrary.parseFechaAMD(this.fechafin.getText()));
                this.curso.setHora(TestLibrary.parseHoraHM(hora.getText()));
                this.curso.setAula(aula.getText());
                this.curso.setNumeroMaximodeAlumnos(Integer.parseInt(maxalumnos.getText()));
                this.curso.setDiasimparte(nDias);
                okAccion = true;
            }
        if(okAccion) modalStage.close();
    }
        
    @FXML private void cancelar(ActionEvent event) {modalStage.close();}
    
    private boolean isInputValid() {
        Boolean isValid = true;
        
        if (TestLibrary.isVacio(titulo)) {
            tituloMsgError.setText("Título No valido! ");
            isValid=false;
        } else tituloMsgError.setText("");
        
        if (TestLibrary.isVacio(profesor)) {
            profesorMsgError.setText("Profesor No valido! ");
            isValid=false;
        } else profesorMsgError.setText("");
        
        if (TestLibrary.isVacio(maxalumnos)) {
            maxalumnosMsgError.setText("Máximo de alumnos No valido! ");
            isValid=false;
        } else maxalumnosMsgError.setText("");
        
        if (TestLibrary.isVacio(hora) || !TestLibrary.isHora(hora.getText())) {
            horaMsgError.setText("Hora No valido! ");
            isValid=false;
        } else horaMsgError.setText("");
        
        if (nDias.isEmpty()) {
            diasMsgError.setText("Marque los días que se imparte");
            isValid=false;
        } else diasMsgError.setText("");
        
        if (TestLibrary.isVacio(fechainicio) || !TestLibrary.isFecha(fechainicio.getText())) {
            fechainicioMsgError.setText("Fecha de inicio No valido! ");
            isValid=false;
        } else fechainicioMsgError.setText("");
        
        if (TestLibrary.isVacio(fechafin) || !TestLibrary.isFecha(fechafin.getText())) {
            fechafinMsgError.setText("Fecha de inicio No valido! ");
            isValid=false;
        } else fechafinMsgError.setText("");
                      
        return isValid;
    }
}
