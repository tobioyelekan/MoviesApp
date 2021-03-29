package com.example.movies.util

import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String?.parseError(): String {
    return try {
        if (this !== null) {
            val error = JSONObject(this)
            error.getString("status_message")
        } else {
            "Something went wrong"
        }
    } catch (e: JSONException) {
        "Something went wrong"
    }
}

fun Fragment.showMessage(msg: String) {
    Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun ImageView.loadImage(url: String) {
    if (url.isNotEmpty()) {
        Glide.with(context)
            .load(url)
            .into(this)
    }
}

fun String.formatDate(): String {
    return try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val newFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        val date = originalFormat.parse(this)
        newFormat.format(date)
    } catch (ex: ParseException) {
        ex.printStackTrace()
        this
    }
}