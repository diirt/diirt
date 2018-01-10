/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.graphene;

import java.util.ArrayList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.diirt.graphene.InterpolationScheme;
import org.diirt.graphene.NumberColorMap;

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
 *  <li> InterpolationScheme
 *  <li> NumberColorMap
 * </ul>
 *
 * <p>
 * Then find the appropriate method and add that property to the configuration
 * dialog. For some properties such as the interpolation scheme and number
 * color mapping, the user cannot choose anything s/he wants. Therefore, a list
 * of allowed properties is also required
 * <p>
 * For example, to allow a <code>NumberColorMap</code> property to be configured,
 * simply use the code
 * <pre>
 * <code>
 * Property&lt; NumberColorMap &gt; p = ...
 * NumberColorMap[] allowedNumberColorMaps = ...
 * configurationDialog.addNumberColorMapListProperty( p );
 * </code>
 * </pre>
 * <p>
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
    private final ArrayList< ConfigurationData > configurationData = new ArrayList< ConfigurationData >();

    /**
     * panel containing everything in this dialog
     */
    private final BorderPane pnlMain = new BorderPane();

    /**
     * panel containing the configuration fields in which the user enters
     * his/her choices
     */
    private final GridPane pnlConfigurations = new GridPane();

    /**
     * panel providing options for the user to save the current configurations
     * or cancel and revert to the previously saved configuration
     */
    private final GridPane pnlSaveCancel = new GridPane();

    /**
     * Creates a default configuration dialog with no configurations available
     */
    public ConfigurationDialog() {
        this.initStyle( StageStyle.UTILITY );
        this.setTitle( "Configure..." );
        Scene s = new Scene( pnlMain );

        pnlMain.setCenter( pnlConfigurations );
        pnlMain.setPadding( new Insets( 5 , 5 , 5 , 5 ) );

        //we assume that there will only ever be 2 columns: 1 to label the
        //property and 1 for the input method for that property
        this.pnlConfigurations.getColumnConstraints().addAll( new ColumnConstraints() , new ColumnConstraints() );
        this.pnlConfigurations.getColumnConstraints().get( 1 ).setHgrow( Priority.ALWAYS );
        this.pnlConfigurations.setVgap( 5 );
        this.pnlConfigurations.setHgap( 5 );

        FlowPane pnlBottom = new FlowPane();
        pnlBottom.setAlignment( Pos.CENTER_RIGHT );
        pnlBottom.getChildren().add( pnlSaveCancel );
        pnlMain.setBottom( pnlBottom );

        Button cmdConfigure = new Button( "Accept" );
        cmdConfigure.setMinSize( 0 , 0 );
        cmdConfigure.setMaxSize( Double.MAX_VALUE , Double.MAX_VALUE );
        cmdConfigure.setOnAction( new EventHandler< ActionEvent >() {

            @Override
            public void handle(ActionEvent event) {
                saveChanges();
                ConfigurationDialog.this.hide();
            }
        });

        Button cmdRevert = new Button( "Revert" );
        cmdRevert.setMinSize( 0 , 0 );
        cmdRevert.setMaxSize( Double.MAX_VALUE , Double.MAX_VALUE );
        cmdRevert.setOnAction( new EventHandler< ActionEvent >() {

            @Override
            public void handle(ActionEvent event) {
                cancelChanges();
            }
        });

        Button cmdCancel = new Button( "Cancel" );
        cmdCancel.setMinSize( 0 , 0 );
        cmdCancel.setMaxSize( Double.MAX_VALUE , Double.MAX_VALUE );
        cmdCancel.setOnAction( new EventHandler< ActionEvent >() {

            @Override
            public void handle( ActionEvent event ) {
                ConfigurationDialog.this.hide();
            }
        });


        this.setOnHidden( new EventHandler< WindowEvent >() {

            @Override
            public void handle(WindowEvent event) {
                cancelChanges();
            }

        });

        pnlSaveCancel.setHgap( 5 );
        pnlSaveCancel.setVgap( 5 );
        pnlSaveCancel.add( cmdConfigure , 0 , 0 );
        pnlSaveCancel.add( cmdRevert , 1 , 0 );
        pnlSaveCancel.add( cmdCancel , 2 , 0 );

        pnlSaveCancel.getColumnConstraints().addAll( new ColumnConstraints() , new ColumnConstraints() , new ColumnConstraints() );
        pnlSaveCancel.getColumnConstraints().get( 0 ).setHgrow( Priority.ALWAYS );
        pnlSaveCancel.getColumnConstraints().get( 0 ).setHalignment( HPos.RIGHT );
        pnlSaveCancel.getColumnConstraints().get( 1 ).setHgrow( Priority.ALWAYS );
        pnlSaveCancel.getColumnConstraints().get( 1 ).setHalignment( HPos.RIGHT );
        pnlSaveCancel.getColumnConstraints().get( 2 ).setHgrow( Priority.ALWAYS );
        pnlSaveCancel.getColumnConstraints().get( 2 ).setHalignment( HPos.RIGHT );

        pnlSaveCancel.setPadding( new Insets( 5 , 0 , 0 , 0 ) );
        this.setScene( s );
    }

    /**
     * Adds a property field to this configuration dialog. The graphical components
     * are displayed in the dialog box, and the configuration data will be modified
     * as the user changes the property.
     *
     * @param graphicalComponents the graphical components of which the property field consists
     * @param data the data related to the property field to be added
     */
    private void addPropertyField( Node[] graphicalComponents , ConfigurationData data ) {
        for ( int i=0 ; i<graphicalComponents.length ; i++ ) {
            this.pnlConfigurations.add( graphicalComponents[ i ] , i , this.configurationData.size() );
        }
        this.configurationData.add( data );
    }

    /**
     * Adds the given string property as something the user may configure. In the dialog,
     * this property will have a label containing the name of the property and a
     * text field in which the user can enter his/her configuration for this
     * property.
     *
     * @param name the textual name of the property (e.g. "X Column")
     * @param p the string property that the user may modify
     */
    public void addStringProperty( String name , StringProperty p ) {
        StringField newField = new StringField( name , p );
        ConfigurationData data = new ConfigurationData( newField , new SimpleStringProperty( p.getValue() ) );
        addPropertyField( newField.getComponents() , data );
    }

    /**
     * Adds the given boolean property as something the user may configure. In the dialog,
     * this property will have a label containing the name of the property and a
     * check box which the user can use to toggle this property to true or false.
     *
     * @param name the textual name of the property (e.g. "X Column")
     * @param p the boolean property that the user may modify
     */
    public void addBooleanProperty( String name , BooleanProperty p ) {
        BooleanField newField = new BooleanField( name , p );
        ConfigurationData data = new ConfigurationData( newField , new SimpleBooleanProperty( p.getValue() ) );
        addPropertyField( newField.getComponents() , data );
    }

    /**
     * Adds the given interpolation scheme property as something the user may
     * configure. In the dialog, this property will have a label containing the
     * name of the property and a combobox which the user can use to modify
     * this property.
     *
     * @param name the textual name of the property (e.g. "X Column")
     * @param p the interpolation scheme property that the user may modify
     * @param allowedInterpolations the list of allowed interpolation schemes
     */
    public void addInterpolationSchemeListProperty( String name , Property< InterpolationScheme > p , InterpolationScheme[] allowedInterpolations ) {
        InterpolationSchemeField newField = new InterpolationSchemeField( name , p , allowedInterpolations );
        ConfigurationData data = new ConfigurationData( newField , new SimpleObjectProperty< InterpolationScheme >( p.getValue() ) );
        addPropertyField( newField.getComponents() , data );
    }

    /**
     * Adds the given number-color mapping property as something the user may
     * configure. In the dialog, this property will have a label containing the
     * name of the property and a combobox which the user can use to modify
     * this property.
     *
     * @param name the textual name of the property (e.g. "X Column")
     * @param p the number-color mapping property that the user may modify
     * @param allowedMappings the list of allowed number-color mappings
     */
    public void addNumberColorMapListProperty( String name , Property< NumberColorMap > p , NumberColorMap[] allowedMappings ) {
        NumberColorMapField newField = new NumberColorMapField( name , p , allowedMappings );
        ConfigurationData data = new ConfigurationData( newField , new SimpleObjectProperty< NumberColorMap >( p.getValue() ) );
        addPropertyField( newField.getComponents() , data );
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
     * Provides the user with the ability to configure one property.
     * <p>
     * Note: Fields may only have 2 components: a label and some sort of input method, e.g.
     * a combobox or text field. This is necessary to maintain an appealing look
     * for the dialog box
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
        public ConfigurationField( Property< T > property ) {
            this.m_property = property;
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

        public Node[] getComponents() {
            return new Node[]{};
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
        return new Label( propertyName + ":" );
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
         * @param name the textual name of the property (e.g. "X Column")
         * @param p the property the user can configure
         */
        public StringField( String name , StringProperty p ) {
            super( p );
            this.lblName = defaultNameLabel( name );
            this.txtValue = new TextField( "         " );
            this.txtValue.textProperty().bindBidirectional( p );
            this.txtValue.setText( p.getValue() );
        }

        @Override
        public Node[] getComponents() {
            return new Node[]{ this.lblName , this.txtValue };
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
         * @param name the textual name of the property (e.g. "X Column")
         * @param p the property the user can configure
         */
        public BooleanField( String name , BooleanProperty p ) {
            super( p );
            this.lblName = defaultNameLabel( name );
            this.chkValue = new CheckBox();
            this.chkValue.selectedProperty().bindBidirectional( p );
            this.chkValue.setSelected( p.getValue() );
            this.chkValue.setMaxWidth( Double.MAX_VALUE );
        }

        @Override
        public Node[] getComponents() {
            return new Node[]{ this.lblName , this.chkValue };
        }
    }

    /**
     * Provides the user with the ability to select an InterpolationScheme
     */
    private class InterpolationSchemeField extends ConfigurationField< InterpolationScheme > {

        final private Label lblName;
        final private ComboBox< InterpolationScheme > cboInterpolations;

        /**
         * Creates an InterpolationSchemeField that allows the user to configure
         * an interpolation scheme property by selecting an interpolation scheme
         * from a list of allowed interpolation schemes.
         *
         * @param name the textual name of the property (e.g. "X Column")
         * @param p the interpolation scheme property the user can configure
         * @param interpolationSchemes the list of allowed interpolation schemes,
         * which must be nonempty
         * @throws IllegalArgumentException if the list of allowed interpolation
         * schemes is empty
         */
        public InterpolationSchemeField( String name , Property<InterpolationScheme> p , InterpolationScheme[] interpolationSchemes ) {
            super( p );
            if ( interpolationSchemes.length == 0 ) {
                throw new IllegalArgumentException( "Must have at least 1 allowed interpolation scheme." );
            }
            this.lblName = defaultNameLabel( name );
            this.cboInterpolations = new ComboBox< InterpolationScheme >();
            this.cboInterpolations.getItems().addAll( interpolationSchemes );
            this.cboInterpolations.valueProperty().bindBidirectional( p );
            this.cboInterpolations.setValue( p.getValue() );
            this.cboInterpolations.setMaxWidth( Double.MAX_VALUE );
        }

        @Override
        public Node[] getComponents() {
            return new Node[]{this.lblName , this.cboInterpolations};
        }
    }

    /**
     * Provides the user with the ability to select a NumberColorMap property
     */
    private class NumberColorMapField extends ConfigurationField< NumberColorMap > {

        final private Label lblName;
        final private ComboBox< NumberColorMap > cboColorMaps = new ComboBox< NumberColorMap >();

        /**
         * Creates a NumberColorMapField that allows the user to configure a
         * number-color mapping by selecting one from a list of allowed
         * mappings
         *
         * @param name the textual name of the property (e.g. "X Column")
         * @param p the property the user can configure
         * @param maps the allowed mappings the user can select
         * @throws IllegalArugmentException if there are no allowed mapping from which the user can select
         */
        public NumberColorMapField( String name , Property< NumberColorMap > p , NumberColorMap[] maps ) {
            super( p );
            if ( maps.length == 0 ) {
                throw new IllegalArgumentException( "Must have at least 1 allowed number-color mapping" );
            }
            this.lblName = defaultNameLabel( name );
            this.cboColorMaps.getItems().addAll( maps );
            this.cboColorMaps.valueProperty().bindBidirectional( p );
            this.cboColorMaps.setValue( p.getValue() );
            this.cboColorMaps.setMaxWidth( Double.MAX_VALUE );
        }

        @Override
        public Node[] getComponents() {
            return new Node[]{ this.lblName , this.cboColorMaps };
        }
    }
}
