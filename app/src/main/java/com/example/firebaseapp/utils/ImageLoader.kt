package com.example.firebaseapp.utils


    import android.content.Context
    import android.widget.ImageView
    import com.bumptech.glide.Glide
    import com.example.firebaseapp.R

class ImageLoader {

        fun loadImage(context: Context, imageUrl: String, imageView: ImageView) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageView)
        }


    fun deleteImage(context: Context,imageView: ImageView) {
        Glide.with(context)
                .load(R.drawable.deleteactionp)
                .into(imageView)

        }
    }