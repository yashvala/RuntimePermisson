package com.example.runtimepermisson;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    final static int REQUEST_CAMERA = 1;
    final static int RESULT_LOAD_IMAGE = 2;
    Button btn_takeImages;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_takeImages = findViewById(R.id.btn_takeImages);
        imageView = findViewById(R.id.imageView);
        btn_takeImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }
        });
    }

    private void ShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(MainActivity.this));
        View view = getLayoutInflater().inflate(R.layout.custom_layout, null);
        Button btnFromCamara = view.findViewById(R.id.btnFromCamara);
        Button btnFromGallery = view.findViewById(R.id.btnFromGallery);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(true);

        btnFromCamara.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openCamera();
                alertDialog.dismiss();
            }
        });

        btnFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                alertDialog.dismiss();
            }
        });
    }

    public void openGallery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RESULT_LOAD_IMAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        }
    }

    public void openCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
            }
            break;
            case RESULT_LOAD_IMAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openGallery();
            }
            break;
        }
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case REQUEST_CAMERA: {
                    if (resultCode == RESULT_OK) {
                        Bundle extra = data.getExtras();
                        Bitmap bmp = (Bitmap) extra.get("data");
                        imageView.setImageBitmap(bmp);
                    }
                }
                break;
                case RESULT_LOAD_IMAGE: {
                    if (resultCode == RESULT_OK) {

                        Uri image = data.getData();
                        imageView.setImageURI(image);
                    }
                    break;
                }
            }
        }
}