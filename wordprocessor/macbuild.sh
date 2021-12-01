jpackage --input target/ \
  --name WordProcessor \
  --main-jar WordProcessor-jar-with-dependencies.jar \
  --main-class wordprocessor.WordProcessor \
  --type dmg \
  --icon "icons.icns" \
  --app-version "1.0.0" \
  --vendor "Andrew's software" \
  --copyright "Copyright 2021" \
  --mac-package-name "Word Processor" \
 #--mac-sign \
 #--mac-bundle-identifier "MyAppName-1.2.3" \
 #--mac-bundle-name "My App Name" \
 #--mac-bundle-signing-prefix "" \
 #--mac-signing-keychain "" \
 #--mac-signing-key-user-name "" \
  --verbose \
  --java-options '--enable-preview'