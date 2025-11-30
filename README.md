# bookcare
BookCare -WIA2007 Mobile Application Development Project

# BookCare Android App

This project is an Android app using **Firebase** services (Authentication, Firestore, Storage, Analytics).

---

## Getting Started

### 1. Clone the Repository

```bash
git clone <your-repo-url>
cd <project-folder>
```

### 2. Set Up Firebase

Each developer must use their **own Firebase project**:

1. Go to [Firebase Console](https://console.firebase.google.com/) → **Add project** → **Add Android app**.
2. Package name must match `applicationId` in `app/build.gradle`.
3. Download `google-services.json` and place it in the `app/` folder.

### 3. Sync and Build

* In Android Studio → **File → Sync Project with Gradle Files**.
* **Rebuild Project**.

### 4. Run the App

* The app should now run normally with your Firebase setup.

---

## Notes

* Do **not** commit your `google-services.json` to GitHub.
* Make sure your team members set up **their own Firebase project** to avoid conflicts.

---

## Firebase Dependencies

```gradle
implementation platform('com.google.firebase:firebase-bom:34.6.0')
implementation 'com.google.firebase:firebase-analytics-ktx'
implementation 'com.google.firebase:firebase-auth-ktx'
implementation 'com.google.firebase:firebase-firestore-ktx'
implementation 'com.google.firebase:firebase-storage-ktx'
```

---

## License

[MIT](LICENSE)
