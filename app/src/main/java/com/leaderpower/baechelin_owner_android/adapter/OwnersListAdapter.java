package com.leaderpower.baechelin_owner_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.Retrofit.RetroClient;
import com.leaderpower.baechelin_owner_android.activity.OrderActivity;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OwnersListAdapter extends RecyclerView.Adapter<OwnersListAdapter.ownerViewHolder> {
    private ArrayList<OwnerItem> ownerList;
    private Context mContext;
    public OwnersListAdapter(ArrayList<OwnerItem> ownerList, Context context) {
        this.ownerList = ownerList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ownerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_owner_card, viewGroup, false);
        return new ownerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ownerViewHolder ownerViewHolder, int i) {
        OwnerItem ownerItem = ownerList.get(i);
        ownerViewHolder.layoutComplete.setVisibility(View.VISIBLE);
        ownerViewHolder.layoutLoading.setVisibility(View.GONE);

        ownerViewHolder.txtAddress.setText(ownerItem.getAddress() + " " + ownerItem.getAddress_detail());
        ownerViewHolder.txtTitle.setText(ownerItem.getShop_name());
        Glide.with(mContext).load(ownerItem.getShop_image()).into(ownerViewHolder.img);

        if (ownerItem.getToken() == null || ownerItem.getToken().equals("")){
            ownerViewHolder.switchAlarm.setChecked(false);
        }
        else {
            ownerViewHolder.switchAlarm.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return ownerList.size();
    }

    class ownerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_owner_title)
        TextView txtTitle;
        @BindView(R.id.card_owner_address)
        TextView txtAddress;
        @BindView(R.id.card_alarm_switch)
        Switch switchAlarm;
        @BindView(R.id.card_owner_image)
        ImageView img;
        @BindView(R.id.card_owner_layout_complete)
        View layoutComplete;
        @BindView(R.id.card_owner_layout_loading)
        View layoutLoading;
        @BindView(R.id.card_owner)
        CardView cardView;

        public ownerViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }

        @OnClick(R.id.card_owner)
        void onCardClicked() {
            Intent intent = new Intent(mContext, OrderActivity.class);
            intent.putExtra("Owner", ownerList.get(getLayoutPosition()));
            mContext.startActivity(intent);

        }

        @OnClick(R.id.card_alarm_switch)
        void onSwitchClicked(){
            if (switchAlarm.isChecked()){
                //if not checked
                String token = FirebaseInstanceId.getInstance().getId();

                Toast.makeText(mContext, token, Toast.LENGTH_LONG).show();
            }
        }
    }
}
