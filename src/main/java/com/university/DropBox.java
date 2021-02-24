package com.university;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.users.FullAccount;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBox extends Application {
    private static final String ACCESS_TOKEN = "qgrGbKCgawQAAAAAAAAAASawMYHRgRrDgxkqRdc_KgQVKiCPvxgquvym22KjL6Sh";

    public DbxClientV2 create() throws DbxException, IOException {
        // Create Dropbox client
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        // Get current account info
        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());

        // Get files and folder metadata from Dropbox root directory
//        ListFolderResult result = client.files().listFolder("");

        ListFolderBuilder listFolderBuilder = client.files().listFolderBuilder("/folder");
        ListFolderResult result = listFolderBuilder.withRecursive(true).start();

        Logger log = Logger.getLogger("thread");
        log.setLevel(Level.INFO);

        return client;

//        while (true) {
//
//            if (result != null) {
//                for ( Metadata entry : result.getEntries()) {
//                    TreeView<Metadata> fileView = new TreeView<Metadata>(
//                            new SimpleFileTreeItem2(entry, client));
////                    if (entry instanceof FileMetadata){
////                        log.info("Added file: "+entry.getPathLower());
////                    }
//                }
//
//                if (!result.getHasMore()) {
//                    log.info("GET LATEST CURSOR");
//                    log.info(result.getCursor());
//                }
//
//                try {
//                    result = client.files().listFolderContinue(result.getCursor());
//                } catch (DbxException e) {
//                    log.info ("Couldn't get listFolderContinue");
//                }
//            }
//        }

//        while (true) {
//            for (Metadata metadata : result.getEntries()) {
//                System.out.println(metadata.getPathLower());
//            }
//
//            if (!result.getHasMore()) {
//                break;
//            }
//
//            result = client.files().listFolderContinue(result.getCursor());
//        }
//
//        // Upload "test.txt" to Dropboxf
//        try (InputStream in = new FileInputStream("test.txt")) {
//            FileMetadata metadata = client.files().uploadBuilder("/test.txt")
//                    .uploadAndFinish(in);
//        }
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
                    /*
                     * Implement dialog to be prompted when users asks for
                     * details.
                     */
                }
            });
            menuHelp.getItems().add(about);

            /* Adding all sub menus at ones to a MenuBar. */
            menu.getMenus().addAll(menuFile, menuHelp);

//            /* Create a button. */
//            Button btn = new Button();
//            btn.setText("Say Hello World.");
//            btn.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    System.out.println("Hello World!");
//                }
//            });
//
//            /* Create a new ToolBar. */
//            ToolBar tools = new ToolBar(btn);
//
//            /* Create a new VBox and add the menu as well as the tools. */
//            VBox menus = new VBox();
//            menus.getChildren().addAll(menu, tools);

            /*
             * Adding a TreeView to the very left of the application window.
             */

            DbxClientV2 client = create();
            client.files().listFolder("").getEntries();

            /* Creating a SplitPane and adding the fileView. */

            Metadata m = client.files().listFolder("").getEntries().get(0);
            SimpleFileTreeItem rootItem = new SimpleFileTreeItem (m.getName(), m, client);
            rootItem.setExpanded(true);
//            buildTree(rootItem, client);

            TreeView<String> tree = new TreeView<String> (rootItem);


            SplitPane splitView = new SplitPane();
            splitView.getItems().add(tree);
            TextField newFolder = new TextField();
            newFolder.setText("foldername");

            tree.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
                @Override
                public TreeCell<String> call(TreeView<String> p) {
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

    public void buildTree(SimpleFileTreeItem item, DbxClientV2 client)  {
        try {
            for (Metadata childM : client.files().listFolder(item.getMetadata().getPathLower()).getEntries()) {
                SimpleFileTreeItem childMI= new SimpleFileTreeItem (childM.getName(), childM, client);
                item.getChildren().add(childMI);
                buildTree(childMI, client);
            }
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }

        public static void main(String[] args) {
        launch(args);
    }

    private final class TextFieldTreeCellImpl extends TreeCell<String> {

        private TextField textField;
        private ContextMenu addMenu = new ContextMenu();
        private TextField newFolder;

        public TextFieldTreeCellImpl(DbxClientV2 client, TextField newFolder, Stage primaryStage) {
            MenuItem uploadFile = new MenuItem("Upload File");
            uploadFile.setOnAction((EventHandler) t -> {
                File file = new FileChooser().showOpenDialog(primaryStage);
                SimpleFileTreeItem newEmployee =
                        null;
                try {
                    newEmployee = new SimpleFileTreeItem(file.getName(), client.files().uploadBuilder(((SimpleFileTreeItem)getTreeItem()).getMetadata().getPathLower() + "/" + file.getName()).withMode(WriteMode.OVERWRITE).uploadAndFinish(new FileInputStream(file)), client);
                } catch (DbxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                getTreeItem().getChildren().add(newEmployee);
            });

            MenuItem createFolder = new MenuItem("Create Folder");
            createFolder.setOnAction((EventHandler) t -> {
                SimpleFileTreeItem newEmployee =
                        null;
                try {
                    newEmployee = new SimpleFileTreeItem(newFolder.getText(), client.files().createFolderV2(((SimpleFileTreeItem)getTreeItem()).getMetadata().getPathLower() + "/" + newFolder.getText()).getMetadata(), client);
                } catch (DbxException e) {
                    e.printStackTrace();
                }
                getTreeItem().getChildren().add(newEmployee);
            });

            MenuItem deleteFolder = new MenuItem("Delete");
            deleteFolder.setOnAction((EventHandler) t -> {
                SimpleFileTreeItem newEmployee =
                        null;
                try {
                    newEmployee = new SimpleFileTreeItem(newFolder.getText(), client.files().deleteV2(((SimpleFileTreeItem)getTreeItem()).getMetadata().getPathLower()).getMetadata(), client);
                } catch (DbxException e) {
                    e.printStackTrace();
                }
                TreeItem c = (TreeItem)getTreeView().getSelectionModel().getSelectedItem();
                boolean remove = c.getParent().getChildren().remove(c);
            });

            addMenu.getItems().add(createFolder);
            addMenu.getItems().add(uploadFile);
            addMenu.getItems().add(deleteFolder);

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
