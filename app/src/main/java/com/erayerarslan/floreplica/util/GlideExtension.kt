package com.erayerarslan.floreplica.util

import android.util.DisplayMetrics
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
fun ImageView.DetailImage(path: String?) {
    val imagePath = path ?: return
    Glide.with(this.context)
        .load(imagePath)
        .apply(
            RequestOptions()
                .fitCenter()  // Resmi ImageView'a tam oturtur ama bozulmayı engeller
                .override(800, 1200)  // İsteğe bağlı: Görüntü boyutunu sınırlar
                .error(R.drawable.ic_error)
        )
        .into(this)
}



fun ImageView.detailImage(path: String?) {

    val imagePath = path ?: return

    Glide.with(this.context)
        .load(imagePath)
        .apply(
            RequestOptions()
                .override(300, 300) // Sabit genişlik ve yükseklik (piksel cinsinden)
                .centerCrop()
                .fitCenter()
                .error(R.drawable.ic_error)
        )
        .into(this)
}