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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.epub.EpubReader;
import static org.boldlygoingnowhere.ultron.EpubRename.WOODY;

/**
 * FXML Controller class
 *
 * @author Daniel Crawford <daniel-crawford@uiowa.edu>
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private StackPane contentPane;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void dragExited(DragEvent event) {
                    contentPane.setStyle("-fx-border-color: #C6C6C6;");
    }

    @FXML
    private void dragOver(final DragEvent e) {
        final Dragboard db = e.getDragboard();

        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase(Locale.getDefault()).endsWith(".epub");

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

    @FXML
    private void dragDropped(final DragEvent e) {
        final Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            db.getFiles().forEach(q -> {
                Platform.runLater(() -> {
                    WOODY.log(Level.FINE, q.getAbsolutePath());
                    Path destination = null;
                    Metadata md = getMetadata(q);
                    try {
                        destination = Files.createDirectories(new File(System.getProperty("user.home")
                                + File.separator + "Books" + File.separator
                                + md.getAuthors().get(0).toString().substring(0, 1).toUpperCase() + File.separator
                                + md.getAuthors().get(0)).toPath());
                    } catch (IOException ex) {
                        WOODY.log(Level.SEVERE, null, ex);
                    }
                    q.renameTo(new File(destination + File.separator +
                            md.getAuthors().get(0) + " - " +
                            md.getTitles().get(0) + ".epub"));
                });
            }
            );
        }
        e.setDropCompleted(success);
        e.consume();
    }

    protected Metadata getMetadata(File epubFile) {
        EpubReader reader = new EpubReader();
        InputStream epubStream = null;
        Book theBook = null;
        try {
            epubStream = new FileInputStream(epubFile);
            theBook = reader.readEpub(epubStream);
        } catch (FileNotFoundException ex) {
            EpubRename.WOODY.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            EpubRename.WOODY.log(Level.SEVERE, null, ex);
        } finally {
            try {
                assert epubStream != null;
                epubStream.close();
            } catch (IOException ex) {
                EpubRename.WOODY.log(Level.SEVERE, null, ex);
            }
        }
        assert theBook != null;
        return theBook.getMetadata();
    }
    
}
