/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.io.IOException;
import java.util.ArrayList;
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
import org.diirt.service.Service;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethod.DataDescription;
import org.diirt.service.ServiceRegistry;

/**
 *
 * @author asbarber
 */
public final class ServiceViewer extends HBox {

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
                    .map(name -> new ServiceBrowserItem(ServiceRegistry.getDefault().findService(name)))
                    .collect(Collectors.toList());
        }

    }

    public static class ServiceBrowserItem implements BrowserItem {
        private final Service service;

        public ServiceBrowserItem(Service service) {
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
                    .map((f) -> new ServiceMethodBrowserItem(f.getValue()))
                    .collect(Collectors.toList());
        }

    }

    public static class ServiceMethodBrowserItem implements BrowserItem {
        private final ServiceMethod method;

        public ServiceMethodBrowserItem(ServiceMethod method) {
            this.method = method;
        }

        @Override
        public String getName() {
            return method.toString();
        }

        @Override
        public String getDescription() {
            return method.getDescription();
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public List<BrowserItem> createChildren() {
            List<BrowserItem> items = new ArrayList<>();

            items.addAll( method.getArguments().stream()
                    .map((m) -> new ServiceMethodArgumentBrowserItem(m))
                    .collect(Collectors.toList()) );

            items.addAll( method.getResults().stream()
                    .map((m) -> new ServiceMethodArgumentBrowserItem(m))
                    .collect(Collectors.toList()) );

            return items;
        }

    }

    public static class ServiceMethodArgumentBrowserItem implements BrowserItem {
        private final DataDescription parameter;

        public ServiceMethodArgumentBrowserItem(DataDescription parameter){
            this.parameter = parameter;
        }

        @Override
        public String getName() {
            return parameter.getName();
        }

        @Override
        public String getDescription() {
            return parameter.getDescription();
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
                initializedChildren = true;
                super.getChildren().setAll(getValue().createChildren().stream().map(bi -> new TreeBrowserItem(bi)).collect(Collectors.toList()));
            }
            return super.getChildren();
        }

    }


    public ServiceViewer() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("ServiceViewer.fxml"));

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
        JavaFXLaunchUtil.launch("Diirt - Service Browser", ServiceViewer.class, args);
    }
}
