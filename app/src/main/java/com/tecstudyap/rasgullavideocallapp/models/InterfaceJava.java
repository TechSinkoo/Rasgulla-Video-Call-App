package com.tecstudyap.rasgullavideocallapp.models;

import android.webkit.JavascriptInterface;

import com.tecstudyap.rasgullavideocallapp.activities.CallActivity;

public class InterfaceJava {

    CallActivity callActivity;

    public InterfaceJava(CallActivity callActivity) {
        this.callActivity = callActivity;
    }

    @JavascriptInterface
    public void onPeerConnected(){
       callActivity.onPeerConnected();
    }
}
