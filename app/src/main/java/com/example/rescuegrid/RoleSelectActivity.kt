package com.example.rescuegrid

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class RoleSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_role_select)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.roleSelectMain)
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

        val donorCard =
            findViewById<MaterialCardView>(
                R.id.donorCard
            )

        val recipientCard =
            findViewById<MaterialCardView>(
                R.id.recipientCard
            )

        val volunteerCard =
            findViewById<MaterialCardView>(
                R.id.volunteerCard
            )

        donorCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    DonorDashboardActivity::class.java
                )
            )
        }

        recipientCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RecipientDashboardActivity::class.java
                )
            )
        }

        volunteerCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    VolunteerDashboardActivity::class.java
                )
            )
        }
    }
}