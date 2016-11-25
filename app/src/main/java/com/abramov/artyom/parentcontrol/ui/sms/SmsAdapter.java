package com.abramov.artyom.parentcontrol.ui.sms;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abramov.artyom.parentcontrol.R;
import com.abramov.artyom.parentcontrol.domain.Sms;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder> {
    private List<Sms> mSmsList;

    public SmsAdapter(List<Sms> smsList) {
        mSmsList = smsList;
    }

    @Override
    public SmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms, parent, false);
        return new SmsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SmsViewHolder holder, int position) {
        holder.init(mSmsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSmsList == null ? 0 : mSmsList.size();
    }

    class SmsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sms_name)
        TextView mName;

        @BindView(R.id.sms_text)
        TextView mText;

        public SmsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void init(Sms sms) {
            mName.setText(sms.getAuthor());
            mText.setText(sms.getMessage());
        }
    }
}
