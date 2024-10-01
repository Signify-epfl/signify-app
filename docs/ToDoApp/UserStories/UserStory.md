# To-Do App - User stories

## Definition of Done (DoD)

For all of the user stories below, we assume that the following criteria are met for the story to be considered "done":

- The acceptance criteria are tested
- Code coverage is at least 80%

## 1. Sign-in and Login

**Story:** As a user, I want to sign-up and log into the app, so that my to-dos are kept across devices.

**Acceptance Criteria:**

- The login screen offers the ability to authenticate with Google.
- The authentication is based on the Firebase Authentication solution.
- Post-login, the user is informed of a successful login.

**Bootcamp Resources:**

- [Assignment description to create a login](1-SignInAndLogin.md)
- [Google Authenticator tutorial](../../Tutorials/GoogleAuthentication.md)

> [!NOTE]  
> Please click [here](../README.md#week-1-environment-setup-and-getting-started) to come back to the timeline of the bootcamp.

## 2. Create a To-Do

**Story:** As a user, I want to create a new to-do, so that I can add new tasks to my list.

**Acceptance Criteria:**

- From the overview, the user can access the to-do creation form.
- For a to-do, the user can enter the title, detailed description, assignee, and due date.
- The fields are validated to ensure the title is not empty before submission, and the due date has been set.
- Upon submission, the to-do is saved to the Firestore database (or its local cache if offline).
- The (Firestore) database has been created with an appropriate schema and stores the to-dos.

**Bootcamp Resources:**

- [Assignment description to create a To-Do](2-AddATodo.md)

> [!NOTE]  
> Please click [here](../README.md#week-2-building-a-to-do-app) to come back to the timeline of the bootcamp.

## 3. List of To-Dos

**Story:** As a user, I want to view a list of my to-dos, so that I can easily see all my pending tasks at once.

**Acceptance Criteria:**

- The list must be scrollable to accommodate a potentially large number of to-dos.
- Each item in the list displays the to-do title.
- To-dos are fetched dynamically from the Firestore database on page load.
- The UI does not need to refresh automatically.
- The list of to-dos is automatically updated to include newly created to-do.

**Bootcamp Resources:**

- [Assignment description to create a list of To-Dos](3-ListOfTodos.md)

> [!NOTE]  
> Please click [here](../README.md#week-2-building-a-to-do-app) to come back to the timeline of the bootcamp.

## 4. Edit a To-Do

**Story:** As a user, I want to edit and view details of my to-dos, so that I can update tasks or review their detailed descriptions.

**Acceptance Criteria:**

- Clicking on a to-do item in the list navigates the user to a detailed view of that to-do.
- The detailed view displays the to-do title, detailed description, assignee, and due date.
- From this view, the user can modify all of the above and the to-do state, or delete the task
- Changes are saved to the Firestore database (or its local cache if offline).
- The app navigates back to the to-do list after the edit is saved or deleted, reflecting the changes immediately.

**Bootcamp Resources:**

- [Assignment description to Edit/View a To-Do](4-EditATodo.md)

> [!NOTE]  
> Please click [here](../README.md#week-2-building-a-to-do-app) to come back to the timeline of the bootcamp.

## 5. Location-based To-Dos

**Story:** As a user, I want to geo-locate my to-dos and see them on a map, so that I can plan my work based on the to-dos location.

**Acceptance Criteria:**

- The user can set a location for a to-do when creating/editing it.
- The Nominatim API is used to reverse-geocode named locations to coordinates.
- A separate map view is available where to-dos with locations are displayed as pins on the map.
- Tapping a pin displays the title and description of the to-do.
- The map view integrates with the Google Maps API to render maps and manage locations.

**Bootcamp Resources:**

- [Assignment description to implement location in our To-Dos](5-LocationBasedTodos.md)

> [!NOTE]  
> Please click [here](../README.md#week-3-finalizing-the-app-with-testing-and-ci) to come back to the timeline of the bootcamp.
