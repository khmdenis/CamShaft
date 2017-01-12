package com.amcbridge.camshaft.controller;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.Point;
import com.amcbridge.camshaft.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;


public class MotionLawController {

    private static final String FILE_NOT_FOUND = "File incorrect or not found.";
    private static final String INPUT_PROBLEM = "Error! Please, try again.";



    @FXML
    private LineChart<Double, Double> lineChart;
    @FXML
    private Button openButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button functionButton;
    @FXML
    private Button clearButton;

    private CamShaft camShaft;
    private ObservableList<Data<Double,Double>> motionLawObservableList;
    public MotionLawController(CamShaft camShaft) {
        this.camShaft = camShaft;
    }

    @FXML
    private void initialize() {
        initAddNewPoint();
        motionLawObservableList =  FXCollections.observableArrayList();
        synchronizedLists();
        lineChart.getData().add(new Series<>(motionLawObservableList));
        motionLawObservableList.addListener(new MotionLawListChangeListener());
        initOpenButton();
        initFunctionButton();
        initSaveButton();
        initClearButton();

    }
    private void initAddNewPoint(){
        lineChart.setOnMousePressed(event -> {
            if (event.getClickCount() >= 2) {
                Point point = getCoordinate(event);
                camShaft.getMotionLaw().add(point);
                motionLawObservableList.add(new Data<>(point.getX(), point.getY()));

            }
        });
    }
    private void initOpenButton() {
        openButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            try {
                List<Point> motionLaw = MotionLawService.readMotionLawFromFile(file);
                camShaft.getMotionLaw().clear();
               camShaft.getMotionLaw().addAll(motionLaw);
            } catch (FileNotFoundException | InputMismatchException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING, FILE_NOT_FOUND);
                alert.setHeaderText(null);
                alert.showAndWait();
            }
            synchronizedLists();
        });

    }

    private void initSaveButton() {
        saveButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(null);
            try {
                MotionLawService.writeMotionLawToFile(file,camShaft.getMotionLaw());
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING, INPUT_PROBLEM);
                alert.setHeaderText(null);
                alert.showAndWait();
            }

        });
    }

    private void initFunctionButton() {
        Dialog<List<Point>> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        TextField functionTextField = new TextField("(sin(x/180*pi)^2-sin(x/180*pi))*50");
        functionTextField.setPrefWidth(300);
        TextField stepTextField = new TextField("18");
        stepTextField.setPrefWidth(50);
        gridPane.addRow(0, new Label("Y(x)="), functionTextField);
        gridPane.addRow(1, new Label("Step:"), stepTextField);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return camShaft.getMotionLaw();
            }
            return null;
        });
        functionButton.setOnAction(event -> {
            Optional<List<Point>> result = dialog.showAndWait();
            result.ifPresent(motionLaw -> {
                motionLaw.clear();
                camShaft.getMotionLaw().addAll(MotionLawService.calculateMotionLaw(
                        functionTextField.getText(),
                        Double.valueOf(stepTextField.getText())
                ));
                synchronizedLists();
            });
        });
    }

    private void initClearButton() {
        clearButton.setOnAction(event -> {
            camShaft.getMotionLaw().clear();
            motionLawObservableList.clear();
        });
    }

    private Point getCoordinate(MouseEvent event) {
        Point2D point = new Point2D(event.getSceneX(), event.getSceneY());
        Double xAxisLocal = lineChart.getXAxis().sceneToLocal(point).getX();
        Double yAxisLocal = lineChart.getYAxis().sceneToLocal(point).getY();
        Double x = lineChart.getXAxis().getValueForDisplay(xAxisLocal);
        Double y = lineChart.getYAxis().getValueForDisplay(yAxisLocal);
        return new Point(x, y);

    }

    private void initContextMenuForPoint(Data<Double,Double> chartPoint,Point motionLawPoint){
        Node node = chartPoint.getNode();
        node.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                final ContextMenu contextMenu = new ContextMenu();
                MenuItem deleteItem = new MenuItem("Delete");
                deleteItem.setOnAction(itemEvent -> {
                    motionLawObservableList.remove(chartPoint);
                    camShaft.getMotionLaw().remove(motionLawPoint);
                });
                MenuItem moveItem = new MenuItem("Move");
                moveItem.setOnAction(itemEvent ->{
                    Dialog<Data<Double,Double>> dialog = new Dialog<>();
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(5);
                    gridPane.setVgap(5);
                    TextField xValue = new TextField(chartPoint.getXValue().toString());
                    TextField yValue = new TextField(chartPoint.getYValue().toString());
                    gridPane.addRow(0, new Label("X: "), xValue, new Label("Y: "), yValue);
                    dialog.getDialogPane().setContent(gridPane);
                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == ButtonType.OK) {
                            return new Data<>(Double.valueOf(xValue.getText()),
                                    Double.valueOf(yValue.getText()));
                        }
                        return null;
                    });
                    Optional<Data<Double, Double>> result = dialog.showAndWait();
                    result.ifPresent(point -> {
                        chartPoint.setXValue(point.getXValue());
                        chartPoint.setYValue(point.getYValue());
                        motionLawPoint.setX(point.getXValue());
                        motionLawPoint.setY(point.getYValue());
                    });
                });
                contextMenu.getItems().addAll(moveItem,deleteItem);
                contextMenu.show(node, event.getScreenX(), event.getScreenY());
            }
     });
    }

    private Point findPoint(Double x, Double y){
        for(Point point: camShaft.getMotionLaw()){
            if(x == point.getX() && y == point.getY())
                return point;
        }
        return null;
    }
    private void synchronizedLists(){
        if(motionLawObservableList != null && !motionLawObservableList.isEmpty())
            motionLawObservableList.clear();
        for(Point point: camShaft.getMotionLaw()){
          motionLawObservableList.add(new Data<>(point.getX(),point.getY()));
        }
    }
    private class MotionLawListChangeListener implements ListChangeListener<Data<Double, Double>> {
        @Override
        public void onChanged(Change<? extends Data<Double, Double>> change) {
            while(change.next()){
                    for(Data<Double,Double> data : change.getAddedSubList()) {
                        Point p = findPoint(data.getXValue(),data.getYValue());
                        initContextMenuForPoint(data,p);
                        data.getNode().addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
                                Point point = getCoordinate(event);
                                data.setXValue(point.getX());
                                data.setYValue(point.getY());
                                p.setX(point.getX());
                                p.setY(point.getY());
                        });
                    }
            }
        }
    }

}
