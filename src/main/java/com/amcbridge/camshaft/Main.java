package com.amcbridge.camshaft;

import com.amcbridge.camshaft.controller.GeometryController;
import com.amcbridge.camshaft.controller.MotionLawController;
import com.amcbridge.camshaft.controller.ProfileController;
import com.amcbridge.camshaft.model.CamShaft;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;

public class Main extends Application {

    private final String GEOMETRY_FILE = "view/Geometry.fxml";
    private final String MOTION_LAW_FILE = "view/MotionLaw.fxml";
    private final String PROFILE_FILE = "view/Profile.fxml";
    private TabPane tabPane;
    private CamShaft camShaft;

    @Override
    public void start(Stage primaryStage) {

        camShaft = CamShaft.newInstance();
        tabPane =  new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        //initialize  geometry rab
        initTab("Geometry",GEOMETRY_FILE, new GeometryController(camShaft));
        //initialize motion law tab
        initTab("Motion Law",MOTION_LAW_FILE, new MotionLawController(camShaft));
        //initialize profile tab
        initTab("Profile",PROFILE_FILE,new ProfileController(camShaft));
        Scene scene = new Scene(tabPane,500,500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("CamShaft");
        primaryStage.show();

    }

    public void initTab(String title, String path, Object controller){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(path));
            loader.setController(controller);
            Tab tab = new Tab();
            tab.setText(title);
            tab.setContent((Node) loader.load());
            tabPane.getTabs().add(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        launch(args);
    }

}


