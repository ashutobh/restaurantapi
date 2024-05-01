# Restaurant Finder Backend APIs

Restaurant Finder is a backend Spring Boot RESTful application that provides set of APIs to frontend application so that groups of people to create and join sessions to share their restaurant preferences. At the end of the session, one restaurant is randomly selected from the choices provided by the participants.

## Below are the APIs provided
- **/showAllUsers**: 
  - Users can show all the Users added to the system.
- **/addUser**: 
  - Add User the to the system before joining the session.
- **/getActiveSession**: 
  - Users can see the Active session is present to participate.
- **/createSession**: 
  - Any one user can create the session
- **/showAllSessionUsers**: 
  - Users can see the users who have already joined the session.
- **/joinSession**: 
  - Participants can join existing sessions using a session code.
- **/addRestaurant**: 
  - Each participant can input their restaurant preferences within the session.
- **/terminateSession**: 
  - User who has created the session can terminate it and one restuarant will be selected
- **/getSelectedRestaurant**: 
  - All Restaurant will be dispalyed, those are selected in any session.

## Installation

1. Clone the repository:

   git clone (https://github.com/ashutobh/restaurantapi.git)

2. cd restaurantapi
   1. Start as a maven project (Make sure maven is already configured)
   2. Refer the postman collection attached in the project(src/main/resources/postman_collection.json). you can simply download postman collection and import to the postman.
   3. you can use the frontend(Restaurant Finder App) app to test the APIs

3. Usage Instructions
   1. Add the Users (/addUser)
   2. Show All Users (/showAllUsers)
   3. Create Session (/createSession)
   4. Show session (/getActiveSession)
   5. Join the session (/joinSession)
   6. Show all session users (/showAllSessionUsers)
   7. Add Restaurant (/addRestaurant)
   8. Terminate Session (/terminateSession)
   9. Show selected restaurant (/getSelectedRestaurant)

4. Technologies Used
   1. Spring Boot
   2. Spring Web Mbc
   3. H2 database
   4. Spring Data JPA
   5. Junit
   6. Mockito
