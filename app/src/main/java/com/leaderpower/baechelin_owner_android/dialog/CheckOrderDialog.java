package com.leaderpower.baechelin_owner_android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.leaderpower.baechelin_owner_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckOrderDialog extends Dialog {
    @BindView(R.id.dialog_accept_order_time)
    TextView txtOrderTime;

    private int delivery_time;
    private CheckOrderDialogListener mListener;

    public CheckOrderDialog(Context context, CheckOrderDialogListener listener) {
        super(context);

        this.delivery_time = 30;
        this.mListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.dialog_accept_order);
        ButterKnife.bind(this);

        txtOrderTime.setText(delivery_time + "분");
    }

    @OnClick(R.id.dialog_accept_order_btn_plus)
    void onPlusClicked(){
        if (delivery_time + 5 > 120){
            Toast.makeText(getContext(), "배달시간이 120분 초과할 수 없습니다", Toast.LENGTH_LONG).show();
        }
        else {
            delivery_time += 5;
            txtOrderTime.setText(delivery_time + "분");
        }
    }

    @OnClick(R.id.dialog_accept_order_btn_minus)
    void onMinusClicked(){
        if (delivery_time - 5 <= 0) {
            Toast.makeText(getContext(), "배달시간이 0분 아래로 설정할 수 없습니다.", Toast.LENGTH_LONG).show();
        }
        else {
            delivery_time -= 5;
            txtOrderTime.setText(delivery_time + "분");
        }
    }

    @OnClick(R.id.dialog_accept_order_close)
    void onCloseClicked() {
        this.dismiss();
    }

    @OnClick(R.id.dialog_accept_order_confirm)
    void onConfirmClicked(){
        mListener.onConfirmClicked(delivery_time);
        this.dismiss();
    }
}
