# Milestone Deliverables

- [Milestone B1](#milestone-b1) due at 23:59 on 17.09.2024
- [Milestone B2](#milestone-b2) due at 23:59 on 23.09.2024
- [Milestone B3](#milestone-b3) due at 23:59 on 30.09.2024

The steps you follow as you complete the three milestones will guide you from the basics to more advanced concepts of Android development. Good luck, and happy coding!

## Milestone B1

This milestone focuses on setting up your Android development infrastructure, plus a few basics of the ToDo app.

| Status | Task                                                                                       | Estimated Time   | Actual Time |
| ------ | ------------------------------------------------------------------------------------------ | ---------------- | ----------- |
| ☐      | [Initial Setup](Setup.md#setting-up-your-development-environment)                         | 1 hour           | -           |
| ☐      | [Create a Simple Android Application](Setup.md#getting-started-with-android)               | 1 hour           | -           |
| ☐      | [Set up Continuous Integration (CI)](Setup.md#setting-up-the-continuous-integration)       | 1.5 hours        | -           |
| ☐      | [Build the APK](Setup.md#building-an-apk)                                                  | 1 hour           | -           |
| ☐      | Read [Ensuring Compatibility and Parity with SigCheck and Screen](sigcheck/README.md)      | 10 min           | -           |
| ☐      | Review [User Stories](../Theory.md#user-stories) theory                                          | 10 min           | -           |
| ☐      | Review [Good Commit Messages](../Theory.md#commit-messages) theory                                       | 10 min           | -           |
| ☐      | Write user stories (see below)                                    | 30 min           | -           |
| ☐      | Implement the [Sign-in and Login](UserStories/UserStory.md#1-sign-in-and-login) user story by following [these steps](UserStories/1-SignInAndLogin.md)| 2.5 hours        | -           |

We provide you with a few [initial user stories](UserStories/UserStory.md) that give you a better understanding of the ToDo app.
For the _Write user stories_ deliverable, you need to write two additional user stories.
Write your user stories in a file named `userStories.txt` at the root of your repo, one story per long line (this is essential in order to enable grading).
In other words, the file should contain exactly two lines of text, each line containing one user story.

> [!NOTE]
> You do not have to implement the features, so be creative in your user stories!

### B1 Grading

> [!IMPORTANT]  
> Automated grading will start on the `main` branch of this repo at 24:00 on 17.09.2024, so please make sure that your submission is pushed to `main` before then.

- Bronze
  - Pass all the public B1 tests, which check that your environment is properly set up
  - Pass all the public tests for [Greeting UI](Setup.md#getting-started-with-android)
- Gold: In addition to Bronze,
  - Pass all the staff tests for [Sign-in and Login](UserStories/UserStory.md#1-sign-in-and-login)
  - Your commit messages are good
  - The two user stories are good
- Platinum: In addition to Gold,
  - Your commit messages are perfect
  - The two user stories are perfect

Continuous Integration (CI) will run the public tests, so you can just check the CI output.

Since we will grade the commit messages that accompany the pushes to your repository,  make sure you follow from the start the guidelines summarized [here](../Theory.md#commit-messages).

## Milestone B2

TBD

<!-- ### Due Date

Monday 23.09.2024 @ 23:59

### Overview

Milestone B2 focuses on creating the app with its core features and beginning to test it.

### Week 2: Building a To-Do App

| Status | Task                                                                                                                                 | Estimated Time  | Actual Time |
| ------ | ------------------------------------------------------------------------------------------------------------------------------------ | --------------- | ----------- |
| ☐      | [Theory: splitting an app into multiple components using interfaces](../Theory.md#splitting-an-app-into-components-using-interfaces) | 15min (reading) | -           |
| ☐      | [Theory: implementing the MVVM model](../Theory.md#implementing-the-mvvm)                                                            | 15min (reading) | -           |
| ☐      | [User-story: Create a To-Do](UserStories/UserStory.md#2-create-a-to-do)                                                              | 4 hours         | -           |
| ☐      | [User-story: View List of To-Dos](UserStories/UserStory.md#3-list-of-to-dos)                                                         | 2 hours         | -           |
| ☐      | [User-story: Edit a To-Do](UserStories/UserStory.md#4-edit-a-to-do)                                                                  | 2 hours         | -           |

> [Deliverables for Milestone 2](Deliverables.md#b2-deliverables)

### B2 Deliverables

For your B2 submission, you must complete and pass the following tasks:

1. [CreateATodo](UserStories/2-AddATodo.md)
2. [List of Todos](UserStories/3-ListOfTodos.md)
3. [EditAndView Todo](UserStories/4-EditATodo.md)
4. Pass the provided tests and sigchecks, see the end of each user stories' description.

> [!TIP]
> The provided tests are enough to guarantee a passing grade on the "Staff tests suite" (see the [grading](../../README.md#grading)).

Continuous Integration (CI) will verify that the signature check compile and that all required tests are present and passing.

|                     | **First tier (4.0)**                              | **Second tier (5.5)**                           | **Third tier (6.0)**                            |
|---------------------|---------------------------------------------------|-------------------------------------------------|-------------------------------------------------|
| **Requirements**    | <div align="center">-</div>                       | All requirements from the first tier            | All requirements from the second tier           |
| **Week 1**          | - User stories<br>- Greeting tests                | Sign-in and login tests                         | <div align="center">-</div>                     |
| **Week 2**          | Pass 50% of the tests                             | Pass all tests                                  | <div align="center">-</div>                     |
| **Week 3**          | - Pass 50% of the tests<br>- 60% coverage         | - Pass all tests<br>- 70% coverage              | - 80% coverage<br>- 3 Advanced exercises        |

> [!NOTE]
>
> - The provided tests count towards the coverage threshold
> - For the Advanced exercises, more information will be soon available
-->

## Milestone B3

TBD
<!-- 
### Due Date

Monday 30.09.2024 @ 23:59

### Overview

Milestone B3 focuses on testing the app and developing advanced features.

### B3 Deliverables

For your B3 submission, you must complete and pass the following tasks:

1. [PR Review](Exercises.md#pull-request-review)
2. [User-story: Location Based To-Dos](UserStories/5-LocationBasedTodos.md)
3. 80 % Line coverage on JaCoCo
4. Pass the provided tests and sigchecks, see the end of each user stories' description.

> [!TIP]
> The provided tests are enough to guarantee a passing grade on the "Staff tests suite" (see the [grading](../../README.md#grading)).

Continuous Integration (CI) will verify that the signature check compile and that all required tests are present and passing.

> [Return to the Timeline](README.md#week-3-finalizing-the-app-with-testing-and-ci)

### Week 3: Finalizing the App with Testing and CI

| Status | Task                                                                                 | Estimated Time  | Actual Time |
| ------ | ------------------------------------------------------------------------------------ | --------------- | ----------- |
| ☐      | Recap: Collaborative development                                                     | 10min (reading) | -           |
| ☐      | Exercise: [PR Review](Exercises.md#pull-request-review)                              | 1 hours         | -           |
| ☐      | [Unit testing](../Tutorials/UnitTesting.md)                                          | 20min (reading) | -           |
| ☐      | [User-story: Location Based To-Dos](UserStories/5-LocationBasedTodos.md)             | 3 hours         | -           |
| ☐      | [UI testing](../Tutorials/AndroidTesting.md)                                         | 4 hours         | -           |

|                     | **First tier (4.0)**                              | **Second tier (5.5)**                           | **Third tier (6.0)**                            |
|---------------------|---------------------------------------------------|-------------------------------------------------|-------------------------------------------------|
| **Requirements**    | <div align="center">-</div>                       | All requirements from the first tier            | All requirements from the second tier           |
| **Week 1**          | - User stories<br>- Greeting tests                | Sign-in and login tests                         | <div align="center">-</div>                     |
| **Week 2**          | Pass 50% of the tests                             | Pass all tests                                  | <div align="center">-</div>                     |
| **Week 3**          | - Pass 50% of the tests<br>- 60% coverage         | - Pass all tests<br>- 70% coverage              | - 80% coverage<br>- 3 Advanced exercises        |

> [!NOTE]
>
> - The provided tests count towards the coverage threshold
> - For the Advanced exercises, more information will be soon available

### B3: Pull Request Review

In this exercise, you will review a Pull Request (PR) made by our team on your repository. This PR introduces a calendar-like feature that displays your to-dos in a timeline view. However, the code will contain a few issues. Your task is to identify these issues and request changes.

> [!WARNING] > **Do not merge the PR**. If you did please revert it.

Before reviewing the code, test the new feature on your emulator or phone. To do this, ensure that the necessary navigation has been added to access the new calendar screen (c.f. PR description).

This sequence of steps is designed to guide you from the basics to more advanced concepts of Android development. The estimated times are there to help you plan your study sessions. Remember, the key to success is consistent progress!

Good luck, and happy coding!
-->

> [!CAUTION]
> Do not modify anything in this file other than the time and progress tracking.
