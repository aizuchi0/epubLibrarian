/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
module nl.siegmann.epublib.core {
    requires org.slf4j;
    requires java.xml;
    requires org.xmlpull.v1;
    requires java.desktop;
    exports nl.siegmann.epublib.domain;
    exports nl.siegmann.epublib.browsersupport;
    exports nl.siegmann.epublib.epub;
    exports nl.siegmann.epublib.service;
    exports nl.siegmann.epublib.util;
    exports nl.siegmann.epublib.utilities;
}
