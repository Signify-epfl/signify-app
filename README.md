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
You can view the Figma project here: [Signify Figma Project](https://www.figma.com/design/2kzILCdZRQU1I76wimIaJg/Signify-App?t=BKSwDNaqzPhiVu4y-1)

For the first milestone we provide also the following file with the figma wireframe and mockup: [Signify Figma M1](https://www.figma.com/design/hJsdCTG31DOo51A86W5NNC/Signify-App-M1?t=BKSwDNaqzPhiVu4y-1)

For the second milestone we provide also the following file with the figma wireframe and mockup: [Signify Figma M2](https://www.figma.com/design/sqe0iMKLsNpGi72ohKHbhI/Signify-App-M2?node-id=0-1&node-type=canvas&t=kf898URWQxbvH1FA-0)

For the last milestone we provide also the following file with the figma wireframe and mockup: [Signify Figma M3](https://www.figma.com/design/zaiPChlhLnYnNAvkWd53FN/Signify-App-M3?node-id=0-1&p=f&t=yEW6zUL2IyGV45x8-0)
## Contributing

All steps of the development of new features and bug fixes are organized through the [scrum board](https://github.com/orgs/Signify-epfl/projects/2) on Github.  
Check [this wiki page](https://github.com/Signify-epfl/signify-app/wiki/Scrum-Tasks) on how to handle scrum tasks.

## Acknowledgments

- Machine learning models are built using publicly available datasets of American Sign Language signs.
