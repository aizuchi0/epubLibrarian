/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
module epubLibrarian {
    requires java.logging;
    requires org.slf4j;
    requires nl.siegmann.epublib.core;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    exports xyz.aizuchi;
    opens xyz.aizuchi to javafx.fxml;
}
