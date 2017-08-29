package com.finalproject.elhen15.musicbox.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by Elhen15 on 29/08/2017.
 */

public class ModelStorageFirebase {

    public static final String IMAGES_KEY = "MusicImages";
    private FirebaseStorage storage;
    private StorageReference imagesReference;

    public ModelStorageFirebase() {
        storage = FirebaseStorage.getInstance();
        imagesReference = storage.getReference().child(IMAGES_KEY);
    }


    // Save image interface
    interface ISaveImageCallback {
        void onComplete(String imageUrl);
        void onCancel();
    }
    public void saveImage(Bitmap imageBmp, String name, final ISaveImageCallback callback){
        StorageReference image = imagesReference.child(name);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        UploadTask uploadTask = image.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                callback.onCancel();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                callback.onComplete(downloadUrl.toString());
            }
        });
    }



    // Get image interface
    interface IGetImageCallback {
        void onComplete(Bitmap image);
        void onCancel();
    }
    public void getImage(String url, final IGetImageCallback callback){
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                callback.onComplete(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d("TAG",exception.getMessage());
                callback.onCancel();
            }
        });
    }

}
