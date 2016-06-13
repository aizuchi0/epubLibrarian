/*
 * Copyright (C) 2016 Daniel Crawford <daniel-crawford@uiowa.edu>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.boldlygoingnowhere.ultron;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class epubRename extends Application {

    ImageView imageView;
    StackPane contentPane;
    BorderPane layout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drag and Drop");
        layout = new BorderPane();
        contentPane = new StackPane();
        Scene scene = new Scene(layout, 400, 400, Color.WHITE);

        contentPane.setOnDragOver((Event event) -> {
            mouseDragOver((DragEvent) event);
        });

        contentPane.setOnDragDropped((Event event) -> {
            mouseDragDropped((DragEvent) event);
        });

        contentPane.setOnDragExited((Event event) -> {
            contentPane.setStyle("-fx-border-color: #C6C6C6;");
        });

        layout.setCenter(contentPane);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    void addImage(Image i, StackPane pane) {

        imageView = new ImageView();
        imageView.setImage(i);
        pane.getChildren().add(imageView);
    }

    private void mouseDragDropped(final DragEvent e) {
        final Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            // Only get the first file from the list
            final File file = db.getFiles().get(0);
            Platform.runLater(() -> {
                System.out.println(file.getAbsolutePath());
                try {
                    if (!contentPane.getChildren().isEmpty()) {
                        contentPane.getChildren().remove(0);
                    }
                    Image img = new Image(new FileInputStream(file.getAbsolutePath()));

                    addImage(img, contentPane);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(epubRename.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        e.setDropCompleted(success);
        e.consume();
    }

    private void mouseDragOver(final DragEvent e) {
        final Dragboard db = e.getDragboard();

        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".png")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpg");

        if (db.hasFiles()) {
            if (isAccepted) {
                contentPane.setStyle("-fx-border-color: red;"
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
