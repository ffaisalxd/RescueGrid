package com.example.rescuegrid

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.profileMain)
        ) { view, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        findViewById<LinearLayout>(
            R.id.backContainer
        ).setOnClickListener {
            finish()
        }

        val role =
            intent.getStringExtra(
                "userRole"
            ) ?: "User"

        findViewById<TextView>(
            R.id.profileNameText
        ).text =
            "Faisal Hamedi"

        findViewById<TextView>(
            R.id.profileEmailText
        ).text =
            "pesalbantai@gmail.com"

        findViewById<TextView>(
            R.id.profileRoleText
        ).text =
            role

        findViewById<TextView>(
            R.id.profileAreaText
        ).text =
            "Kuala Lumpur"

        findViewById<MaterialButton>(
            R.id.changeRoleButton
        ).setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Change role")
                .setMessage(
                    "Return to role selection?"
                )
                .setNegativeButton(
                    "Cancel",
                    null
                )
                .setPositiveButton(
                    "Change Role"
                ) { _, _ ->

                    val intent =
                        Intent(
                            this,
                            RoleSelectActivity::class.java
                        )

                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                Intent.FLAG_ACTIVITY_NEW_TASK

                    startActivity(intent)
                    finish()
                }
                .show()
        }

        findViewById<MaterialButton>(
            R.id.logoutButton
        ).setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage(
                    "Return to the login screen?"
                )
                .setNegativeButton(
                    "Cancel",
                    null
                )
                .setPositiveButton(
                    "Logout"
                ) { _, _ ->

                    val intent =
                        Intent(
                            this,
                            MainActivity::class.java
                        )

                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)
                    finish()
                }
                .show()
        }
    }
}