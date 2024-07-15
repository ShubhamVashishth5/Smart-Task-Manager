# Smart task manager (assignment for lenscorp.ai)

### Libraries used
1. Jetpack librarires
2. Hilt for dependency injection
3. Room persistance library for database
4. AndroidX biometri
5. Charts : implementation("io.github.dautovicharis:charts:1.3.1")
6. Leku for location picker:  implementation ("com.adevinta.android:leku:11.1.4")
7. Google Maps API, Places API, Geolocation API
8. Material UI 

### Build and run

To run, simply clone the repo and run 
``./gradlew assembleDebug`` in the root directory
Generated output apk can be found in \app\build\outputs\apk\debug

Or 

Import project in android studio and run on emulator or device


### Design
1. App uses MVVM architecture with Composables serving as the views.
2. No fragments are used, for navigation, androidX navigation is used
3. For location, I have used Google apis along with Leku library, which is an alternative to implementing the whole location picking process.
4. For charts i have used a chart library instead of using canvas and implementing all the charts.
5. I focused on implementing as much as i could, with very little regard to ui, which might make the UI look very simplistic

### Link to demo
[App Demo](https://www.youtube.com/watch?v=M2bRYKTXCo4)

[Permission and push notification demo (only local push notifications)](https://www.youtube.com/watch?v=hPlivd74gWA)
