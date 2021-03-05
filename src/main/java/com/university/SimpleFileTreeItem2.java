package com.university;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.IOException;

public class SimpleFileTreeItem2 extends TreeItem<String> {
    File file;
    Drive client;

    public SimpleFileTreeItem2(String f, File file, Drive client) {
        super(f);
        this.file = file;
        this.client = client;
    }

    @Override
    public ObservableList<TreeItem<String>> getChildren() {
        if (isFirstTimeChildren && this.file != null) {
            isFirstTimeChildren = false;

            try {
                ObservableList<SimpleFileTreeItem2> children = FXCollections
                        .observableArrayList();
                String query = "'" +this.file.getId() + "' in parents";
                client.files().list().setQ(query).setSpaces("drive")
                        // Fields will be assigned values: id, name, createdTime
                        .setFields("nextPageToken, files(id, name, createdTime, parents, mimeType)")//
                        .setPageToken(null).execute().getFiles().stream().forEach(fi->children.add(new SimpleFileTreeItem2(fi.getName(),fi,client)));
                super.getChildren().setAll(children);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        if (isFirstTimeLeaf && this.file != null) {
            isFirstTimeLeaf = false;
            isLeaf = !"application/vnd.google-apps.folder".equals(this.file.getMimeType());
        }

        return isLeaf;
    }

    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
