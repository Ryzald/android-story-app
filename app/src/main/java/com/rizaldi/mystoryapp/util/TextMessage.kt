package com.rizaldi.mystoryapp.util

import android.content.Context
import android.widget.Toast

object TextMessage {
    fun setText(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}