package com.amcbridge.camshaft.controller;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.CamShaftType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class GeometryController {
    private static final String IS_EMPTY_FIELD = "Please, fill in all fields.";
    @FXML
    private ChoiceBox<CamShaftType> camShaftTypeChoiceBox;
    @FXML
    private Pane translatingPane;
    @FXML
    private Pane rotatingPane;
    @FXML
    private Button applyButton;

    private CamShaft camShaft;
    private Map<CamShaftType,Pane> panes;

    public GeometryController(CamShaft camShaft) {
        this.camShaft = camShaft;
    }

    @FXML
    private void initialize(){
        initPanes();
        initChoiceBox();
        initApplyButton();
    }

    private void initApplyButton(){
        applyButton.setOnAction(event -> {
            CamShaftType camShaftType = camShaftTypeChoiceBox.getSelectionModel().getSelectedItem();
           Set<TextField> textFieldSet = panes.get(camShaftType).lookupAll(".text-field").stream()
                    .map(node -> (TextField) node).collect(Collectors.toSet());
            Map<String,Double> params = readParams(textFieldSet);
            if(params != null){
              camShaft.setType(camShaftType);
              camShaft.setParameters(params);
            }
        });
    }
    private Map<String,Double> readParams(Set<TextField> textFieldSet){
        Map<String,Double> params = new HashMap();
        for(TextField textField:textFieldSet){
            if (textField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, IS_EMPTY_FIELD);
                alert.setHeaderText(null);
                alert.showAndWait();
                return null;
            }
            params.put(textField.getId(), Double.parseDouble(textField.getText()));
        }
        return params;
    }

    private void initPanes(){

        panes = new HashMap();
        panes.put(CamShaftType.ROTATING, rotatingPane);
        panes.put(CamShaftType.TRANSLATING, translatingPane);
    }

    private void initChoiceBox(){
        camShaftTypeChoiceBox.setItems(FXCollections.observableList(Arrays.asList(CamShaftType.values())));
        camShaftTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener(new CamShaftTypeChoiceBoxChangeListener());
        camShaftTypeChoiceBox.getSelectionModel().selectFirst();
    }
    private class CamShaftTypeChoiceBoxChangeListener implements ChangeListener<CamShaftType> {
        @Override
        public void changed(ObservableValue<? extends  CamShaftType> observable, CamShaftType oldValue, CamShaftType newValue) {
            for(Pane p:panes.values()) p.setVisible(false);
            panes.get(newValue).setVisible(true);
        }
    }
}
