# Winter Hearth & Glacier — Standalone Android App (Windows Build Guide)

Is project se aap apna **khud ka `.apk` app** bana sakte hain — users ko koi 3rd-party app (KWGT, Nova, Shortcut Maker) download nahi karna padega.

## Windows Laptop par APK kaise banayein:
1. Is ZIP ko extract karein (`WinterHearthGlacierApp` folder milega).
2. **Android Studio** open karein -> **Open** par click karein -> `WinterHearthGlacierApp` folder select karein.
3. Gradle sync poora hone dein (1–2 minute, internet on rakhein).
4. Upar menu mein **Build -> Generate App Bundles or APKs -> Build APK(s)** par click karein.
5. Build complete hone par neeche **locate** link par click karein — aapko `app-debug.apk` mil jayega!
6. Is `.apk` ko apne Android phone par bhejein aur install karein.

## App ke andar kya-kya built-in hai:
- **1-Tap Wallpaper**: `Winter_Hearth_Wallpaper` seedha Home & Lock screen par set hota hai.
- **Native Melting Glacier Battery Widget**: Real battery % ke hisaab se `glacier_100` se `glacier_10` tak melt hota hai.
- **10 Glacier App Shortcuts**: Phone, Messages, Camera, Music, Browser, Settings, Maps, Clock, Notes, Mail icons 1 tap mein Home Screen par pin hote hain.
- **Campfire Dynamic Island**: `DynamicHearthService.kt` notifications ko floating winter campfire pill mein dikhata hai.
