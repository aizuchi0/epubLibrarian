
#!/bin/bash
#This script builds a .pkg OSX installer.
fpm -s tar -t osxpkg -n epubLibrarian \
   -v 1.1.20191108122618 \
   --osxpkg-identifier-prefix "xyz.aizuchi.EpubLibrarian" \
   --prefix /opt/epubLibrarian \
   "/Users/dgcrawfo/NetBeansProjects/epubLibrarian/packaging/epubLibrarian-1.1.20191108122618.tgz"
            
        