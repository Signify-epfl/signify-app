# Signify - Learn Fingerspelling in American Sign Language (ASL)

**Signify** is a mobile application designed to teach users the art of fingerspelling in American Sign Language (ASL). While many existing apps focus on children, Signify is tailored for users of all ages who want to develop or sharpen their fingerspelling skills. The app combines interactive recognition technology with structured learning modules for a comprehensive learning experience.

## Features

### 1. Fingerspelling Recognition

- **Real-time Feedback**: Users can practice fingerspelling by signing letters in front of their device's camera.
- **Machine Learning**: The app leverages machine learning algorithms to provide instant feedback on the accuracy of users' signs.

### 2. Learning Module: One Sign a Day

- **Daily Lessons**: A dedicated section where users learn a new sign every day.
- **Tips and Instructions**: Each lesson includes tips for perfecting the sign, along with step-by-step instructions.

### 3. Profile & Progress Tracking

- **User Profiles**: Users can create profiles to track their progress and learning journey.
- **Metrics and Motivation**: Metrics such as accuracy rates and completed lessons are displayed to motivate continuous improvement.

### 4. Interactive Exercises

- **Exercise Mode**: Users see an image and the corresponding word (e.g., a car with the word "CAR") and need to sign the letters.
- **Performance Feedback**: The app provides feedback on users' performance and suggests areas for improvement.

## Technology Stack

- **Frontend**: Jetpack Compose (Android)
- **Backend**: Google Firebase
  - **Authentication**: Google Sign-In via Firebase
  - **Data Storage**: Firebase Firestore for real-time syncing and data storage
- **Machine Learning**: Camera-based ASL sign recognition using machine learning algorithms
- **Offline Support**: Users can access previously loaded lessons and exercises without an Internet connection.

## Future Plans

- **Sign Language Expansion**: We plan to expand to British and French sign languages to enrich the learning experience and broaden accessibility.
- **Additional Learning Modes**: Introducing more diverse exercises and quizzes to enhance the user experience.

## Design and Mockups

The design and user interface mockups for **Signify** are available on Figma. This includes wireframes, high-fidelity screens, and prototypes that showcase the overall user experience and visual flow of the app.  
You can view the Figma project here: [Signify Figma Project](https://www.figma.com/files/team/1415806088953386120/project/284550251/Signify-Team?fuid=1415806084792420415)

## Contributing

Contributions are welcome! Please submit a pull request or open an issue to discuss any changes.

## Acknowledgments

- Machine learning models are built using publicly available datasets of American Sign Language signs.
