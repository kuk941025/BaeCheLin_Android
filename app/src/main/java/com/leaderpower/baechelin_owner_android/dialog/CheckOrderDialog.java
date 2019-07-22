package com.leaderpower.baechelin_owner_android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.leaderpower.baechelin_owner_android.R;

import butterknife.BindView;

public class CheckOrderDialog extends Dialog {

    public CheckOrderDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.dialog_accept_order);


    }
}
