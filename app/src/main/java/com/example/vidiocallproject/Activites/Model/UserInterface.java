package com.example.vidiocallproject.Activites.Model;

import android.webkit.JavascriptInterface;

import com.example.vidiocallproject.Activites.call_activity;

public class UserInterface {

    call_activity call_activity;

    public UserInterface(call_activity call_activity){
        this.call_activity =call_activity;

    }
    @JavascriptInterface
    public void OnPeerConnected()
    {
        call_activity.onPeerconnected();
    }
}
