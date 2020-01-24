#!/bin/bash -ex
export APP_NAME="epubLibrarian"
export APP_ZIP="epubLibrarian-1.1.20191108122618-unsigned.zip"

/usr/local/bin/platypus \
    --app-version "1.1.20191108122618" \
    --interface-type "None" \
    --author "dgcrawfo" \
    --bundle-identifier "xyz.aizuchi.EpubLibrarian" \
    --background \
    --quit-after-execution \
    --app-icon epubLibrarian.icns \
    --bundled-file 'dist/jlink/epubLibrarian' \
    --overwrite antBuildScripts/epubLibrarian.platypus "/Users/dgcrawfo/NetBeansProjects/epubLibrarian/packaging/epubLibrarian"
    
/usr/libexec/PlistBuddy -c "Add CFBundlePackageType string APPL" "/Users/dgcrawfo/NetBeansProjects/epubLibrarian/packaging/epubLibrarian.app/Contents/Info.plist"
/usr/bin/ditto -c -k --keepParent "/Users/dgcrawfo/NetBeansProjects/epubLibrarian/packaging/epubLibrarian.app" "/Users/dgcrawfo/NetBeansProjects/epubLibrarian/packaging/epubLibrarian.zip"
        