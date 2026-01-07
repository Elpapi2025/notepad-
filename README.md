## GreenNote Native Android App

This directory contains a native Android application built with Kotlin and Jetpack Compose. It's a re-implementation of the Next.js web application.

### How to Run

1.  **Open in Android Studio:** Open the `android` directory as a new project in Android Studio.
2.  **Gradle Sync:** Wait for Android Studio to download all the necessary dependencies (this is called "Gradle Sync").
3.  **Run the App:** Once the sync is complete, you can run the app on an Android emulator or a physical device directly from Android Studio by clicking the 'Run' button (a green play icon).

### Project Structure

*   `app/src/main/java/com/example/greennote/`: Main source code.
    *   `data/`: Contains the data model (`Note.kt`) and the data repository.
    *   `ui/screens/`: Contains the Jetpack Compose screens (`NoteListScreen.kt`, `NoteEditScreen.kt`).
    *   `ui/theme/`: Contains the UI theme and colors.
    *   `MainActivity.kt`: The main entry point of the application.
*   `app/build.gradle.kts`: App-level build file where dependencies are declared.
*   `build.gradle.kts`: Project-level build file.
