package com.erayerarslan.floreplica.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.erayerarslan.floreplica.R

fun ImageView.loadImage(path: String?) {

    Glide.with(this.context).load(Constants.BASE_URL + path).apply(centerCropTransform()
        .error(R.drawable.ic_error)).into(this)
}