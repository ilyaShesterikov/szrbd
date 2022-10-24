package com.university;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GoogleDrive extends Application {

    public Drive create() throws IOException {
        // Create Dropbox client
        Drive driveService = GoogleDriveUtils.getDriveService();

        Logger log = Logger.getLogger("thread");
        log.setLevel(Level.INFO);

        return driveService;

    }

    @Override
    public void start(Stage primaryStage) {
        try {
            /* Create a new MenuBar. */
            MenuBar menu = new MenuBar();
            /* Create new sub menus. */
            Menu menuFile = new Menu("File");
            Menu menuHelp = new Menu("Help");
            MenuItem about = new MenuItem("About");
            about.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                }
            });
            menuHelp.getItems().add(about);

            /* Adding all sub menus at ones to a MenuBar. */
            menu.getMenus().addAll(menuFile, menuHelp);


            Drive client =  GoogleDriveUtils.getDriveService();
            client.files().list();


            List<File> m = getGoogleRoot();
            SimpleFileTreeItem2 rootItem = new SimpleFileTreeItem2("root", null, client);
            rootItem.getChildren().setAll(m.stream().map(fi->new SimpleFileTreeItem2(fi.getName(), fi, client)).collect(Collectors.toList()));
            rootItem.setExpanded(true);

            TreeView<String> tree = new TreeView<String> (rootItem);


            SplitPane splitView = new SplitPane();
            splitView.getItems().add(tree);
            TextField newFolder = new TextField();
            newFolder.setText("foldername");

            tree.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
                @Override
                public TreeCell<String> call(TreeView<String> p) {
//                    return null;
                    return new TextFieldTreeCellImpl(client, newFolder, primaryStage);
                }
            });

            splitView.getItems().add(newFolder);
//            splitView.getItems().add(new HTMLEditor());

            /* Create a root node as BorderPane. */
            BorderPane root = new BorderPane();

            /* Adding the menus as well as the content pane to the root node. */
//            root.setTop(menus);
            root.setCenter(splitView);

            /*
             * Setting the root node of a scene as well as the applications CSS
             * file. CSS file has to be in same src directory as this class. The
             * path is always relative.
             */
            Scene scene = new Scene(root, 800, 600);
//            scene.getStylesheets().add(
//                    getClass().getResource("application.css").toExternalForm());

            /* Adding a scene to the stage. */
            primaryStage.setScene(scene);
            primaryStage.setTitle("FileRex");

            /* Lift the curtain :0). */
            primaryStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void buildTree(SimpleFileTreeItem item, DbxClientV2 client)  {
//        try {
//            for (Metadata childM : client.files().listFolder(item.getMetadata().getPathLower()).getEntries()) {
//                SimpleFileTreeItem childMI= new SimpleFileTreeItem (childM.getName(), childM, client);
//                item.getChildren().add(childMI);
//                buildTree(childMI, client);
//            }
//        } catch (DbxException e) {
//            e.printStackTrace();
//        }
//    }

    public static final List<File> getGoogleSubFolders(String googleFolderIdParent) throws IOException {

        Drive driveService = GoogleDriveUtils.getDriveService();

        String pageToken = null;
        List<File> list = new ArrayList<File>();

        String query = null;
        if (googleFolderIdParent == null) {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and 'root' in parents";
        } else {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and '" + googleFolderIdParent + "' in parents";
        }

        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    // Fields will be assigned values: id, name, createdTime
                    .setFields("nextPageToken, files(id, name, createdTime, parents, mimeType)")//
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }

    public static final List<File> getGoogleRoot() throws IOException {

        Drive driveService = GoogleDriveUtils.getDriveService();

        String pageToken = null;
        List<File> list = new ArrayList<File>();

        String query = "'0AIThBugeFsR8Uk9PVA' in parents";

        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive")
                    .setFields("nextPageToken, files(id, name, createdTime, mimeType, parents)")
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private final class TextFieldTreeCellImpl extends TreeCell<String> {

        private TextField textField;
        private ContextMenu addMenu = new ContextMenu();
        private TextField newFolder;

        public TextFieldTreeCellImpl(Drive client, TextField newFolder, Stage primaryStage) {
            MenuItem uploadFile = new MenuItem("Upload File");
            uploadFile.setOnAction((EventHandler) t -> {
                java.io.File file = new FileChooser().showOpenDialog(primaryStage);
                SimpleFileTreeItem2 newEmployee =
                        null;
                try {
                    File fileMetadata = new File();
                    fileMetadata.setName(file.getName());

                    List<String> parents = Arrays.asList(((SimpleFileTreeItem2)getTreeItem()).getFile().getId());
                    fileMetadata.setParents(parents);
                    //
                    Drive driveService = GoogleDriveUtils.getDriveService();

                    File googleFile = driveService.files().create(fileMetadata, new FileContent(Files.probeContentType(file.toPath()), file))
                            .setFields("id, webContentLink, webViewLink, parents, mimeType").execute();
                    newEmployee = new SimpleFileTreeItem2(file.getName(), googleFile, client);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                getTreeItem().getChildren().add(newEmployee);
            });

            MenuItem createFolder = new MenuItem("Create Folder");
            createFolder.setOnAction((EventHandler) t -> {
                SimpleFileTreeItem2 newEmployee =
                        null;
                try {
                    File fileMetadata = new File();

                    fileMetadata.setName(newFolder.getText());
                    fileMetadata.setMimeType("application/vnd.google-apps.folder");

                    String parentId = ((SimpleFileTreeItem2)getTreeItem()).getFile().getId();
                    if (parentId != null) {
                        List<String> parents = Arrays.asList(parentId);

                        fileMetadata.setParents(parents);
                    }

                    // Create a Folder.
                    // Returns File object with id & name fields will be assigned values
                    File file = client.files().create(fileMetadata).setFields("id, name, mimeType, parents").execute();

                    newEmployee = new SimpleFileTreeItem2(newFolder.getText(), file, client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getTreeItem().getChildren().add(newEmployee);
            });

            MenuItem deleteFolder = new MenuItem("Delete");
            deleteFolder.setOnAction((EventHandler) t -> {
                try {
                    client.files().delete(((SimpleFileTreeItem2)getTreeItem()).getFile().getId()).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TreeItem c = getTreeView().getSelectionModel().getSelectedItem();
                boolean remove = c.getParent().getChildren().remove(c);
            });

            MenuItem rename = new MenuItem("Rename");
            rename.setOnAction((EventHandler) t -> {
                SimpleFileTreeItem2 newEmployee =
                        null;
                File file = null;
                try {
                    file = client.files().update(((SimpleFileTreeItem2)getTreeItem()).getFile().getId(), new File().setName(newFolder.getText())).setFields("id, name, mimeType, parents").execute();

                    newEmployee = new SimpleFileTreeItem2(newFolder.getText(), file, client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            addMenu.getItems().add(createFolder);
            addMenu.getItems().add(uploadFile);
            addMenu.getItems().add(deleteFolder);
            addMenu.getItems().add(rename);

        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText((String) getItem());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                    if (
                            !getTreeItem().isLeaf()&&getTreeItem().getParent()!= null
                    ){
                        setContextMenu(addMenu);
                    }
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(textField.getText());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });

        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}
