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

All steps of the development of new features and bug fixes are organized through the [scrum board](https://github.com/orgs/Signify-epfl/projects/2) on Github. Tasks are compartmentalized as entries in the [scrum board](https://github.com/orgs/Signify-epfl/projects/2) and distributed into 1 week sprints.

### User stories

Any new feature must be associated with a user story.
A user story must match the following format:
`As a <role>, I want to <action>, so that <reason>.`
e.g. `As an active user, I want to log in to the app using my email, so that my data can be saved between sessions.`
New user stories must be added to the scrum board under the status `Product Backlog`.

### Issues

To signal a bug, create an task on the scrum board detailing the fix. Then convert that task to a Github issue.
Once an assignee fixes the issue, they must create a pull request linking their fix to that issue.
A different developer will then need to approve the request to merge it with the app's main development branch.

### Task Anatomy

Before making any contribution, you **must** mark a task in the scrum board with the status `In Development`.
Once completed, mark it as `In Review`.
After a separate developer reviews your change, the task shall finally be marked as `Done`.

The following fields in a task detail what the objective of your work is:

- title: What does this task aim to do?
- description: What specific changes will be made when this task is completed?
- nature: Is this a user story or a single sprint task?
- priority: How important is this task?
- status: What stage of development is this task in?
  - Product Backlog: this is an unimplemented user story.
  - Sprint Backlog: this task should be considered for the current sprint.
  - In Development: this task is currently being worked on.
  - In Review: this task is awaiting review from another developer.
  - Done in Sn: this task was reviewed and marked as done in sprint n.
- assignee: Who is or will be working on this task? (set before status `In Development`)
- estimated time: How long do you estimate this task to take to complete?
- actual time: How long did this task take to complete? (set after status `Done`)
- scope: Which part of the project does this task primarily affect?
- epic: Which broad set of features does this task aim to build up?

The title and description of a change should match the conventions for git commit messages.
Task fields are to be set as soon as possible in the task's life cycle.
Each of these fields must be filled in by the time a task is marked as `Done`.

## Acknowledgments

- Machine learning models are built using publicly available datasets of American Sign Language signs.
