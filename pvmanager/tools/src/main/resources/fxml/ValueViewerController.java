/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

/**
 * FXML Controller class
 *
 * @author carcassi
 */
public class ValueViewerController implements Initializable {
    @FXML
    private TitledPane commonMetadata;
    @FXML
    private TextField typeField;
    @FXML
    private TextField alarmField;
    @FXML
    private TextField timeField;
    @FXML
    private TitledPane numberMetadata;
    @FXML
    private TextField displayRangeField;
    @FXML
    private TextField alarmRangeField;
    @FXML
    private TextField warningRangeField;
    @FXML
    private TextField controlRangeField;
    @FXML
    private TextField unitField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
