package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    Button signup;
    TextInputEditText email,password,blood,pnumber;
    ProgressBar probar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
//        signup = findViewById(R.id.signup);
//        email = findViewById(R.id.email);
//        password = findViewById(R.id.password);
//        blood = findViewById(R.id.blood);
//        pnumber = findViewById(R.id.pnumber);
//        probar = findViewById(R.id.loading);
//
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dataAdd();
//            }
//        });
    }

//    private void dataAdd() {
//        String mail = email.getText().toString();
//        String pass = password.getText().toString();
//        String bt = blood.getText().toString();
//        String num = pnumber.getText().toString();
//
//        probar.setVisibility(View.VISIBLE);
//
//        mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    String uid = user.getUid();
//
//                    DocumentReference documentReference = db.collection("donor").document(uid);
//                    Map<String,Object> userData = new HashMap<>();
//                    userData.put("user_id",uid);
//                    userData.put("blood_type",bt);
//                    userData.put("number",num);
//
//                    documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
//                            probar.setVisibility(View.GONE);
//                            Intent intent = new Intent(register.this,donorAccount.class);
//                            startActivity(intent);
//                        }
//                    });
//                }else {
//                    email.setError("Email already Exist");
//                    email.requestFocus();
//                    probar.setVisibility(View.GONE);
//                }
//            }
//        });
//
//    }
}