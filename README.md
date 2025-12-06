## Firebase Setup 

To run this project, you **must** connect it to your own Firebase backend. Follow these steps carefully.

### 1. Create a Firebase Project

*   Go to the [Firebase Console](https://console.firebase.google.com/).
*   Click **"Add project"** and follow the on-screen instructions to create a new project.

### 2. Add an Android App to Your Project

*   Inside your new Firebase project, click the Android icon ( `</>` ) to add an Android app.
*   **Android package name**: This is the most important step. The package name you enter here **must** match the `applicationId` in the `app/build.gradle.kts` file. For this project, use:
    *   `com.example.bookcare`
*   **App nickname** (Optional): Give your app a memorable name (e.g., "Community Forum App").
*   **Debug signing certificate SHA-1** (Optional): You can skip this for now.
*   Click **"Register app"**.

### 3. Download and Place the `google-services.json` File

*   After registering the app, Firebase will prompt you to download a `google-services.json` file.
*   Download this file.
*   In Android Studio, switch to the **Project** view to see your file directory.
*   Move the downloaded `google-services.json` file into the **`app/`** directory. The final path should be `app/google-services.json`.

### 4. Enable the Firestore Database

The app will crash if the database is not enabled. 

*   In the Firebase Console, go to the **Build** section in the left menu and click on **Firestore Database**.
*   Click the **"Create database"** button.
*   A wizard will appear. Choose to start in **Test mode**. This will set up security rules that allow the app to read and write data during development.
*   Select a location for your database and click **"Enable"**.



## How to Build and Run the Project

1.  Clone this repository to your local machine.
2.  Open the project in Android Studio.
3.  Follow **all the Firebase Setup steps** listed above.
4.  Let Android Studio sync the project with Gradle. It should do this automatically after you add the `google-services.json` file.
5.  Build and run the app on an Android emulator or a physical device.


# Community Forum 

Users can create posts, view posts from others, and interact with them by liking and commenting. The app is built with Java and uses Google Firebase's Cloud Firestore as its backend for real-time data storage and updates.

## Features

*   **View Posts**: See a real-time feed of all posts from the community.
*   **Post Types**: Categorize posts as "Discussion," "Review," or "Event."
*   **Create Posts**: A floating button allows users to create and publish new posts.
*   **Like Posts**: Users can like and unlike posts.
*   **Commenting**: Users can open a dialog to view and add comments to any post.
*   **Real-time Database**: All data is powered by Firebase Cloud Firestore, so changes (new posts, likes, comments) appear instantly without needing to refresh.



## Technology Stack

*   **Language**: Java
*   **Platform**: Android
*   **Database**: Google Firebase - Cloud Firestore
*   **IDE**: Android Studio

