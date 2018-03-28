/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import modelo.Alumno;
import testlibrary.TestLibrary;

/**
 * FXML Controller class
 *
 * @author impre
 */
public class FXMLAlumnoViewController implements Initializable {
    private TestLibrary mainApp;
    private boolean okAccion = false;
    private Alumno alumno;
    private String accion;
    private Stage modalStage;
    @FXML private GridPane panelGrid;
    @FXML private TextField dni;
    @FXML private TextField nombre;
    @FXML private TextField edad;
    @FXML private TextField direccion;
    @FXML private TextField fechaalta;
    @FXML private ImageView foto;
    @FXML private Label dniMsgError;
    @FXML private Label nombreMsgError;
    @FXML private Label edadMsgError;
    @FXML private Label direccionMsgError;
    @FXML private Label fechaaltaMsgError;
    @FXML private Label imagenMsgError;
    @FXML private Button baceptar;
    @FXML private Button bcancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        edad.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) edad.setText(newValue.replaceAll("[^\\d]", "")); });
    }    
    
    public boolean isOkAccion() {return okAccion;}
    
    public void initStage(Stage stage, Alumno alumno, String acc) throws ParseException {
        
        modalStage = stage;
        this.alumno=alumno;
        accion=acc;
        modalStage.setTitle(accion);
        
        if ("Añadir".equals(accion)) {
            this.dni.setText("");
            this.nombre.setText("");
            this.edad.setText("");
            this.direccion.setText("");
            this.fechaalta.setText(TestLibrary.parseFechaDMA(LocalDate.now()));
        }
        else {
            if ("Borrar".equals(accion)) {panelGrid.disableProperty().setValue(true);}
            
            this.dni.setText(this.alumno.getDni());
            this.nombre.setText(this.alumno.getNombre());
            this.edad.setText(this.alumno.getEdad()+"");
            this.direccion.setText(this.alumno.getDireccion());
            this.fechaalta.setText(TestLibrary.parseFechaDMA(this.alumno.getFechadealta()));
            
            this.foto.setImage(this.alumno.getFoto());
        }
    }

    @FXML private void aceptar(ActionEvent event) throws ParseException {
        
        if ("Borrar".equals(accion)) 
            okAccion = true;
        else
            if (isInputValid()) {
                this.alumno.setDni(dni.getText());
                this.alumno.setNombre(nombre.getText());
                this.alumno.setDireccion(direccion.getText());
                this.alumno.setFoto(foto.getImage());
                this.alumno.setFechadealta(TestLibrary.parseFechaAMD(this.fechaalta.getText()));
                this.alumno.setEdad(Integer.parseInt(edad.getText()));
                okAccion = true;
            }
        if(okAccion) modalStage.close();
    }

    @FXML private void cancelar(ActionEvent event) {modalStage.close();}
    
    public void newFoto(){
        JFileChooser jf = new JFileChooser();
        jf.setDialogTitle("Selección de imagen");
        jf.setAcceptAllFileFilterUsed(false);
        jf.setFileFilter(new FileNameExtensionFilter("jpg, png, gif", "jpg", "png", "gif"));
        int sel = jf.showOpenDialog(null);
        if (sel == JFileChooser.APPROVE_OPTION) {
            String rutaOrigen = jf.getSelectedFile().getAbsolutePath();
            File imageFile = new File(rutaOrigen);
            String fileLocation = imageFile.toURI().toString();
            Image imagen2 = new Image(fileLocation);
            this.foto.setImage(imagen2);
        }
    }
        
    public void setMain(TestLibrary mainApp) {this.mainApp = mainApp;}
    
    private boolean isInputValid() {
        Boolean isValid = true;
        
        if (dni.getText() == null || dni.getText().length() == 0) {
            dniMsgError.setText("Dni No valido! ");
            isValid=false;
        } else dniMsgError.setText("");
        
        if (nombre.getText() == null || nombre.getText().length() == 0) {
            nombreMsgError.setText("Nombre No valido! ");
            isValid=false;
        } else nombreMsgError.setText("");
        
        if (edad.getText() == null || edad.getText().length() == 0) {
            edadMsgError.setText("Edad No valido! ");
            isValid=false;
        } else edadMsgError.setText("");
        
        if (direccion.getText() == null || direccion.getText().length() == 0) {
            direccionMsgError.setText("Direccion No valido! ");
            isValid=false;
        } else direccionMsgError.setText("");
        
        if (!TestLibrary.isFecha(fechaalta.getText()) || fechaalta.getText() == null || fechaalta.getText().length() == 0) {
            fechaaltaMsgError.setText("Fecha de alta No valido! ");
            isValid=false;
        } else fechaaltaMsgError.setText("");
                      
        return isValid;
    }
}
