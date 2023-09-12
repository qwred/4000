package com.example.a4000;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageAdd extends AppCompatActivity
{

    private static  final int PICK_IMAGE_REQUEST = 1;

    private Button add;
    private Button change;
    private ImageView image;
    private Uri imageUri;

    private TextView userText;
    private Button update;
    private EditText editComment;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    FirebaseAuth mAuth;

    FirebaseUser user;

    private String userName;

    private DatabaseReference root, commentRoot;
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
        commentRoot = root.child("comment");
        // Path of the database of firebase.



        add = findViewById(R.id.addButton);
        change = findViewById(R.id.changeButton);
        image = findViewById(R.id.imageFile);

        userText = findViewById(R.id.userComment);
        update = findViewById(R.id.buttonUpdate);
        editComment = findViewById(R.id.editText);



        update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String comment = editComment.getText().toString();
                commentRoot.setValue(comment);

            }
        });




        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String value = snapshot.getValue(String.class);
                    userText.setText(value);
                    editComment.setText(value);
                }
                else
                    userText.setText("The comment you entered at the bottom will be updated here.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d("Firebase", "Error: " + error.getMessage());
            }
        };

        commentRoot.addValueEventListener(valueEventListener);

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

////        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
//        final StorageReference fileRef = reference.child(userName).child(year).child(month).child(week+"."+getFileExtension(uri));
//        // Path of the storage of the firebase.
//        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//
//                        Model model = new Model(uri.toString());
//                        String modelId = root.push().getKey();
//                        root.child(modelId).setValue(model);
//                        progressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(ImageAdd.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                        //Todo: Caption(150), compressing image.
//                        //
//
//
//
//
//                        //
//
//                    }
//                });
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                progressBar.setVisibility(View.VISIBLE);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressBar.setVisibility(View.INVISIBLE);
//                Toast.makeText(ImageAdd.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
//            }
//        });


        //


        final StorageReference fileRef = reference.child(userName).child(year).child(month).child(week+"."+getFileExtension(uri));
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            ByteArrayOutputStream temp = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, temp); //compress the image to reduce its size
            byte[] data = temp.toByteArray();

            UploadTask uploadTask = fileRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(ImageAdd.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private String getFileExtension(Uri mUri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }


}