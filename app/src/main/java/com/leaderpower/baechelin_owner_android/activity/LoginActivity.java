package com.leaderpower.baechelin_owner_android.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.app.BaechelinApp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_edit_id)
    EditText editId;
    @BindView(R.id.login_edit_pw)
    EditText editPw;
    @BindView(R.id.login_btn_sign_in)
    Button btnSignIn;
    @BindView(R.id.login_progressbar)
    ProgressBar progressLogin;

    private BaechelinApp baechelinApp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        baechelinApp = BaechelinApp.getInstance();

        btnSignIn.setOnClickListener(onClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Log.d("login", "null");
        }
        else {
            BaechelinApp.setCurrentUser(currentUser);
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
            String email = editId.getText().toString();
            String password = editPw.getText().toString();

            setViewEnabled(false);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    setViewEnabled(true);
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                        BaechelinApp.setCurrentUser(FirebaseAuth.getInstance().getCurrentUser());
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                        Log.d("FAIL", "FAILED");
                    }
                }
            });

        }
    };

}
