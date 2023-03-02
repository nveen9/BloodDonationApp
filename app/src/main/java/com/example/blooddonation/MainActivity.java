package com.example.blooddonation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    Button login;
    TextView enter,guest;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        enter = findViewById(R.id.enter);
        guest = findViewById(R.id.guest);
        login = findViewById(R.id.login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userID = mAuth.getUid();

        if (mAuth.getCurrentUser() != null){
            DocumentReference documentReference = db.collection("admin").document(userID);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value!=null && value.exists()){
                        String type = value.getString("type");
                        if (type.equals("admin")){
                            Intent intent = new Intent(MainActivity.this, admin.class);
                            startActivity(intent);
                        }
                    }else {
                        Intent intent = new Intent(MainActivity.this,donorAccount.class);
                        startActivity(intent);
                    }
                }
            });
        }

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,registerFragMain.class);
                startActivity(intent);
            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,donorListing.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,login.class);
                startActivity(intent);
            }
        });
    }
}