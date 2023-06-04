package com.example.mockprojectv3.viewmodel;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModel;

public class NotificationViewModel extends ViewModel{
    private String notification;

    public NotificationViewModel(String notification) {
        this.notification = notification;
    }
    public String getNotification() {
        return notification;
    }
    public void setNotification(String notification) {
        this.notification = notification;
    }

    public void ShowLogMessage(){
        Log.e("Hoang", "Button click");
    }
    public void ShowLogMessage2(String message){
        Log.e("Hoang", message);
    }
    public void ShowLogMessage3(View view, String message){
        Log.e("Hoang", message);
    }
    public void ShowLogMessage4(ProfileViewModel profileViewModel){
        if (profileViewModel != null) {
            Log.e("Hoang","this is" +  profileViewModel.getUserName());
        } else {
            Log.e("Hoang","profileViewModel is null");
        }
    }
}
