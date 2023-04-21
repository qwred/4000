package com.example.a4000;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageAdd extends AppCompatActivity
{

    private static  final int PICK_IMAGE_REQUEST = 1;

    private Button add;
    private Button change;
    private ImageView image;
    private Uri imageUri;

    private TextView text;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    FirebaseAuth mAuth;

    FirebaseUser user;

    private String userName;

    private DatabaseReference root;
    private StorageReference reference = FirebaseStorage.getInstance().getReference();

    private ProgressBar progressBar;

    String week, ym, year, month;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_add);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        week = getIntent().getStringExtra("WEEK");
        ym = getIntent().getStringExtra("YM");

        String[] date = ym.split("\\.");
        year = date[0];
        month = date[1];

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        userName = user.getDisplayName();

        root = FirebaseDatabase.getInstance().getReference().child("users").child(userName).child("Image").child(year).child(month).child(week);
        // Path of the database of firebase.



        add = findViewById(R.id.addButton);
        change = findViewById(R.id.changeButton);
        image = findViewById(R.id.imageFile);


        //
//        String imagePath = userName + "Image" + year + month + week;
        StorageReference fileRef = reference.child(userName).child(year).child(month).child(week+".jpg");
//        StorageReference imageRef = reference.child(imagePath);

        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri)
            {
                Glide.with(getApplicationContext()).load(uri).into(image);

            }
        });

        //

        image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);

            }
        });


        add.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(imageUri != null)
                {
                    uploadToFirebase(imageUri);

                }
                else
                {
                    Toast.makeText(ImageAdd.this, "Please select an Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(imageUri != null)
                    uploadToFirebase(imageUri);
                else
                    Toast.makeText(ImageAdd.this,"Please select an Image", Toast.LENGTH_SHORT).show();
            }
        });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            image.setImageURI(imageUri);

        }
    }

    private void uploadToFirebase(Uri uri){

//        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        final StorageReference fileRef = reference.child(userName).child(year).child(month).child(week+"."+getFileExtension(uri));
        // Path of the storage of the firebase.
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Model model = new Model(uri.toString());
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(model);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ImageAdd.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        //




                        //

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ImageAdd.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }


}