package com.university;

import java.io.File;
import java.util.List;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.sun.corba.se.impl.orbutil.ObjectUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import sun.awt.AWTAccessor;

/**
 * @author Alexander Bolte - Bolte Consulting (2010 - 2014).
 *
 *         This class shall be a simple implementation of a TreeItem for
 *         displaying a file system tree.
 *
 *         The idea for this class is taken from the Oracle API docs found at
 *         http
 *         ://docs.oracle.com/javafx/2/api/javafx/scene/control/TreeItem.html.
 *
 *         Basically the file sytsem will only be inspected once. If it changes
 *         during runtime the whole tree would have to be rebuild. Event
 *         handling is not provided in this implementation.
 */
public class SimpleFileTreeItem extends TreeItem<String> {
    Metadata metadata;
    DbxClientV2 client;


    /**
     * Calling the constructor of super class in oder to create a new
     * TreeItem<File>.
     *
     * @param f
     *            an object of type File from which a tree should be build or
     *            which children should be gotten.
     */
    public SimpleFileTreeItem(String f, Metadata metadata, DbxClientV2 client) {
        super(f);
        this.metadata = metadata;
        this.client = client;
    }

    /*
     * (non-Javadoc)
     *
     * @see javafx.scene.control.TreeItem#getChildren()
     */
    @Override
    public ObservableList<TreeItem<String>> getChildren() {
        if (isFirstTimeChildren) {
            isFirstTimeChildren = false;

            /*
             * First getChildren() call, so we actually go off and determine the
             * children of the File contained in this TreeItem.
             */
            try {
                super.getChildren().setAll(buildChildren(this));
            } catch (DbxException e) {
                e.printStackTrace();
            }
        }
        return super.getChildren();
    }

    /*
     * (non-Javadoc)
     *
     * @see javafx.scene.control.TreeItem#isLeaf()
     */
    @Override
    public boolean isLeaf() {
        if (isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            isLeaf = this.getMetadata() instanceof FileMetadata;
        }

        return isLeaf;
    }

    /**
     * Returning a collection of type ObservableList containing TreeItems, which
     * represent all children available in handed TreeItem.
     *
     * @param TreeItem
     *            the root node from which children a collection of TreeItem
     *            should be created.
     * @return an ObservableList<TreeItem<File>> containing TreeItems, which
     *         represent all children available in handed TreeItem. If the
     *         handed TreeItem is a leaf, an empty list is returned.
     */
    private ObservableList<SimpleFileTreeItem> buildChildren(TreeItem<String> TreeItem) throws DbxException {

        if (this.metadata != null && this.metadata instanceof FolderMetadata) {
//             if (treeItem.isRoot == null || !treeItem.isRoot) {
            List<Metadata> files = this.client.files().listFolderBuilder(this.metadata.getPathLower()).start().getEntries();
            if (files != null) {
                ObservableList<SimpleFileTreeItem> children = FXCollections
                        .observableArrayList();

                for (Metadata childFile : files) {
                    children.add(new SimpleFileTreeItem(childFile.getName(), childFile, client));
                }

                return children;
            }
//            } else {return treeItem.getChildren();}
        }

        return FXCollections.emptyObservableList();
    }

    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
