package com.example.a4000;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChildRecyclerViewAdapter extends RecyclerView.Adapter<ChildRecyclerViewAdapter.ChildViewHolder> {
    private List<ChildItem> childList;

    //
    FirebaseAuth mAuth;

    FirebaseUser user;

    private String userName;

    //

    private String parentTitle;

    public ChildRecyclerViewAdapter(List<ChildItem> childList) {this.childList = childList;}

    public ChildRecyclerViewAdapter(List<ChildItem> childList, String parentTitle) {
        this.childList = childList;
        this.parentTitle = parentTitle;
    }



    public class ChildViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;

        public ChildViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.childLogoIv);
            title = itemView.findViewById(R.id.childTitleTv);
        }
    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
        holder.imageView.setImageResource(childList.get(position).getImage());
        holder.title.setText(childList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(view.getContext(), ImageAdd.class);
                intent.putExtra("WEEK", childList.get(position).getTitle());
                intent.putExtra("YM",parentTitle);
                //
                view.getContext().startActivity(intent);
            }
        });

        //
        String ym = parentTitle;
        String[] date = ym.split("\\.");
        String year = date[0];
        Log.d("child", "year" + year);
        String month = date[1];
        Log.d("child", "month" + month);
        String week = childList.get(position).getTitle();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        userName = user.getDisplayName();

        File localFile;

        StorageReference imageRef = storage.getReference().child(userName).child(year).child(month).child(week+"."+"jpg");
        try {
            localFile = File.createTempFile(userName+year+month+week,"jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
        {

            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
            {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                holder.imageView.setImageBitmap(bitmap);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
            }
        });



        //
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }
}