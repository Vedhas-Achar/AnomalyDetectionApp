package com.example.touchlogger

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts // NEW IMPORT
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // NEW: The modern way to handle activity results.
    // This launcher will handle the permission request.
    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // This block is called when the user returns from the settings screen.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
            // Permission was granted
            startOverlayService()
        } else {
            // Permission was denied
            Toast.makeText(this, "Permission not granted. Cannot start service.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton: Button = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            checkOverlayPermissionAndStartService()
        }
    }

    private fun checkOverlayPermissionAndStartService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            // Permission is not granted, request it using our new launcher.
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            // LAUNCH the intent instead of using startActivityForResult
            overlayPermissionLauncher.launch(intent)
        } else {
            // Permission is already granted, we can start the service.
            startOverlayService()
        }
    }

    private fun startOverlayService() {
        Toast.makeText(this, "Starting Overlay Service. Phone will become unresponsive.", Toast.LENGTH_LONG).show()
        startService(Intent(this, OverlayService::class.java))
        finish() // Close the UI so the overlay is the only thing visible.
    }

    // The old onActivityResult method is NO LONGER NEEDED with this new approach.
    // override fun onActivityResult(...) { ... }
}