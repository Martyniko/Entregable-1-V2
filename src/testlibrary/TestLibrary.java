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
import java.util.function.Consumer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Curso;
import modelo.Matricula;

/**
 *
 * @author impre
 */
public class TestLibrary extends Application {
    private static FXMLMenuController controladorMenu;
    private static FXMLAlumnoViewController FXMLAlumnoViewController;
    public static final AccesoaBD acceso= new AccesoaBD();
    public static final ObservableList<Alumno> alumnosObsList=FXCollections.observableList(acceso.getAlumnos());
    public static final ObservableList<Curso> cursosObsList=FXCollections.observableList(acceso.getCursos());
    public static ObservableList<Matricula> matriculasObsList;
    
    private Stage primaryStage;
    private BorderPane root;
    private AnchorPane alumnosListView;
    private AnchorPane cursosListView;
    private AnchorPane matriculasListView;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        //acceso = new AccesoaBD();
        //alumnosObsList.setAll(acceso.getAlumnos());
        //alumnosList = FXCollections.observableList(acceso.getAlumnos());
        //CursosList= FXCollections.observableList(acceso.getCursos());
        //MatriculasList= FXCollections.observableList(acceso.getMatriculas());
        showMenu(primaryStage);
    }
    
    public void showMenu(Stage stage) throws Exception{
        FXMLLoader loader =new FXMLLoader(getClass().getResource("/view/FXMLMenu.fxml"));
        root = (BorderPane) loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Academia");
        stage.setScene(scene);
        controladorMenu = loader.<FXMLMenuController>getController();
        controladorMenu.setMain(this);
        controladorMenu.initStage(stage);
        stage.show();
    }
    
    public void showAlumnos() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TestLibrary.class.getResource("/view/FXMLAlumnosList.fxml"));
            alumnosListView = (AnchorPane) loader.load();
            root.setCenter(alumnosListView);
            FXMLAlumnosListController controller = loader.getController();
            controller.setMain(this);
            controller.initStage(primaryStage, alumnosObsList);
        } catch (IOException e) {e.printStackTrace();}
    }
        
    public Boolean showVentanaAlumno(Alumno alumno, String accion) throws ParseException {
        try {
            Stage estageActual = new Stage();
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/view/FXMLAlumno.fxml"));
            Parent roots = (Parent) loader.load();
            Scene scene = new Scene(roots);
            estageActual.setScene(scene);
            estageActual.initModality(Modality.APPLICATION_MODAL);
            FXMLAlumnoViewController = loader.<FXMLAlumnoViewController>getController();
            FXMLAlumnoViewController.initStage(estageActual,alumno,accion);
            estageActual.showAndWait();
            return FXMLAlumnoViewController.isOkAccion();
        } catch (IOException e)  {return false;} 
    } 
    
    public void showCursos() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TestLibrary.class.getResource("/view/FXMLCursosList.fxml"));
            cursosListView = (AnchorPane) loader.load();
            root.setCenter(cursosListView);
            FXMLCursosListController controller = loader.getController();
            controller.setMain(this);
            controller.initStage(primaryStage, cursosObsList);
        } catch (IOException e) {e.printStackTrace();}
    }
        
    public Boolean showVentanaCurso(Curso curso, String accion) throws ParseException {
        try {
            Stage estageActual = new Stage();
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/view/FXMLCurso.fxml"));
            Parent roots = (Parent) loader.load();
            Scene scene = new Scene(roots);
            estageActual.setScene(scene);
            estageActual.initModality(Modality.APPLICATION_MODAL);
            FXMLCursoViewController FXMLCursoViewController = loader.<FXMLCursoViewController>getController();
            FXMLCursoViewController.initStage(estageActual,curso,accion);
            estageActual.showAndWait();
            return FXMLCursoViewController.isOkAccion();
        } catch (IOException e)  {return false;} 
    } 
    
    public void showMatriculas(Curso curso) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TestLibrary.class.getResource("/view/FXMLMatriculasList.fxml"));
            matriculasListView = (AnchorPane) loader.load();
            root.setCenter(matriculasListView);
            FXMLMatriculasListController controller = loader.getController();
            controller.setMain(this);
            controller.initStage(primaryStage, curso);
        } catch (IOException e) {e.printStackTrace();}
    }
        
    public Boolean showVentanaMatricula(Curso curso, Matricula matricula, String accion) throws ParseException {
        try {
            Stage estageActual = new Stage();
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/view/FXMLMatricula.fxml"));
            System.out.println("matricular 1");
            Parent roots = (Parent) loader.load();
            System.out.println("matricular 2");
            Scene scene;
            scene = new Scene(roots);
            System.out.println("matricular 3");
            estageActual.setScene(scene);
            estageActual.initModality(Modality.APPLICATION_MODAL);
            FXMLMatriculaViewController controller = loader.<FXMLMatriculaViewController>getController();
            controller.setMain(this);
            
            controller.initStage(estageActual,curso,matricula,accion);
            estageActual.showAndWait();
            return controller.isOkAccion();
        } catch (IOException e)  {return false;} 
    }
    public Stage getPrimaryStage() {return primaryStage;}
    
    public static void main(String[] args) {launch(args);}
    
    public static void salvar(){
        acceso.salvar();
    }
    
    public static ObservableList<Matricula> getMatriculasCurso(Curso curso) {
        matriculasObsList= FXCollections.observableList(acceso.getMatriculasDeCurso(curso.getTitulodelcurso()));
        return matriculasObsList;
    }
    
    public  static Boolean isAlumnoEnCurso(Alumno alumno) {
        Boolean isOk=false;
        for (Matricula matricula: matriculasObsList) {
            if(alumno.getDni().equals(matricula.getAlumno().getDni())) isOk=true;
        }
        return isOk;
    }
    
    public static Boolean isCursoCompleto(Curso mcurso) {
        if(matriculasObsList.isEmpty()) return false;
        else {
            int ma=mcurso.getNumeroMaximodeAlumnos();
            int nm=matriculasObsList.size();
            return (ma == nm);
        }
        
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
