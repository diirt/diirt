/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

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

public final class FormulaFunctionViewer extends HBox {

    @FXML
    private TreeTableView<BrowserItem> functionsTreeTable;

    public static interface BrowserItem {
        public String getName();
        public String getDescription();
        public boolean isLeaf();
        public List<BrowserItem> createChildren();
    }

    public static class FormulaFunctionRootBrowserItem implements BrowserItem {

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
            return FormulaRegistry.getDefault()
                    .listFunctionSets().stream().sorted()
                    .map(name -> new FormulaFunctionSetBrowserItem(FormulaRegistry.getDefault().findFunctionSet(name)))
                    .collect(Collectors.toList());
        }

    }

    public static class FormulaFunctionSetBrowserItem implements BrowserItem {
        private final FormulaFunctionSet set;

        public FormulaFunctionSetBrowserItem(FormulaFunctionSet set) {
            this.set = set;
        }

        @Override
        public String getName() {
            return set.getName();
        }

        @Override
        public String getDescription() {
            return set.getDescription();
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public List<BrowserItem> createChildren() {
            return set.getFunctions().stream()
                    .sorted((f1, f2) -> f1.getName().compareTo(f2.getName()))
                    .map((f) -> new FormulaFunctionDataBrowserItem(f))
                    .collect(Collectors.toList());
        }

    }

    public static class FormulaFunctionDataBrowserItem implements BrowserItem {
        private final FormulaFunction function;

        public FormulaFunctionDataBrowserItem(FormulaFunction function) {
            this.function = function;
        }

        @Override
        public String getName() {
            return FormulaFunctions.formatSignature(function);
        }

        @Override
        public String getDescription() {
            return function.getDescription();
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


    public FormulaFunctionViewer() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("FormulaFunctionViewer.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
//        functionsTreeTable = new TreeTableView<>();
//        setHgrow(functionsTreeTable, Priority.ALWAYS);
        TreeItem<BrowserItem> root = new TreeBrowserItem(new FormulaFunctionRootBrowserItem());
        root.setExpanded(true);
        functionsTreeTable.setRoot(root);
        functionsTreeTable.setShowRoot(false);
        functionsTreeTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        TreeTableColumn<BrowserItem,String> nameCol = new TreeTableColumn<>("Name");
        TreeTableColumn<BrowserItem,String> descriptionCol = new TreeTableColumn<>("Description");

        functionsTreeTable.getColumns().setAll(nameCol, descriptionCol);

        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        descriptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
    }

    public static void main(String[] args) {
        JavaFXLaunchUtil.launch("Diirt - Formula Function Browser", FormulaFunctionViewer.class, args);
    }

}
