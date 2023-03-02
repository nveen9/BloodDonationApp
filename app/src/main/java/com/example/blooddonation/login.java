package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class login extends AppCompatActivity {

    EditText email,password;
    TextView signup;
    Button login;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressBar probar;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        probar = findViewById(R.id.loading);
        signup = findViewById(R.id.signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString();
                String pass = password.getText().toString();

                if (mail.isEmpty()){
                    email.setError("Enter the Email");
                    email.requestFocus();
                }
                else if (pass.isEmpty()){
                    password.setError("Enter the Password");
                    password.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    email.setError("Provide valid Email");
                    email.requestFocus();
                }
                else {
                    userAvailable();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, registerFragMain.class);
                startActivity(intent);
            }
        });
    }

    private void userAvailable() {
        String mail = email.getText().toString();
        String pass = password.getText().toString();

        probar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    probar.setVisibility(View.GONE);
                    userID = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("admin").document(userID);
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value!=null && value.exists()){
                                String type = value.getString("type");
                                if (type.equals("admin")){
                                    Toast.makeText(login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(login.this, admin.class);
                                    startActivity(intent);
                                }
                            }else {
                                Toast.makeText(login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(login.this, donorAccount.class);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    Toast.makeText(login.this, "User or Password Incorrect", Toast.LENGTH_SHORT).show();
                    probar.setVisibility(View.GONE);
                }
            }
        });
    }
}