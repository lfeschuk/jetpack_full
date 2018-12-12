package com.example.leonid.jetpack.jetpack_shlihim;


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

import com.example.leonid.jetpack.jetpack_shlihim.Objects.DeliveryGuys;
import com.example.leonid.jetpack.jetpack_shlihim.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class LoginActivity extends AppCompatActivity {

    final static String TAG = "LoginActivity";
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;
    EditText et_phone;
    EditText et_pass;
    EditText et_region;
    Button sign_up;
    String password = "";
    String email = "";
    String name = "";
    long index;
    Boolean sign_in = true;
    String phone = "";
    byte bitmask_edit = 0;

    FirebaseUser user;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Log.d(TAG,"OnCreate");
        //  FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
       // mAuth.signOut();
        updateUI(mAuth.getCurrentUser());

        et_phone = findViewById(R.id.edittext_phone);
        et_pass = findViewById(R.id.edittext_password);
        final EditText et_name = findViewById(R.id.edittext_name);
        et_region = findViewById(R.id.edittext_region_number);
        sign_up = findViewById(R.id.button_sign_up);
        sign_up.setClickable(true);
        final Button sign_in_choose = findViewById(R.id.sign_in_choose);
        sign_in_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sign_in)
                {
                    sign_in = false;
                    et_region.setVisibility(View.VISIBLE);
                    et_name.setVisibility(View.VISIBLE);
                    sign_up.setText("הרשם");
                    sign_in_choose.setText("הכנס");

                }
                else
                {
                    sign_in = true;
                    et_region.setVisibility(View.GONE);
                    et_name.setVisibility(View.GONE);
                    sign_up.setText("הכנס");
                    sign_in_choose.setText("הרשם");
                }

            }
        });




        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                phone = editable.toString();
                email = editable.toString() + "@gmail.com";
            }
        });

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = editable.toString();
                bitmask_edit |= (1 << 1);
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
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!phone.startsWith("05") || phone.length() != 10)
                {
                    Toast.makeText(LoginActivity.this, "אנא הכנס מספר טלפון חוקי", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals(""))
                {
                    Toast.makeText(LoginActivity.this, "אנא הכנס סיסמא", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sign_in)
                {
                    sign_in(email,password);
                }
                else
                    {
                        sign_up(email, password);
                }
            }
        });


    }

    void sign_in (final String email,final String password)
    {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });

    }
    public void sign_up(final String email,final String password)
    {
        if (name.equals(""))
        {
            Toast.makeText(LoginActivity.this, "אנא הכנס שם מלא", Toast.LENGTH_SHORT).show();
            return;
        }
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
                            Log.d(TAG,"name: " + name);

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("deliveryGuyIndex");
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    long index = (long) dataSnapshot.getValue();
                                    long index_new = index;
                                    index_new++;
                                    Log.d(TAG,"set index: " + index_new);
                                    mDatabase.setValue(index_new);
                                    createDeliveryGuy(index,phone,name);
                                    User um = new User(name,phone,String.valueOf(index));
                                    um.setEnable_notification(true);
                                    mDatabase = FirebaseDatabase.getInstance().getReference("User_Delivery");
                                    mDatabase.child(phone).setValue(um);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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
    }
    void createDeliveryGuy(long index,String phone,String name)
    {
        DeliveryGuys dg = new DeliveryGuys();
        String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());
        dg.setTimeBeFree(timeStamp);
        dg.setIndex(index);
        dg.setIndex_string(String.valueOf(index));
        dg.setName(name);
        dg.setPhone(phone);
        mDatabase = FirebaseDatabase.getInstance().getReference("Delivery_Guys");
        mDatabase.child(dg.getIndex_string()).setValue(dg);


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
