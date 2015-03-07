/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.graphing;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Creates bubble graphs.
 * 
 * @author mjchao
 */
public class BubbleGraphApp extends BaseGraphApp {
    
    final private BubbleGraphView bubbleGraphView = new BubbleGraphView();
    final private ConfigurationDialog configuration = new ConfigurationDialog();

    @Override
    public BaseGraphView getGraphView() {
	return this.bubbleGraphView;
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	this.addDataFormulae( "=tableOf(column(\"X\", range(-10,10)), column(\"Y\", 'sim://noiseWaveform'), column(\"SIZE\", 'sim://gaussianWaveform'), column(\"COLOR\", 'sim://sineWaveform'))",
                "=tableOf(column(\"X\", range(-10,10)), column(\"Y\", 'sim://noiseWaveform'))",
                "=tableOf(column(\"X\", arrayOf(2,3,4,3,2,1,0,1)), column(\"Y\", arrayOf(0,1,2,3,4,3,2,1)), column(\"SIZE\", arrayOf(5,4,3,2,1,2,3,4)), column(\"SIZE\", arrayOf(\"A\",\"A\",\"B\",\"B\",\"B\",\"B\",\"A\",\"A\")))",
                "=tableOf(column(\"X\", arrayOf(1,2,3,4,5)), column(\"Y\", arrayOf(3,1,4,2,5)), column(\"NAMES\", arrayOf(\"A\", \"A\", \"A\", \"B\", \"B\")))"
	
	);
    }
    
    @Override
    public void openConfigurationPanel() {
	this.configuration.show();
	this.configuration.loadSaved();
    }
    
    private class ConfigurationDialog extends Stage {
	
	private class ConfigurationState {
	    public String xColumn = "";
	    public String yColumn = "";
	    public String sizeColumn = "";
	    public String colorColumn = "";
	    public boolean highlightFocus = false;
	}
	
	private Label lblXColumn = new Label( "X Column: " );
	private TextField txtXColumn = new TextField();
	
	private Label lblYColumn = new Label( "Y Column: " );
	private TextField txtYColumn = new TextField();
	
	private Label lblSizeColumn = new Label( "Size Column: " );
	private TextField txtSizeColumn = new TextField();
	
	private Label lblColorColumn = new Label( "Color Column: " );
	private TextField txtColorColumn = new TextField();
	
	private Label lblHighlightFocus = new Label( "Highlight Focus Value: " );
	private CheckBox chkHighlightFocus = new CheckBox();
	
	private Button cmdConfigure = new Button( "Configure" );
	private Button cmdCancel = new Button( "Cancel" );
	
	private ConfigurationState lastConfiguration = new ConfigurationState();
	
	public ConfigurationDialog() {
	    this.initStyle(StageStyle.UTILITY);
	    
	    GridPane pnlConfigurations = new GridPane();
	    
	    GridPane.setConstraints( lblXColumn , 0 , 0 );
	    GridPane.setConstraints( txtXColumn , 1 , 0 );
	    txtXColumn.textProperty().addListener( new ChangeListener< String >() {

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    if ( !newValue.equals( oldValue ) ) {
			configure();
		    }
		}
		
	    });
	    
	    GridPane.setConstraints( lblYColumn , 0 , 1 );
	    GridPane.setConstraints( txtYColumn , 1 , 1 );
	    txtYColumn.textProperty().addListener( new ChangeListener< String >() {

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    if ( !newValue.equals( oldValue ) ) {
			configure();
		    }
		}
		
	    });
	    
