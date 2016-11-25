package com.abramov.artyom.parentcontrol.ui.calls;

import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abramov.artyom.parentcontrol.R;
import com.abramov.artyom.parentcontrol.domain.Call;
import com.abramov.artyom.parentcontrol.ui.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallsFragment extends BaseFragment {
    @BindView(R.id.calls_list)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calls, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        addSubscription(getModel().getItemsObservable(Call.class).subscribe(calls -> {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(new CallsAdapter(calls));
        }));
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
