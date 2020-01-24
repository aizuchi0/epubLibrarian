
#!/bin/bash -ex
##This builder script only works with Java jlink applications, *not* jumbojars.
/usr/local/bin/platypus \
    --app-version "1.1.20191108122618" \
    --interface-type "None" \
    --author "dgcrawfo" \
    --bundle-identifier "xyz.aizuchi.EpubLibrarian" \
    --background \
    --quit-after-execution \
    --app-icon "/Users/dgcrawfo/NetBeansProjects/epubLibrarian/epubLibrarian.icns" \
    --bundled-file 'dist/jlink/epubLibrarian' \
    --overwrite dist/jlink/epubLibrarian/bin/epubLibrarian.platypus "/Users/dgcrawfo/NetBeansProjects/epubLibrarian/epubLibrarian"
    
    /usr/libexec/PlistBuddy -c "Add CFBundlePackageType string APPL" "/Users/dgcrawfo/NetBeansProjects/epubLibrarian/epubLibrarian.app/Contents/Info.plist"

    #Codesign 
    cd /Users/dgcrawfo/NetBeansProjects/epubLibrarian
    security list-keychains -s /Users/Shared/Keychains/Jenkins.keychain
    security unlock-keychain  -p "" /Users/Shared/Keychains/Jenkins.keychain
    echo "****Sign the app****"
    codesign -f -s "Developer ID Application: Daniel Crawford (KKUXJV9FP5)" \
            --prefix "xyz.aizuchi.EpubLibrarian"  --keychain /Users/Shared/Keychains/Jenkins.keychain  \
            -vvvv --deep "/Users/dgcrawfo/NetBeansProjects/epubLibrarian/epubLibrarian.app"
    echo "****Verify the signing****"
    codesign -dv --verbose=4 "/Users/dgcrawfo/NetBeansProjects/epubLibrarian/epubLibrarian.app"
    echo "****Test the signature****"
    spctl --assess --verbose "/Users/dgcrawfo/NetBeansProjects/epubLibrarian/epubLibrarian.app"
            
    #Build Autoplot DMG
    #Set up disk image staging folder
    rm -rf staging
    mkdir staging
    ln -s /Applications staging/Applications
    cp -a "/Users/dgcrawfo/NetBeansProjects/epubLibrarian/epubLibrarian.app" staging

    ##generate raw disk image
    rm -f epubLibrarian.dmg
    hdiutil create -srcfolder staging -fs HFS+ -volname epubLibrarian -format UDRW -ov raw-epubLibrarian.dmg

    #remove working files and folders
    rm -rf staging
        
    # we now have a raw DMG file.
        
    # remount it so we can set the volume icon properly
    mkdir -p staging
    hdiutil attach raw-epubLibrarian.dmg -mountpoint staging
    SetFile -a C staging
    hdiutil detach staging
    #sleep 3
    #killall -9 diskimages-helper 
    rm -rf staging
        
    # convert the raw image
    rm -f epubLibrarian.dmg
    hdiutil convert raw-epubLibrarian.dmg -format UDZO -o epubLibrarian.dmg
    rm -f raw-epubLibrarian.dmg
    rm -rf epubLibrarian.app

        