	    GridPane.setConstraints( lblSizeColumn , 0 , 2 );
	    GridPane.setConstraints( txtSizeColumn , 1 , 2 );
	    txtSizeColumn.textProperty().addListener( new ChangeListener< String >() {

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    if ( !newValue.equals( oldValue ) ) {
			configure();
		    }
		}
		
	    });
	    
	    GridPane.setConstraints( lblColorColumn , 0 , 3 );
	    GridPane.setConstraints( txtColorColumn , 1 , 3 );
	    txtColorColumn.textProperty().addListener( new ChangeListener< String >() {

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    if ( !newValue.equals( oldValue ) ) {
			configure();
		    }
		}
		
	    });
	    
	    GridPane.setConstraints( lblHighlightFocus , 0 , 4 );
	    GridPane.setConstraints( chkHighlightFocus , 1 , 4 );
	    chkHighlightFocus.selectedProperty().addListener( new ChangeListener< Boolean >() {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    if ( !newValue.equals( oldValue ) ) {
			configure();
		    }
		}
		
	    });
	    
	    GridPane.setConstraints( cmdConfigure , 0 , 5 );
	    cmdConfigure.setOnAction( new EventHandler< ActionEvent >() {

		@Override
		public void handle(ActionEvent event) {
		    configureAndSave();
		    ConfigurationDialog.this.hide();
		}
	    });
	    
	    GridPane.setConstraints( cmdCancel , 1 , 5 );
	    cmdCancel.setOnAction( new EventHandler< ActionEvent >() {

		@Override
		public void handle(ActionEvent event) {
		    ConfigurationDialog.this.hide();
		}
	    });
	    
	    pnlConfigurations.getChildren().addAll( 
		    lblXColumn , txtXColumn , 
		    lblYColumn , txtYColumn , 
		    lblSizeColumn , txtSizeColumn , 
		    lblColorColumn , txtColorColumn , 
		    lblHighlightFocus , chkHighlightFocus , 
		    cmdConfigure , cmdCancel 
	    );
	    
	    for ( Node n : pnlConfigurations.getChildren() ) {
		if ( n instanceof Control ) {
		    ((Control) n).setMinSize( 0 , 0 );
		    ((Control) n).setMaxSize( Integer.MAX_VALUE , Integer.MAX_VALUE );
		}
	    }
	    
	    for (int j = 0; j < 2; j++) {
		ColumnConstraints cc = new ColumnConstraints();
		cc.setHgrow(Priority.ALWAYS);
		pnlConfigurations.getColumnConstraints().add(cc);
	    }

	    for (int j = 0; j < 6; j++) {
		RowConstraints rc = new RowConstraints();
		rc.setVgrow(Priority.ALWAYS);
		pnlConfigurations.getRowConstraints().add(rc);
	    }
	    
	    Scene s = new Scene( pnlConfigurations);
	    this.setScene( s );
	    this.sizeToScene();
	}
	
	private void loadSaved() {
	    txtXColumn.setText( lastConfiguration.xColumn );
	    txtYColumn.setText( lastConfiguration.yColumn );
	    txtSizeColumn.setText( lastConfiguration.sizeColumn );
	    txtColorColumn.setText( lastConfiguration.colorColumn );
	    chkHighlightFocus.setSelected( lastConfiguration.highlightFocus );
	    configure();
	}
	
	private void configure() {
	    BubbleGraphApp.this.bubbleGraphView.setXColumn( txtXColumn.getText().equals( "" )? null : txtXColumn.getText() );
	    BubbleGraphApp.this.bubbleGraphView.setYColumn( txtYColumn.getText().equals( "" )? null : txtYColumn.getText() );
	    BubbleGraphApp.this.bubbleGraphView.setSizeColumn( txtSizeColumn.getText().equals( "" )? null : txtSizeColumn.getText() );
	    BubbleGraphApp.this.bubbleGraphView.setColorColumn( txtColorColumn.getText().equals( "" )? null : txtColorColumn.getText() );
	    BubbleGraphApp.this.bubbleGraphView.setHighlightFocusValue( chkHighlightFocus.isSelected() );
	}
	
	private void configureAndSave() {
	    configure();
	    lastConfiguration.xColumn = txtXColumn.getText();
	    lastConfiguration.yColumn = txtYColumn.getText();
	    lastConfiguration.sizeColumn = txtSizeColumn.getText();
	    lastConfiguration.colorColumn = txtColorColumn.getText();
	    lastConfiguration.highlightFocus = chkHighlightFocus.isSelected();
	}
    }
    
    final public static void main( String[] args ) {
	launch( args );
    }
}
