package com.leaderpower.baechelin_owner_android.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.app.BaechelinApp;
import com.leaderpower.baechelin_owner_android.util.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_edit_id)
    EditText editId;
    @BindView(R.id.login_edit_pw)
    EditText editPw;
    @BindView(R.id.login_btn_sign_in)
    Button btnSignIn;
    @BindView(R.id.login_progressbar)
    ProgressBar progressLogin;
    @BindView(R.id.login_check_save)
    CheckBox chkSave;

    private BaechelinApp baechelinApp;
    private FirebaseAuth mAuth;
    private SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        baechelinApp = BaechelinApp.getInstance();

        btnSignIn.setOnClickListener(onClickListener);
        sharedPrefManager = new SharedPrefManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            BaechelinApp.setCurrentUser(currentUser);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        }
        else {
            //if not logged in and check whether value is saved;
            boolean isSaved = sharedPrefManager.isSaved();
            chkSave.setChecked(isSaved);
            if (isSaved){
                editId.setText(sharedPrefManager.getEmail());
                editPw.setText(sharedPrefManager.getPassword());
            }
        }


    }
    private void setViewEnabled(boolean isEnabled){
        editId.setEnabled(isEnabled);
        editPw.setEnabled(isEnabled);
        btnSignIn.setEnabled(isEnabled);
        if (!isEnabled) progressLogin.setVisibility(View.VISIBLE);
        else progressLogin.setVisibility(View.GONE);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String email = editId.getText().toString();
            final String password = editPw.getText().toString();

            setViewEnabled(false);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
//                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                        BaechelinApp.setCurrentUser(FirebaseAuth.getInstance().getCurrentUser());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        setViewEnabled(true);

                        //save
                        if (chkSave.isChecked()){
                            sharedPrefManager.setEmailAndPassword(email, password);
                        }
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                        Log.d("FAIL", "FAILED");
                        setViewEnabled(true);
                    }
                }
            });

        }
    };

    @OnClick(R.id.login_txt_sign_up)
    void onSignUpClicked(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://localhost:3000/signup"));
        startActivity(intent);
    }

    @OnClick(R.id.login_check_save)
    void onCheckClicked(){
        if (chkSave.isEnabled()) Toast.makeText(this, "checked", Toast.LENGTH_LONG).show();
        sharedPrefManager.setSaveAccount(chkSave.isChecked());
    }
}
