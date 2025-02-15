# Manga App

This is a Manga Android application that allows users to explore, bookmark, and read their favorite manga. The app leverages Firebase Authentication for user registration and login, and Firebase Firestore to store user bookmarks. Manga details are fetched from the MangaDex API, allowing users to browse and search through a vast collection of manga titles.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [Screenshots](#screenshots)
- [Future Enhancements](#future-enhancements)

## Features

- **User Registration and Authentication**: Users can register and log in using Firebase Authentication.
- **Manga Exploration**: Browse manga titles with images, descriptions, and ratings from MangaDex API.
- **Bookmark Favorites**: Users can bookmark their favorite manga to save for later, stored in Firestore.
- **Read Manga**: Access a manga reader within the app.
- **Search and Filter**: Filter manga by title, genre, and other options for a personalized experience.

## Tech Stack

- **Languages**: Java, XML
- **Libraries**: 
  - [Firebase](https://firebase.google.com/) (Authentication, Firestore)
  - [Picasso](https://square.github.io/picasso/) or [Glide](https://bumptech.github.io/glide/) for image loading
  - [MangaDex API](https://api.mangadex.org/) for manga data
- **Android Architecture Components**: ViewModel, LiveData, RecyclerView
- **Other Tools**: Android Studio

## Project Structure

```plaintext
.
├── adapter/
│   ├── MangaAdapter.java         # Adapter for displaying manga list
│   ├── BookmarksAdapter.java     # Adapter for displaying bookmarked manga
├── model/
│   ├── Manga.java                # Data model for Manga items
│   ├── Bookmark.java             # Data model for bookmarks
├── ui/
│   ├── LoginActivity.java        # Activity for user login
│   ├── RegisterActivity.java     # Activity for user registration
│   ├── MainActivity.java         # Main activity displaying manga list
│   ├── MangaDetailActivity.java  # Activity for viewing manga details
│   ├── BookmarksActivity.java    # Activity for viewing bookmarked items
├── utils/
│   ├── FirebaseUtils.java        # Firebase helper functions
├── resources/
│   ├── layout/                   # XML layout files for each activity and item
│   ├── drawable/                 # Images and icons
├── README.md                     # Project documentation
└── build.gradle                  # Build configurations and dependencies
```

## Setup and Installation

### Prerequisites

- Android Studio (latest version)
- Firebase Project (with Authentication and Firestore set up)
- MangaDex API key (if required)

### Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/manga-app.git
   cd manga-app
   ```

2. **Setup Firebase**:
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/).
   - Enable Firebase Authentication (Email/Password).
   - Enable Firestore Database.
   - Download the `google-services.json` file and place it in the `app/` directory.

3. **Configure Dependencies**:
   Ensure your `build.gradle` includes the required libraries:
   ```gradle
   implementation 'com.google.firebase:firebase-auth:21.0.1'
   implementation 'com.google.firebase:firebase-firestore:24.0.0'
   implementation 'com.squareup.picasso:picasso:2.8'
   ```

4. **Run the Project**:
   - Open the project in Android Studio.
   - Sync the project and ensure there are no errors.
   - Run the app on an emulator or physical device.

## Usage

1. **Register or Log in**:
   - Users need to register an account or log in with existing credentials to access the app.
  
2. **Browse Manga**:
   - Users can view the manga collection, search by title, and view detailed manga information by tapping on any manga item.

3. **Bookmark a Manga**:
   - To bookmark a manga, click on the bookmark icon within the details view. Bookmarked items are saved in Firestore.

4. **View Bookmarks**:
   - Users can view saved bookmarks in the **Bookmarks** section, displayed in a grid layout.
  
5. **Read Manga**:
   - From the Manga Detail view, users can open the manga reader to start reading.

## Screenshots

![724shots_so](https://github.com/user-attachments/assets/855028fb-4087-49cf-9680-34634f247224)


## Future Enhancements

- **Offline Support**: Allow users to download manga chapters for offline reading.
- **User Profile Management**: Add profile settings where users can update their information.
- **Enhanced Search & Filters**: Add more filter options for genre, rating, and popularity.
- **Notifications**: Notify users of new manga releases or updates in their bookmarked manga.

---
