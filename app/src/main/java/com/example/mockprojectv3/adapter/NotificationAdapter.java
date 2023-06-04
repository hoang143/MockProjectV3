package com.example.mockprojectv3.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mockprojectv3.databinding.ItemNotificationBinding;
import com.example.mockprojectv3.viewmodel.NotificationViewModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<NotificationViewModel> mListNoti;

    public void setData(List<NotificationViewModel> list) {
        this.mListNoti = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemNotificationBinding itemNotificationBinding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NotificationViewHolder(itemNotificationBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {
        NotificationViewModel notification = mListNoti.get(position);
        if (notification == null) {
            return;
        }
        holder.itemNotificationBinding.setNotificationViewModel(notification);
    }

    @Override
    public int getItemCount() {
        if (mListNoti != null) {
            return mListNoti.size();
        }
        return 0;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private ItemNotificationBinding itemNotificationBinding;

        public NotificationViewHolder(@NonNull ItemNotificationBinding itemNotificationBinding) {
            super(itemNotificationBinding.getRoot());
            this.itemNotificationBinding = itemNotificationBinding;
        }
    }
}
