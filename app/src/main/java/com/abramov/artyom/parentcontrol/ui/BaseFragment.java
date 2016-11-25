package com.abramov.artyom.parentcontrol.ui;

import android.support.v4.app.Fragment;

import com.abramov.artyom.parentcontrol.MyApplication;
import com.abramov.artyom.parentcontrol.model.BaseModel;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseFragment extends Fragment{
    private CompositeSubscription mCompositeSubscription;

    @Inject
    BaseModel mBaseModel;

    @Override
    public void onStart() {
        super.onStart();

        mCompositeSubscription = new CompositeSubscription();
        ((MyApplication) getActivity().getApplication()).getInjector().inject(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBaseModel = null;

        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription.clear();
            mCompositeSubscription = null;
        }
    }

    public void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    public BaseModel getModel() {
        return mBaseModel;
    }
}
