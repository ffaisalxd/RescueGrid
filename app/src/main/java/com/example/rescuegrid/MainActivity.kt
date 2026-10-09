package com.example.rescuegrid

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        RescueData.initialize(this)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->

            val bars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            view.setPadding(
                bars.left,
                bars.top,
                bars.right,
                bars.bottom
            )

            insets
        }

        val emailEditText =
            findViewById<TextInputEditText>(
                R.id.emailEditText
            )

        val passwordEditText =
            findViewById<TextInputEditText>(
                R.id.passwordEditText
            )

        val loginButton =
            findViewById<MaterialButton>(
                R.id.loginButton
            )

        val registerButton =
            findViewById<MaterialButton>(
                R.id.registerButton
            )

        loginButton.setOnClickListener {

            val email =
                emailEditText.text
                    .toString()
                    .trim()

            val password =
                passwordEditText.text
                    .toString()

            if (email.isEmpty()) {

                emailEditText.error =
                    "Enter your email"

                return@setOnClickListener
            }

            if (
                !Patterns.EMAIL_ADDRESS
                    .matcher(email)
                    .matches()
            ) {

                emailEditText.error =
                    "Enter a valid email"

                return@setOnClickListener
            }

            if (password.isEmpty()) {

                passwordEditText.error =
                    "Enter your password"

                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Welcome to RescueGrid",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(
                Intent(
                    this,
                    RoleSelectActivity::class.java
                )
            )
        }

        registerButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }
    }
}