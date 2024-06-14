package com.petspick.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.ByteArrayOutputStream

class Utils {
    companion object{
        fun parseStrToList(str: String): List<String> {
            val jsonString = str.replace("[", "").replace("]", "")
            val arr = jsonString.split(", ")
            return arr.toList()
        }

        fun uriToBitmap(context: Context, uri: Uri, callback: (Bitmap) -> Unit) {
            Glide
                .with(context)
                .asBitmap()
                .load(uri)
                .override(512, 512)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        callback(resource)
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        fun bitmapToBase64(photo: Bitmap): String {
            val stream = ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            return Base64.encodeToString(stream.toByteArray(), 0)
        }

        fun base64ToBitmap(base64: String): Bitmap {
            return try {
                val bytes = Base64.decode(base64, 0)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            }
        }
    }
}