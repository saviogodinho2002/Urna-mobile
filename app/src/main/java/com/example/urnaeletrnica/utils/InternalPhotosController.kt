package com.example.urnaeletrnica.utils

import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.util.*

class InternalPhotosController(){
    companion object{
        public fun saveAndGetDirPhoto(contentContext:Context,contentResolver:ContentResolver,directoryName:String,imgUri:Uri):String?{
            try {
                val cw = ContextWrapper(contentContext)
                val directory = cw.getDir(directoryName, Context.MODE_PRIVATE)
                val path = File(directory,"${UUID.randomUUID().toString()}.jpg")


                /*
                val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                 val bitmap = ImageDecoder.decodeBitmap(source)
                */
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgUri)
                val out = FileOutputStream(path)

                bitmap.compress( Bitmap.CompressFormat.PNG,15,out)
                out.close()
                return path.absolutePath

            }catch (error:Exception){

            }
            return  null
        }
        public fun removePhotoFile(photo:String){
            val file = File(photo)
            file.delete()
        }
    }


}
