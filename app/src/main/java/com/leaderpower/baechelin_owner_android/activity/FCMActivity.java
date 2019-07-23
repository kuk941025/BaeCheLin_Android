package com.leaderpower.baechelin_owner_android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.dialog.OrderReceivedDialog;
import com.leaderpower.baechelin_owner_android.dialog.OrderReceivedDialogListener;

public class FCMActivity extends AppCompatActivity implements OrderReceivedDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OrderReceivedDialog receivedDialog = new OrderReceivedDialog(this, this, "test", "1000");
        receivedDialog.show();

    }

    @Override
    public void onCheckOrderClicked() {
        Intent intent = new Intent(FCMActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onCancelClicked() {
        this.finish();
    }
}
