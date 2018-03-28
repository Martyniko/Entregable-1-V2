/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testlibrary;

import controller.*;
import accesoaBD.AccesoaBD;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import modelo.Alumno;
import modelo.Curso;
import modelo.Dias;
import modelo.Matricula;

/**
 *
 * @author impre
 */
public class TestLibrary extends Application {
    public static final AccesoaBD acceso= new AccesoaBD();
    public static ObservableList<Alumno> alumnosObsList=FXCollections.observableList(acceso.getAlumnos());
    public static ObservableList<Curso> cursosObsList=FXCollections.observableList(acceso.getCursos());
    public static ObservableList<Matricula> matriculasObsListTodas=FXCollections.observableList(acceso.getMatriculas());
    
    private Stage primaryStage;
    private BorderPane root;
    private AnchorPane mPane;
    private AnchorPane alumnosListView;
    private AnchorPane cursosListView;
    private AnchorPane matriculasListView;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setOnCloseRequest((WindowEvent event) -> {salvar();});
        loadMenu(primaryStage);
    }
    
    public void loadMenu(Stage stage) throws Exception{
        FXMLLoader loader =new FXMLLoader(getClass().getResource("/view/FXMLMenu.fxml"));
        root = (BorderPane) loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Academia");
        stage.setScene(scene);
        FXMLMenuController controladorMenu = loader.<FXMLMenuController>getController();
        controladorMenu.setMain(this);
        controladorMenu.initStage(stage);
        stage.show();
    }
    
    public void loadAlumnos() {
            FXMLAlumnosListController controller = loadLista("/view/FXMLAlumnosList.fxml").getController();
            controller.setMain(this);
            controller.initStage(primaryStage, alumnosObsList);
    }
        
    public Boolean loadVentanaAlumno(Alumno alumno, String accion) throws ParseException {
        Stage estageActual = new Stage();
        FXMLAlumnoViewController controller = loadVentanaModal(estageActual,"/view/FXMLAlumno.fxml").getController();
        controller.initStage(estageActual,alumno,accion);
        estageActual.showAndWait();
        return controller.isOkAccion();
    } 
    
    public void loadCursos() {
            FXMLCursosListController controller = loadLista("/view/FXMLCursosList.fxml").getController();
            controller.setMain(this);
            controller.initStage(primaryStage, cursosObsList);
    }
        
    public Boolean loadVentanaCurso(Curso curso, String accion) throws ParseException {
        Stage estageActual = new Stage();
        FXMLCursoViewController controller = loadVentanaModal(estageActual,"/view/FXMLCurso.fxml").getController();
        controller.initStage(estageActual,curso,accion);
        estageActual.showAndWait();
        return controller.isOkAccion();
    } 
    
    public Boolean loadVentanaMatricula(Curso curso, Matricula matricula, String accion) throws ParseException {
        Stage estageActual = new Stage();
        FXMLMatriculaViewController controller = loadVentanaModal(estageActual,"/view/FXMLMatricula.fxml").getController();
        controller.setMain(this);
        controller.initStage(estageActual,curso,matricula,accion);
        estageActual.showAndWait();
        return controller.isOkAccion();
    }
    
    public FXMLLoader loadLista(String vista) {
        FXMLLoader loader =new FXMLLoader(getClass().getResource(vista));
        try {
            mPane = (AnchorPane) loader.load();
            root.setCenter(mPane);
        } catch (IOException e) {e.printStackTrace();}
        return loader;
    }
    
    public FXMLLoader loadVentanaModal(Stage stage,String vista) {
        FXMLLoader loader =new FXMLLoader(getClass().getResource(vista));
        try {
            Parent roots = (Parent) loader.load();
            Scene scene = new Scene(roots);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e) {e.printStackTrace();}
        return loader;
    }
    
    public Stage getPrimaryStage() {return primaryStage;}
    
    public static void main(String[] args) {launch(args);}
    
    public static void salvar(){
        acceso.salvar();}
    
    public static String diasToString(ArrayList<Dias> dias) {
        String result="";
        if (dias!=null)
            result = dias.stream().map((dia) -> dia.toString()+" ").reduce(result, String::concat);
        return result;
    }
    
         
    public void loadAviso(String titulo,String aviso,String msg){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(aviso);
        alert.setContentText(msg);
        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        Optional<ButtonType> result = alert.showAndWait();
    }
    
    public static boolean isFecha(String fechaAValidar) {
        String ExpReg ="^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$";
        return (fechaAValidar.matches(ExpReg));
       }
    
    public static boolean isHora(String horaAValidar) {
        String ExpReg ="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
        return (horaAValidar.matches(ExpReg));
    }
        
    public static LocalTime parseHoraHM(String hora) throws ParseException{
        return LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm"));
    }
    public static LocalDate parseFechaAMD(String fecha) throws ParseException{
        return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    public static String  parseFechaDMA(LocalDate fecha) {
        return  fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); 
    }
}
