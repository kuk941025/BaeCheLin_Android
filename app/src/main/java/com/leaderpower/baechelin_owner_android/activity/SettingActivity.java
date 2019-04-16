package com.leaderpower.baechelin_owner_android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.app.BaechelinApp;
import com.leaderpower.baechelin_owner_android.model.BusinessInfo;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.setting_toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_txt_title)
    TextView txtToolTitle;
    @BindView(R.id.setting_txt_name)
    TextView txtName;
    @BindView(R.id.setting_txt_serial_num)
    TextView txtSerialNum;
    @BindView(R.id.setting_txt_phone)
    TextView txtPhone;
    @BindView(R.id.setting_txt_created_at)
    TextView txtCreatedAt;
    @BindView(R.id.setting_txt_email)
    TextView txtEmail;
    @BindView(R.id.toolbar_right_btn)
    Button btnToolbar;

    private BaechelinApp app = BaechelinApp.getInstance();
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);

        setToolbar();
        setSettingUI();

        mAuth = FirebaseAuth.getInstance();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        txtToolTitle.setText("사용자 정보");
        btnToolbar.setVisibility(View.GONE);

    }

    private void setSettingUI(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        BusinessInfo businessInfo = app.getBusinessInfo();

        txtEmail.setText(businessInfo.getEmail());
        txtName.setText(businessInfo.getName());
        txtSerialNum.setText(businessInfo.getCorp_serial_num());
        txtPhone.setText(businessInfo.getCorp_num());
        txtCreatedAt.setText(sdf.format(businessInfo.getCreated_at()));

    }

    @OnClick(R.id.setting_btn_sign_out)
    void onSignOutClicked() {
        mAuth.signOut();
        finish();
    }
}
