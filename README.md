# Popular Movies, Stage 2

Udacity - Android Developer Nanodegree - Projects: Popular Movies, Stage 2

Built a UI that presented the user with a grid of movie posters, allowed users to change sort order, and presented a screen with additional information on the movie selected by the user

# Project Specifications
## User Interface - Layout 

- UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.

- Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.

- UI contains a screen for displaying the details for a selected movie.

- Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.

- Movie Details layout contains a section for displaying trailer videos and user reviews.e file.
## User Interface - Function

- When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.

- When a movie poster thumbnail is selected, the movie details screen is launched.

- When a trailer is selected, app uses an Intent to launch the trailer.

- In the movies detail screen, a user can tap a button (for example, a star) to mark it as a Favorite. Tap the button on a favorite movie will unfavorite it.

## Network API Implementation

- In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.

- App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.

- App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

## Data Persistence
- The titles and IDs of the user’s favorite movies are stored stored using Room.

- When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the database.

## Android Architecture Components

- If Room is used, database is not re-queried unnecessarily. LiveData is used to observe changes in the database and update the UI accordingly.

- If Room is used, database is not re-queried unnecessarily after rotation. Cached LiveData from ViewModel is used instead.
<p float="left">
<img width="260" alt="screenshot" src="https://raw.githubusercontent.com/aencg/.github/master/popular_movies_images/popular%20movies1.png?token=ANISTK3NHWONP2VHXEW44327EKBC6">

<img width="260" alt="screenshot2" src="https://raw.githubusercontent.com/aencg/.github/master/popular_movies_images/popular%20movies2.png?token=ANISTK2BNTSNR4NHOLYGLZK7EKBPO">
</p>
 
