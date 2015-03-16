/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.graphing;

import java.util.ArrayList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Allows the user to configure the properties of a graph (e.g. the x column of
 * a bubble graph, or the interpolation scheme of a line graph).
 * <p>
 * To create a custom <code>ConfigurationDialog</code>, in the containing class, 
 * list out what properties the user may configure. In most cases, the containing
 * class will be of type <code>BaseGraphApp</code>, and the supported property types are
 * <ul>
 *  <li> String
 *  <li> Boolean
 *  <li> InterpolationScheme (TODO)
 * </ul>
 * 
 * <p>
 * Then, determine what code should execute when each property changes, and create
 * a <code>Runnable</code> for each of these actions. For example,
 * 
 * <p>
 * <pre>
 * <code>
 * Runnable onPropertyChanged = new Runnable() {
 *     public void run() {
 *          //update graph view to reflect this property change
 *     }
 * };
 * </code>
* </pre>
* 
 * <p>
 * Finally, in the containing class, determine the appropriate <code>addProperty()</code>
 * method to add each property to the configuration dialog. For example, to add
 * a string property, we would use
 * 
 * <p>
 * <pre>
 * <code>
 * StringProperty newProperty = new StringProperty( this , "My String Property" , "" );
 * Runnable onStringChanged = new Runnable() {
 *     public void run() {
 *         graph.update( graph.newUpdate().xColumn( newProperty.getValue() );
 *     }
 * };
 * config.addStringProperty( newProperty , onStringChanged );
 * </code>
 * </pre>
 * <p>
 * Then, the configuration dialog will display the name of the property, "My String Property",
 * in a label, and provide a text field for the user to edit the property.
 * 
 * @author mjchao
 */
public class ConfigurationDialog extends Stage {
    
    /**
     * Contains the configuration field in which the user's current configurations
     * for a property are stored, and remembers the last saved configuration for 
     * this property
     * 
     * @param <T> the type of data used for this configuration (e.g. string, boolean)
     */
    private class ConfigurationData< T > {

	final public ConfigurationField< T > field;
	final public Property< T > lastSavedProperty;
	
	public ConfigurationData( ConfigurationField< T > f , Property< T > p ) {
	    this.field = f;
	    this.lastSavedProperty = p;
	}
    }
    
    /**
     * stores the data for all the properties in this configuration dialog
     */
    private ArrayList< ConfigurationData > configurationData = new ArrayList< ConfigurationData >();
    
    /**
     * panel containing everything in this dialog
     */
    final private BorderPane pnlMain = new BorderPane();
    
    /**
     * panel containing the configuration fields in which the user enters
     * his/her choices
     */
    final private GridPane pnlConfigurations = new GridPane();
    
    /**
     * panel providing options for the user to save the current configurations
     * or cancel and revert to the previously saved configuration
     */
    final private GridPane pnlSaveCancel = new GridPane();
    
    /**
     * Creates a default configuration dialog with no configurations available
     */
    public ConfigurationDialog() {
	this.initStyle( StageStyle.UTILITY );
	Scene s = new Scene( pnlMain );
	pnlMain.setCenter( pnlConfigurations );
	pnlMain.setBottom( pnlSaveCancel );
	
	Button cmdConfigure = new Button( "Configure" );
	cmdConfigure.setOnAction( new EventHandler< ActionEvent >() {

	    @Override
	    public void handle(ActionEvent event) {
		saveChanges();
	    }
	});
	
	Button cmdCancel = new Button( "Cancel" );
	cmdCancel.setOnAction( new EventHandler< ActionEvent >() {

	    @Override
	    public void handle(ActionEvent event) {
		cancelChanges();
	    }
	});
	
	pnlSaveCancel.add( cmdConfigure , 0 , 0 );
	pnlSaveCancel.add( cmdCancel , 1 , 0 );
	this.setScene( s );
    }
    
    /**
     * Adds the given string property as something the user may configure. In the dialog,
     * this property will have a label containing the name of the property and a
     * text field in which the user can enter his/her configuration for this
     * property.
     * 
     * @param p the string property that the user may modify
     * @param onPropertyChanged what to do when the user changes this string property
     */
    public void addStringProperty( StringProperty p , Runnable onPropertyChanged ) {
	StringField newField = new StringField( p , onPropertyChanged );
	this.pnlConfigurations.add( newField , 0 , this.configurationData.size() );
	ConfigurationData data = new ConfigurationData( newField , new SimpleStringProperty( p.getValue() ) );
	this.configurationData.add( data );
    }
    
    /**
     * Adds the given boolean property as something the user may configure. In the dialog,
     * this property will have a label containing the name of the property and a 
     * check box which the user can use to toggle this property to true or false.
     * 
     * @param p the boolean property that the user may modify
     * @param onPropertyChanged what to do when the user changes this property
     */
    public void addBooleanProperty( BooleanProperty p , Runnable onPropertyChanged ) {
	BooleanField newField = new BooleanField( p , onPropertyChanged );
	this.pnlConfigurations.add( newField , 0 , this.configurationData.size() );
	ConfigurationData data = new ConfigurationData( newField , new SimpleBooleanProperty( p.getValue() ) );
	this.configurationData.add( data );
    }
    
    /**
     * Adds the given string property as something the ser may configure from a 
     * list of predefined options. In the dialog, this property will have a label
     * containing the name of the property and a combobox which the user can
     * use to change this property.
     * 
     * @param p a string property that the user may modify
     * @param list the list of different strings the property may be set to
     * @param onPropertyChanged what to do when the user changes this property
     */
    public void addListProperty( StringProperty p , String[] list , Runnable onPropertyChanged ) {
	ListField newField = new ListField( p , onPropertyChanged , list );
	this.pnlConfigurations.add( newField , 0 , this.configurationData.size() );
	ConfigurationData data = new ConfigurationData( newField , new SimpleStringProperty( p.getValue() ) );
	this.configurationData.add( data );
    }
    
    /**
     * Saves the current configuration state so that the user may revert to it
     * later if necessary
     */
    public void saveChanges() {
	for ( ConfigurationData d : this.configurationData ) {
	    d.lastSavedProperty.setValue( d.field.getValue() );
	}
    }
    
    /**
     * Discards the user's current configurations and reverts to the previous
     * saved configuration state
     */
    public void cancelChanges() {
	loadSaved();
    }
    
    /**
     * Loads the previously saved configuration state, discarding any 
     * configurations that may currently exist.
     */
    public void loadSaved() {
	for ( ConfigurationData d : this.configurationData ) {
	    d.field.setValue( d.lastSavedProperty.getValue() );
	}
    }
    
    /**
     * Opens this configuration dialog.
     */
    public void open() {
	this.show();
	this.toFront();
	this.sizeToScene();
	this.loadSaved();
    }
    
    /**
     * Provides the user with the ability to configure one property
     * 
     * @param <T> the type of property this field allows the user to modify
     */
    private class ConfigurationField< T > extends FlowPane {
	
	/**
	 * the property the user is allowed to modify
	 */
	final private Property< T > m_property;
	
	/**
	 * Creates a configuration field for the given property.
	 * 
	 * @param property the property the user may configure
	 * @param onPropertyChanged what to do if the user changes the property
	 */
	public ConfigurationField( Property< T > property , Runnable onPropertyChanged) {
	    this.m_property = property;
	    
	    this.m_property.addListener( new ChangeListener< T >() {

		@Override
		public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
		    onPropertyChanged.run();
		}

	    });
	}
	
	public Property< T > property() {
	    return this.m_property;
	}
	
	public void setValue( T value ) {
	    this.m_property.setValue( value );
	}
	
	public T getValue() {
	    return this.m_property.getValue();
	}
    }
    
    /**
     * Creates a default name label for a property. This simply takes the given
     * property name, and appends ":     " to it.
     * 
     * @param propertyName the name of a property
     * @return a label with the default name display
     */
    private static Label defaultNameLabel( String propertyName ) {
	return new Label( propertyName + ":     " );
    }
    
    /**
     * Provides the user with the ability to configure a string property
     */
    private class StringField extends ConfigurationField< String > {
	
	/**
	 * shows the name of the property
	 */
	final private Label lblName;
	
	/**
	 * shows the value of the property, and allows the user to change
	 * the value of the property by typing something in this text field
	 */
	final private TextField txtValue;
	
	/**
	 * Creates a default string field that allows the user to configure
	 * a text-based property
	 * 
	 * @param p the property the user can configure
	 * @param onChange what to do when the user modifies the property
	 */
	public StringField( StringProperty p , Runnable onChange ) {
	    super( p , onChange );
	    this.lblName = defaultNameLabel( p.getName() );
	    this.txtValue = new TextField( "         " );
	    this.txtValue.textProperty().bindBidirectional( p );
	    this.getChildren().addAll( this.lblName , this.txtValue );
	}
    }
    
    /**
     * Provides the user with the ability to configure a boolean property
     */
    private class BooleanField extends ConfigurationField< Boolean > {
	
	/**
	 * shows the name of the property
	 */
	final private Label lblName;
	
	/**
	 * allows the user to configure the property by toggling it with this
	 * check box
	 */
	final private CheckBox chkValue;
	
	/**
	 * Creates a default boolean field that allows the user to configure
	 * a boolean property 
	 * 
	 * @param p the property the user can configure
	 * @param onChange what to do when the property changes
	 */
	public BooleanField( BooleanProperty p , Runnable onChange ) {
	    super( p , onChange );
	    this.lblName = defaultNameLabel( p.getName() );
	    this.chkValue = new CheckBox();
	    this.chkValue.selectedProperty().bindBidirectional( p );
	    
	    this.getChildren().addAll( this.lblName , this.chkValue );
	}
    }
    
    private class ListField extends ConfigurationField< String > {

	final private Label lblName;
	final private ComboBox< String > cboList;
	
	public ListField( Property<String> property , Runnable onPropertyChanged ) {
	    super( property , onPropertyChanged );
	    this.lblName = defaultNameLabel( property.getName() );
	    this.cboList = new ComboBox< String >();
	    this.cboList.valueProperty().bindBidirectional( property );
	    this.cboList.setEditable( false );
	    this.getChildren().addAll( this.lblName , this.cboList );
	}
	
	public ListField( Property< String > p , Runnable onPropertyChanged , String... options ) {
	    this( p , onPropertyChanged );
	    addListOptions( options );
	}
	
	final public void addListOptions( String... options ) {
	    this.cboList.getItems().addAll( options );
	}
	
    }
}
