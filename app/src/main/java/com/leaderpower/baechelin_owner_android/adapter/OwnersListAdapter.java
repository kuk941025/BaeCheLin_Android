package com.leaderpower.baechelin_owner_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.Retrofit.RetroClient;
import com.leaderpower.baechelin_owner_android.activity.OrderActivity;
import com.leaderpower.baechelin_owner_android.app.BaechelinApp;
import com.leaderpower.baechelin_owner_android.model.BusinessInfo;
import com.leaderpower.baechelin_owner_android.model.BusinessOwners;
import com.leaderpower.baechelin_owner_android.model.Notification;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OwnersListAdapter extends RecyclerView.Adapter<OwnersListAdapter.ownerViewHolder> {
    private ArrayList<OwnerItem> ownerList;
    private Context mContext;
    private FirebaseFirestore db;
    private String tokenId;

    public OwnersListAdapter(ArrayList<OwnerItem> ownerList, Context context, FirebaseFirestore db) {
        this.ownerList = ownerList;
        this.mContext = context;
        this.db = db;
        this.tokenId = FirebaseInstanceId.getInstance().getId();
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


        //is this device listening to alarm?

        List<Notification> tokens = ownerItem.getToken();
        ownerViewHolder.switchAlarm.setChecked(false);
        for (Notification token : tokens) {
            if (token.getToken_id().equals(tokenId)){
                ownerViewHolder.switchAlarm.setChecked(true);
                break;
            }
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
        void onSwitchClicked() {
            final OwnerItem owner = ownerList.get(getLayoutPosition());
            DocumentReference dbRef = db.collection("owner").document(owner.getOid()).collection("notifications").document(tokenId);

            if (switchAlarm.isChecked()) {
                final String token = BaechelinApp.getInstance().getRegisteration_Token();
                //Checked, save token id to firestore
                Map<String, Object> params = new HashMap<>();
                final Date curDate = new Date();
                params.put("corp_name", owner.getCorp_name());
                params.put("shop_name", owner.getShop_name());
                params.put("uid", owner.getUid());
                params.put("created_at", curDate);
                params.put("token", token);
                dbRef.set(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Notification updated = new Notification();
                        updated.setToken_id(tokenId);
                        updated.setCorp_name(owner.getCorp_name());
                        updated.setUid(owner.getUid());
                        updated.setCreated_at(curDate);
                        updated.setToken(BaechelinApp.getInstance().getRegisteration_Token());

                        ownerList.get(getLayoutPosition()).getToken().add(updated);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "네트워크 오류가 발생했습니다. " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                //remove token_id from firebase
                dbRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Iterator iterator = ownerList.get(getLayoutPosition()).getToken().iterator();
                        while (iterator.hasNext()) {
                            Notification item = (Notification) iterator.next();
                            if (item.getToken_id().equals(tokenId)) {
                                iterator.remove();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "네트워크 오류가 발생했습니다. " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        }
    }
}
