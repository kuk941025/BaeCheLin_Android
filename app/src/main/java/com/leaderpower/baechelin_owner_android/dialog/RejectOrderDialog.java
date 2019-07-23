package com.leaderpower.baechelin_owner_android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.leaderpower.baechelin_owner_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RejectOrderDialog extends Dialog {
    @BindView(R.id.dialog_reject_edit_reason)
    EditText editReason;


    private RejectOrderDialogListener mListener;

    public RejectOrderDialog(Context context, RejectOrderDialogListener listener) {
        super(context);
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.dialog_reject_order);
        ButterKnife.bind(this);


    }



    @OnClick(R.id.dialog_reject_confirm)
    void onConfirmClicked(){
        String strReason = editReason.getText().toString();
        if (strReason.equals("")) {
            Toast.makeText(getContext(), "거절 사유를 입력해주세요.", Toast.LENGTH_LONG).show();
        }
        else {
            mListener.onRejectClicked(editReason.getText().toString());
            this.dismiss();
        }

    }

    @OnClick(R.id.dialog_reject_cancel)
    void onCancelClicked(){
        this.dismiss();
    }

    @OnClick(R.id.dialog_reject_order_close)
    void onCloseClicked(){
        this.dismiss();
    }
}
