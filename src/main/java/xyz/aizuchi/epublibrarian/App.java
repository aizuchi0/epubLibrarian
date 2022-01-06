package xyz.aizuchi.epublibrarian;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * JavaFX App
 */
public class App extends Application {

    static AnchorPane ap;
    Metadata md;

    @Override
    public void start(Stage stage) {

        Label label = new Label("Drag epubs here for sorting into your Library");
        label.setAlignment(Pos.CENTER);
        Color c = Color.web("#9f9f9f");
        label.setTextFill(c);
        label.setLayoutX(130.0);
        label.setLayoutY(163.0);
        label.setStyle("&#10;");
        label.setWrapText(false);
        label.setFont(new Font(24.0));

        MenuBar mb = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem destDir = new MenuItem("Destination directory…");
        MenuItem quitItem = new MenuItem("Quit");
        fileMenu.getItems().add(destDir);
        fileMenu.getItems().add(new SeparatorMenuItem());
        fileMenu.getItems().add(quitItem);
        quitItem.setOnAction(new ExitAction());
        mb.getMenus().add(fileMenu);
        ap = new AnchorPane(label);
        AnchorPane.setLeftAnchor(ap, 163.0);
        AnchorPane.setRightAnchor(ap, 97.0);
        ap.setId("contentPane");
        ap.setOnDragDropped(new dragDropped());
        ap.setOnDragOver(new dragOver());
        ap.setOnDragExited(new dragExited());
        VBox vBox = new VBox(mb, ap);
        Scene scene = new Scene(vBox, 640, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    protected Metadata getMetadata(File epubFile) {
        EpubReader reader = new EpubReader();
        InputStream epubStream = null;
        Book theBook = null;
        try {
            epubStream = new FileInputStream(epubFile);
            theBook = reader.readEpub(epubStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                assert epubStream != null;
                epubStream.close();
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        assert theBook != null;
        return theBook.getMetadata();
    }

    private static class dragOver implements EventHandler<DragEvent> {

        public dragOver() {
        }

        @Override
        public void handle(DragEvent e) {
            final Dragboard db = e.getDragboard();

            final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase(Locale.getDefault()).endsWith(".epub");

            if (db.hasFiles()) {
                if (isAccepted) {
                    ap.setStyle("-fx-border-color: red;"
                            + "-fx-border-width: 5;"
                            + "-fx-background-color: #C6C6C6;"
                            + "-fx-border-style: solid;");
                    e.acceptTransferModes(TransferMode.COPY);
                }
            } else {
                e.consume();
            }
        }
    }

    private static class dragExited implements EventHandler<DragEvent> {

        public dragExited() {
        }

        @Override
        public void handle(DragEvent e) {
            ap.setStyle("-fx-border-color: #C6C6C6;");
        }
    }

    private static class ExitAction implements EventHandler<ActionEvent> {

        public ExitAction() {
        }

        @Override
        public void handle(ActionEvent t) {
            Platform.exit();
        }
    }

    private class dragDropped implements EventHandler<DragEvent> {

        public dragDropped() {
        }

        @Override
        public void handle(DragEvent e) {
            final Dragboard db = e.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                db.getFiles().forEach(q -> {
                    Platform.runLater(() -> {
                        Logger.getLogger(App.class.getName()).log(Level.FINE, q.getAbsolutePath());
                        Path destination = null;
                        md = getMetadata(q);
                        try {
                            destination = Files.createDirectories(new File(System.getProperty("user.home")
                                    + File.separator + "Library" + File.separator + "Mobile Documents"
                                    + File.separator + "com~apple~CloudDocs"
                                    + File.separator + "Books" + File.separator
                                    + md.getAuthors().get(0).toString().substring(0, 1).toUpperCase() + File.separator
                                    + md.getAuthors().get(0)).toPath());
                        } catch (IOException ex) {
                            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        q.renameTo(new File(destination + File.separator
                                + md.getAuthors().get(0) + " - "
                                + md.getTitles().get(0) + ".epub"));
                    });
                }
                );
            }
            e.setDropCompleted(success);
            e.consume();
        }
    }

}
