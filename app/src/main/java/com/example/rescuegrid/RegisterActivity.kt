package com.example.rescuegrid

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.registerMain)
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

        findViewById<LinearLayout>(
            R.id.backContainer
        ).setOnClickListener {
            finish()
        }

        val nameEditText =
            findViewById<TextInputEditText>(
                R.id.nameEditText
            )

        val emailEditText =
            findViewById<TextInputEditText>(
                R.id.registerEmailEditText
            )

        val passwordEditText =
            findViewById<TextInputEditText>(
                R.id.registerPasswordEditText
            )

        val confirmPasswordEditText =
            findViewById<TextInputEditText>(
                R.id.confirmPasswordEditText
            )

        val createAccountButton =
            findViewById<MaterialButton>(
                R.id.createAccountButton
            )

        createAccountButton.setOnClickListener {

            val name =
                nameEditText.text
                    .toString()
                    .trim()

            val email =
                emailEditText.text
                    .toString()
                    .trim()

            val password =
                passwordEditText.text
                    .toString()

            val confirmPassword =
                confirmPasswordEditText.text
                    .toString()

            if (name.isEmpty()) {

                nameEditText.error =
                    "Enter your name"

                return@setOnClickListener
            }

            if (
                email.isEmpty() ||
                !Patterns.EMAIL_ADDRESS
                    .matcher(email)
                    .matches()
            ) {

                emailEditText.error =
                    "Enter a valid email"

                return@setOnClickListener
            }

            if (password.length < 6) {

                passwordEditText.error =
                    "Use at least 6 characters"

                return@setOnClickListener
            }

            if (
                password !=
                confirmPassword
            ) {

                confirmPasswordEditText.error =
                    "Passwords do not match"

                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Account created",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(
                Intent(
                    this,
                    RoleSelectActivity::class.java
                )
            )

            finish()
        }
    }
}