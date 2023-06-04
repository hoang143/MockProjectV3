package com.example.mockprojectv3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mockprojectv3.R;
import com.example.mockprojectv3.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> mListCat;

    public void setData(List<Category> list){
        this.mListCat = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCat.get(position);
        if(category == null){
            return;
        }
        holder.btnCat.setText(category.getNameCategory());
    }

    @Override
    public int getItemCount() {
        if(mListCat !=null){
            return mListCat.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView btnCat;

        public CategoryViewHolder(@NonNull View itemView ){
            super(itemView);
            btnCat = itemView.findViewById(R.id.tvCategory);
        }
    }
}
