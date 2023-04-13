package com.example.a4000;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChildRecyclerViewAdapter extends RecyclerView.Adapter<ChildRecyclerViewAdapter.ChildViewHolder> {
    private List<ChildItem> childList;

    public ChildRecyclerViewAdapter(List<ChildItem> childList) {
        this.childList = childList;
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
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }
}