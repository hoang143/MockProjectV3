package com.example.mockprojectv3.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mockprojectv3.R;
import com.example.mockprojectv3.adapter.NotificationAdapter;
import com.example.mockprojectv3.databinding.FragmentNotificationBinding;
import com.example.mockprojectv3.databinding.FragmentProfileBinding;
import com.example.mockprojectv3.viewmodel.HomeViewModel;
import com.example.mockprojectv3.viewmodel.NotificationViewModel;
import com.example.mockprojectv3.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    HomeViewModel homeViewModel;
    private RecyclerView rcNoti;
    private NotificationAdapter notificationAdapter;
    private FragmentNotificationBinding mFragmentNotificationBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentNotificationBinding = FragmentNotificationBinding.inflate(inflater, container, false);

        NotificationViewModel notificationViewModel = new NotificationViewModel("Button clicked!");
        mFragmentNotificationBinding.setFragmentNotificationViewModel(notificationViewModel);

        rcNoti = mFragmentNotificationBinding.rcNotification;
        rcNoti.setLayoutManager(new LinearLayoutManager(this.getContext()));

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        rcNoti.addItemDecoration(itemDecoration);
        notificationAdapter = new NotificationAdapter();

        rcNoti.setAdapter(notificationAdapter);
        return mFragmentNotificationBinding.getRoot();
    }

    private List<NotificationViewModel> getNotifications() {
        List<NotificationViewModel> lst = new ArrayList<>();
        lst.add(new NotificationViewModel("hoang 1"));
        lst.add(new NotificationViewModel("hoang 2"));
        lst.add(new NotificationViewModel("hoang 3"));
        lst.add(new NotificationViewModel("hoang 4"));
        lst.add(new NotificationViewModel("hoang 5"));
        lst.add(new NotificationViewModel("hoang 6"));
        lst.add(new NotificationViewModel("hoang 7"));
        lst.add(new NotificationViewModel("hoang 8"));
        lst.add(new NotificationViewModel("hoang 9"));
        lst.add(new NotificationViewModel("hoang 10"));
        lst.add(new NotificationViewModel("hoang 11"));
        lst.add(new NotificationViewModel("hoang 12"));
        lst.add(new NotificationViewModel("hoang 13"));
        lst.add(new NotificationViewModel("hoang 14"));
        lst.add(new NotificationViewModel("hoang 15"));
        lst.add(new NotificationViewModel("hoang 16"));
        lst.add(new NotificationViewModel("hoang 17"));
        lst.add(new NotificationViewModel("hoang 18"));
        lst.add(new NotificationViewModel("hoang 19"));
        lst.add(new NotificationViewModel("hoang 20"));
        return lst;
    }

    @Override
    public void onStart() {
        super.onStart();
        notificationAdapter.setData(getNotifications());
    }


}