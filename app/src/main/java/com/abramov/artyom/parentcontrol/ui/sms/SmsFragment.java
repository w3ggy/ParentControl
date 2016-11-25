package com.abramov.artyom.parentcontrol.ui.sms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abramov.artyom.parentcontrol.R;
import com.abramov.artyom.parentcontrol.domain.Sms;
import com.abramov.artyom.parentcontrol.ui.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SmsFragment extends BaseFragment {
    @BindView(R.id.sms_list)
    RecyclerView mRecyclerView;

    public static SmsFragment getInstance() {
        return new SmsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        addSubscription(getModel().getItemsObservable(Sms.class).subscribe(sms -> {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView.setAdapter(new SmsAdapter(sms));
        }));
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
