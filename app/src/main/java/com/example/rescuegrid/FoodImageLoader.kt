package com.example.rescuegrid

import android.net.Uri
import android.widget.ImageView

object FoodImageLoader {

    fun load(
        imageView: ImageView,
        imageUri: String
    ) {

        imageView.imageTintList = null
        imageView.clearColorFilter()
        imageView.setPadding(
            0,
            0,
            0,
            0
        )

        if (imageUri.isBlank()) {

            showPlaceholder(
                imageView
            )

            return
        }

        try {

            imageView.setImageURI(
                Uri.parse(
                    imageUri
                )
            )

            imageView.scaleType =
                ImageView.ScaleType.CENTER_CROP

        } catch (_: Exception) {

            showPlaceholder(
                imageView
            )
        }
    }

    private fun showPlaceholder(
        imageView: ImageView
    ) {

        imageView.imageTintList = null
        imageView.clearColorFilter()

        imageView.setImageResource(
            android.R.drawable.ic_menu_gallery
        )

        imageView.scaleType =
            ImageView.ScaleType.CENTER_INSIDE
    }
}