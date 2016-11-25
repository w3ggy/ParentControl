package com.abramov.artyom.parentcontrol.ui.calls;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abramov.artyom.parentcontrol.R;
import com.abramov.artyom.parentcontrol.domain.Call;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallsAdapter extends RecyclerView.Adapter<CallsAdapter.CallViewHolder>{
    private List<Call> mCalls;

    public CallsAdapter(List<Call> calls) {
        mCalls = calls;
    }

    @Override
    public CallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call, parent, false);
        return new CallViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mCalls == null ? 0 : mCalls.size();
    }

    @Override
    public void onBindViewHolder(CallViewHolder holder, int position) {
        holder.init(mCalls.get(position));
    }

    class CallViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.call_name)
        TextView mName;

        @BindView(R.id.call_date)
        TextView mDate;

        @BindView(R.id.call_duration)
        TextView mDuration;

        @BindView(R.id.call_number)
        TextView mNumber;

        public CallViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void init(Call call) {
            mName.setText(call.getName());
            mDate.setText(new SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.getDefault()).format(call.getDate()));
            mNumber.setText(call.getNumber());
        }
    }
}
