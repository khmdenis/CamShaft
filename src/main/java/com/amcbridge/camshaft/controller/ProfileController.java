package com.amcbridge.camshaft.controller;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.Point;
import com.amcbridge.camshaft.service.CamShaftCalculator;
import com.amcbridge.camshaft.service.CommonCamShaftCalculator;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.util.List;


public class ProfileController {
    private static final String MISSING_MOTION_LAW_OR_PARAMETERS = "Parameters or motion law is missing.";
    private CamShaft camShaft;
    @FXML
    private LineChart<Double,Double> profileLineChart;
    @FXML
    private Button buildButton;

    public ProfileController(CamShaft camShaft){ this.camShaft = camShaft;}

    @FXML
    private void initialize(){
        initChart();
    }

    private void initChart(){
        profileLineChart.setAnimated(false);
        buildButton.setOnAction(event -> {
            if(camShaft.getParameters().isEmpty() || camShaft.getMotionLaw().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, MISSING_MOTION_LAW_OR_PARAMETERS);
                alert.setHeaderText(null);
                alert.showAndWait();
                return;
            }
            profileLineChart.getData().clear();
            CamShaftCalculator camShaftCalculator = CommonCamShaftCalculator.build(camShaft);
            camShaftCalculator.calculateProfile();
            addNewSeries(camShaft.getOuterCurve());
            addNewSeries(camShaft.getInnerCurve());

        });

    }

    private void addNewSeries(List<Point> profile){
        Series<Double,Double> series = new Series<>();
        profile.forEach(point -> series.getData().add(new Data<>(point.getX(), point.getY())));
        profileLineChart.getData().add(series);
    }
}
