package com.example.singlemind.Controllers;

public class FlagEvent {

    public Boolean mEventFlag;

    public FlagEvent(Boolean flag) {
        mEventFlag = flag;
    }

    public Boolean getFlag() {
        return mEventFlag;
    }
}