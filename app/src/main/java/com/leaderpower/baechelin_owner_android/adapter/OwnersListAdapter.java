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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.Retrofit.RetroClient;
import com.leaderpower.baechelin_owner_android.activity.OrderActivity;
import com.leaderpower.baechelin_owner_android.app.BaechelinApp;
import com.leaderpower.baechelin_owner_android.model.BusinessInfo;
import com.leaderpower.baechelin_owner_android.model.BusinessOwners;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import java.util.ArrayList;
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
//        ownerViewHolder.switchAlarm.setChecked(false);
//        if (ownerItem.getToken() != null && ownerItem.getToken().size() > 0) {
//            for (String id : ownerItem.getToken()) {
//                if (id == tokenId) {
//                    ownerViewHolder.switchAlarm.setChecked(true);
//                    break;
//                }
//            }
//        }

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

//            OwnerItem owner = ownerList.get(getLayoutPosition());
//            final String oid = owner.getOid();
//            BusinessInfo businessInfo = BaechelinApp.getInstance().getBusinessInfo();
//            List<BusinessOwners> authOwners = businessInfo.getOwners();
//
//            if (switchAlarm.isChecked()) {
//                //if not checked, add token id to database
//
//                for (BusinessOwners authOwner : authOwners ){
//                    if (authOwner.getOid().equals(oid)){
//                        if (authOwner.getToken() == null ) authOwner.setToken(new ArrayList<String>());
//                        authOwner.getToken().add(tokenId);
//                        break;
//                    }
//                }
//
//
//            } else {
//                //remove id from base
//
//                for (BusinessOwners authOwner : authOwners ){
//
//                    if (authOwner.getOid().equals(oid)){
//                        List<String> tokenIDs = authOwner.getToken();
//                        Iterator iterator = tokenIDs.iterator();
//                        while (iterator.hasNext()){
//                            String id = (String) iterator.next();
//                            if (id.equals(tokenId)) iterator.remove();
//                        }
//                    }
//                }
//            }
//
//            Map<String, Object> updateField = new HashMap<>();
//            updateField.put("owners", authOwners);
//            db.collection("auths").document(owner.getUid()).update(updateField)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d("TAG", "SUCCESS");
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("TAG", "FAILED " + e.getMessage());
//                }
//            });
        }
    }
}
