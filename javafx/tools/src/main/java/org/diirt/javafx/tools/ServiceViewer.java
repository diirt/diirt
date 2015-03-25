/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.ui.tools;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.HBox;
import org.diirt.datasource.formula.FormulaFunction;
import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FormulaFunctions;
import org.diirt.datasource.formula.FormulaRegistry;
import org.diirt.service.Service;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceRegistry;

/**
 *
 * @author asbarber
 */
public final class ServiceMethodViewer extends HBox {

    @FXML
    private TreeTableView<BrowserItem> servicesTreeTable;
    
    public static interface BrowserItem {
        public String getName();
        public String getDescription();
        public boolean isLeaf();
        public List<BrowserItem> createChildren();
    }
    
    public static class ServiceRootBrowserItem implements BrowserItem {

        @Override
        public String getName() {
            return "Name";
        }

        @Override
        public String getDescription() {
            return "Description";
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public List<BrowserItem> createChildren() {
            return ServiceRegistry.getDefault()
                    .getRegisteredServiceNames().stream().sorted()
                    .map(name -> new ServiceFunctionSetBrowserItem(ServiceRegistry.getDefault().findService(name)))
                    .collect(Collectors.toList());
        }
        
    }
    
    public static class ServiceFunctionSetBrowserItem implements BrowserItem {
        private final Service service;

        public ServiceFunctionSetBrowserItem(Service service) {
            this.service = service;
        }

        @Override
        public String getName() {
            return service.getName();
        }

        @Override
        public String getDescription() {
            return service.getDescription();
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public List<BrowserItem> createChildren() {
            return service.getServiceMethods().entrySet().stream()
                    .sorted((f1, f2) -> f1.getValue().getName().compareTo(f2.getValue().getName()))
                    .map((f) -> new ServiceFunctionDataBrowserItem(f.getValue()))
                    .collect(Collectors.toList());
        }
        
    }
    
    public static class ServiceFunctionDataBrowserItem implements BrowserItem {
        private final ServiceMethod method;

        public ServiceFunctionDataBrowserItem(ServiceMethod method) {
            this.method = method;
        }

        @Override
        public String getName() {
            return method.getName();
        }

        @Override
        public String getDescription() {
            return method.getDescription();
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public List<BrowserItem> createChildren() {
            return Collections.emptyList();
        }
        
    }
    
    public static class TreeBrowserItem extends TreeItem<BrowserItem> {
        private boolean initializedChildren = false;

        public TreeBrowserItem(BrowserItem value) {
            super(value);
        }

        @Override
        public boolean isLeaf() {
            return getValue().isLeaf();
        }

        @Override
        public ObservableList<TreeItem<BrowserItem>> getChildren() {
            if (!initializedChildren) {
                super.getChildren().setAll(getValue().createChildren().stream().map(bi -> new TreeBrowserItem(bi)).collect(Collectors.toList()));
                initializedChildren = true;
            }
            return super.getChildren();
        }
        
    }
    

    public ServiceMethodViewer() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("ServiceMethodViewer.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        TreeItem<BrowserItem> root = new TreeBrowserItem(new ServiceRootBrowserItem());
        root.setExpanded(true);
        servicesTreeTable.setRoot(root);
        servicesTreeTable.setShowRoot(false);
        servicesTreeTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        TreeTableColumn<BrowserItem,String> nameCol = new TreeTableColumn<>("Name");
        TreeTableColumn<BrowserItem,String> descriptionCol = new TreeTableColumn<>("Description");

        servicesTreeTable.getColumns().setAll(nameCol, descriptionCol);
        
        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        descriptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
    }
    
    public static void main(String[] args) {
        //JavaFXLaunchUtil.launch("Diirt - Service Browser", ServiceMethodViewer.class, args);
    }    
}
