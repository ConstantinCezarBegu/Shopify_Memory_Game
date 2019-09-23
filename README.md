# shopify-memory-game
Coding challenge for Shopify
This app has persistence of rotation, database rotation, error handling. All that good stuff.

## apk
The apk can be found [here](app-debug.apk).

## directories
Rundown of the directories and what they do.

* [adapters](/app/src/main/java/com/example/shopify_memory_game/adapters ) 
  * GridLayoutWrapper.kt:
    * Prevents crash when relaunching activity (This was caused by the start animation that recycler view has). 
  * RecyclerViewAdapter.kt:
    * Logic for the recycler view RecyclerViewSelectionImageTracker.kt: Logic that allows to track the selected items for the game.


* [data](/app/src/main/java/com/example/shopify_memory_gamAe/data)
  * db
    * Dao and repository for the app (Stored the products) network: Logic that allows to fetch the data and error check it. NOTE: The entity for the card dao is located in here. This is due that I combined the annotation for retrofit and room (quicker to code)
  * preference 
    * Contains the wrapper for the preference (clean code) 
  * repository
    * contains the file that allows to store the fetched objects into the database and extract the required info.


* [internal](/app/src/main/java/com/example/shopify_memory_game/internal)
  * The files in here are used to simplify code elsewhere.


* [ui](/app/src/main/java/com/example/shopify_memory_game/ui)
  * Contains the viewmodel, bundle shared pref view model factory, activity and dialogs

## extra
Feel free to check out my public app on the [Google Play Store](https://play.google.com/store/apps/details?id=com.constantin.constaflux).
