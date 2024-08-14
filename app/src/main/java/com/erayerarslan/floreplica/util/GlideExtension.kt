package com.erayerarslan.floreplica.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.erayerarslan.floreplica.R

fun ImageView.loadImage(path: String?) {

    val imagePath = path ?: return
    Glide.with(this.context)
        .load(imagePath)
        .apply(
            RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_error)
        )
        .into(this)
}