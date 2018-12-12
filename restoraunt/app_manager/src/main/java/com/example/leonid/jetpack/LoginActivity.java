package com.example.leonid.jetpack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Objects.DataBaseManager;
import Objects.UserAdmin;

public class LoginActivity extends AppCompatActivity {

    final static String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    EditText et_email;
    EditText et_pass;
    Button sign_up;
    String password;
    String email;
    Boolean allow_click = false;
    Boolean allow_click2 = false;
    FirebaseUser user;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
         Log.d(TAG,"OnCreate");
      //  FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        updateUI(mAuth.getCurrentUser());


        et_email = findViewById(R.id.edittext_email);
        et_pass = findViewById(R.id.edittext_password);
        sign_up = findViewById(R.id.button_sign_up);
        sign_up.setClickable(false);

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                email = editable.toString();
                allow_click = true;
                sign_up.setClickable(allow_click && allow_click2);
            }
        });

        et_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password = editable.toString();
                allow_click2 = true;
                sign_up.setClickable(allow_click && allow_click2);
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_user(email,password);
            }
        });


    }

    void create_user (final String email,final String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success  email:" + email);
                            Toast.makeText(LoginActivity.this, "Authentication succeded.",
                                    Toast.LENGTH_SHORT).show();
                            user = mAuth.getCurrentUser();
                            updateUI(user);
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed." +
                                    task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
        UserAdmin um = new UserAdmin(user.getDisplayName(),email);
        um.setEnable_notification(true);
        DataBaseManager dbm = new DataBaseManager();
        dbm.writeUserAdmin(um);
    }
    void updateUI(FirebaseUser user)
    {
        if (user == null)
        {
            return;
        }
        else
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }
}
