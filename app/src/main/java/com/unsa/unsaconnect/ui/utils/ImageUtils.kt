package com.unsa.unsaconnect.ui.utils

import com.unsa.unsaconnect.R

fun getImageForNews(newsId: Int): Int {
    return when (newsId % 3) {
        0 -> R.drawable.image_3
        1 -> R.drawable.image_1
        2 -> R.drawable.image_2
        else -> R.drawable.image_1 // Default case
    }
}
