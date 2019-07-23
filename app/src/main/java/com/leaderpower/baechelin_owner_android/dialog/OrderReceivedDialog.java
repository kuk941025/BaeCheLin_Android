package com.leaderpower.baechelin_owner_android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.leaderpower.baechelin_owner_android.R;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderReceivedDialog extends Dialog {
    @BindView(R.id.dialog_order_received_food)
    TextView txtFood;
    @BindView(R.id.dialog_order_received_price)
    TextView txtPrice;

    OrderReceivedDialogListener mListener;
    public OrderReceivedDialog(Context context, OrderReceivedDialogListener listener, String strFood, String strPrice) {
        super(context);
        this.mListener = listener;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order_received);

    }

    @OnClick(R.id.dialog_order_received_btn_show)
    void onShowOrderClicked(){
        mListener.onCheckOrderClicked();
    }

    @OnClick(R.id.dialog_order_received_close)
    void onCloseClicked(){
        this.dismiss();
        mListener.onCancelClicked();
    }
}
