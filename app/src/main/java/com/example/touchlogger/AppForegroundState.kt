// In app/src/main/java/com/example/touchlogger/AppForegroundState.kt

package com.example.touchlogger

/**
 * A simple singleton object to hold the package name of the app currently in the foreground.
 * Both services can access this shared state.
 */
object AppForegroundState {
    // We make it volatile to ensure that changes made by one thread
    // are immediately visible to other threads.
    @Volatile
    var currentAppPackageName: String = "N/A"
}