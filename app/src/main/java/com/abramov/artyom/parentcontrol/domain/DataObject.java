package com.abramov.artyom.parentcontrol.domain;

import java.util.ArrayList;
import java.util.List;

public class DataObject {
    private static final int N = 100;
    private List<Sms> mSmsList = new ArrayList<>();
    private List<Call> mCallList = new ArrayList<>();
    private List<Loc> mLocList = new ArrayList<>();

    public DataObject(List<Sms> smsList, List<Call> callList, List<Loc> locList) {
        mSmsList = smsList.subList(0, smsList.size() > N ? N : smsList.size() - 1);
        mCallList = callList.subList(0, callList.size() > N ? N : callList.size() - 1);;
        mLocList = locList;
    }

    public List<Sms> getSmsList() {
        return mSmsList;
    }

    public void setSmsList(List<Sms> smsList) {
        mSmsList = smsList;
    }

    public List<Call> getCallList() {
        return mCallList;
    }

    public void setCallList(List<Call> callList) {
        mCallList = callList;
    }

    public List<Loc> getLocList() {
        return mLocList;
    }

    public void setLocList(List<Loc> locList) {
        mLocList = locList;
    }
}
