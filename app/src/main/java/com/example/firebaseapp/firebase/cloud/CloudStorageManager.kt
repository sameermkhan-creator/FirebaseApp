package com.example.firebaseapp.firebase.cloud

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

private const val PHOTOS_REFERENCE = "photos"

class CloudStorageManager {

    private val firebaseStorage by lazy { FirebaseStorage.getInstance() }

    fun uploadPhoto(selectedImageUri: Uri, onSuccessAction: (String) -> Unit) {
        //1
        val photosReference = firebaseStorage.getReference(PHOTOS_REFERENCE)

        //2
        selectedImageUri.lastPathSegment?.let { segment ->
            //3
            val photoReference = photosReference.child(segment)

            //4
            photoReference.putFile(selectedImageUri)
                //5
                .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    val exception = task.exception

                    if (!task.isSuccessful && exception != null) {
                        throw exception
                    }
                    return@Continuation photoReference.downloadUrl
                })
                //6
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        onSuccessAction(downloadUri.toString())
                    }
                }






        }
    }

    fun deletePhoto(selectedImageUri: Uri) {

        val photosReference = firebaseStorage.getReference(PHOTOS_REFERENCE)

        selectedImageUri.lastPathSegment?.let { segment ->

            val photoReference = photosReference.child(segment)


            photoReference.delete().addOnCompleteListener {


            }



        }


    }


}