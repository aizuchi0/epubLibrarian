
#!/bin/bash

fpm -s tar -t deb -n epubLibrarian \
   -v 1.1.20191108122618 \
   --prefix /opt/epubLibrarian \
   --deb-no-default-config-files --no-auto-depends \
   --before-install dist/jlink/epubLibrarian/conf/epubLibrarian.preinst \
   --after-install dist/jlink/epubLibrarian/conf/epubLibrarian.postinst \
   --before-remove dist/jlink/epubLibrarian/conf/epubLibrarian.preremove \
   --after-upgrade dist/jlink/epubLibrarian/conf/epubLibrarian.postupgrade \
   /Users/dgcrawfo/NetBeansProjects/epubLibrarian/packaging/epubLibrarian-1.1.20191108122618.tgz
        