# WINOrganizer pro Android — v1.0 MVP

Otevri v Android Studiu (Hedgehog a novejsi) > Open > slozka WINOrganizer-Android.
Sync Gradle, spust na emulatoru / telefonu (min SDK 26, cil SDK 34).

## Co je uvnitr
- Poznamky, Ukoly, Kalendar 2026-2035
- Fotograficky denik: fotka, datum+cas, tema, pocasi, GPS, objektiv, misto
- Pocasi: aktualni dle GPS (Open-Meteo) + archivni pro stare zaznamy
- Mapa: osmdroid (OpenStreetMap), piny, trasa, vzdalenost
- Statistiky: tema / pocasi / objektiv / mesic / rok / top mista
- Tmavy vzhled (Material 3 dark)
- Offline: Room databaze, zaloha / obnova JSON (SAF)

## Struktura
- Models.kt — Room entity: Folder, Note, Task, Event, PhotoEntry
- Weather.kt — Open-Meteo current + archive, mapovani kodu pocasi na cestinu
- Backup.kt — export/import JSON pres Uri
- MapScreen.kt — osmdroid mapa, piny, trasa (haversine)
- StatsScreen.kt — statistiky z PhotoEntry
- PhotoDiaryScreen.kt — seznam, detail, formular, GPS, fotka z galerie
- MainActivity.kt — navigace: Poznamky / Ukoly / Kalendar / Fotodenik

## Poznamky k produkci
- GPS vyzaduje povoleni ACCESS_FINE_LOCATION + POST_NOTIFICATIONS pro pripominky.
- Fotky se ukladaji jako cesta k souboru / URI, ne jako blob — pro velke fotky doporucujeme kompresi.
- Hesla / Kontakty z puvodniho WINOrganizeru jsou pripravene jako dalsi modul — neni v tomto MVP.
- Pred vydanim pridej proguard, podepsani APK a test na realnem zarizeni.

## Jak zkompilovat BEZ instalace - pres GitHub (doporuceno)

1. Zaloz si zdarma ucet na github.com
2. Vytvor novy repozitar, napr. `winorganizer-android` (soukromy nebo verejny)
3. Nahraj tam obsah slozky WINOrganizer-Android (vsechny soubory vcetne skryte .github/)
   - pres web: Add file > Upload files
   - nebo pres git: git init, git add ., git commit, git push
4. Na GitHubu klikni na zalozku **Actions** > **Android CI - build APK** > Run workflow
   - pri push na main se spusti automaticky
5. Po dokonceni (zelena fajfka) klikni na beh > dole **Artifacts** > stahni **WINOrganizer-debug-apk**
6. APK nahraj do telefonu a nainstaluj (povolit instalaci z neznamych zdroju)

Poznamka: v tomto balicku je zjednoduseny `gradlew` stub. Pro plnohodnotny wrapper spust jednou lokalne `gradle wrapper --gradle-version 8.5` a commitni vygenerovane soubory, nebo pouzij Android Studio, ktere wrapper dogeneruje samo.

## Jak zkompilovat BEZ instalace - pres GitHub

1. Zaloz ucet na github.com
2. Vytvor repozitar napr. winorganizer-android
3. Nahraj obsah slozky WINOrganizer-Android VETNE skryte slozky .github/ (pozor: GitHub web upload skryte soubory nekdy schova - pouzij git push, nebo v repozitari vytvor rucne .github/workflows/android.yml a vloz obsah z tohoto souboru)
4. Zalozka Actions > Android CI - build APK > Run workflow (pri push na main se spusti samo)
5. Po dokonceni stahni Artifacts > WINOrganizer-debug-apk
6. APK nahraj do telefonu a nainstaluj
