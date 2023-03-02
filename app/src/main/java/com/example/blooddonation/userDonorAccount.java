package com.example.blooddonation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class userDonorAccount extends AppCompatActivity {

    TextView location,bt,name;
    Button direction,call;
    String userID;
    FirebaseFirestore db;
    Uri phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_donor_account);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));

        location = findViewById(R.id.location);
        bt = findViewById(R.id.bloodtype);
        name = findViewById(R.id.name);
        direction = findViewById(R.id.direction);
        call = findViewById(R.id.call);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        userID = intent.getStringExtra("UserID");

        DocumentReference documentReference = db.collection("donor").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                bt.setText(value.getString("blood_type"));
                name.setText(value.getString("name"));
                location.setText(value.getString("address"));
                String number =  value.getString("contact_no");
                Log.d("1234",number);

                phone = Uri.parse("tel:" + number);
            }
        });

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DocumentReference documentReference = db.collection("donor").document(userID);
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            Double lati =  value.getDouble("latitude");
                            Double longi =  value.getDouble("longitude");
                            if (lati==null && longi==null){
                                Toast.makeText(userDonorAccount.this, "Cannot find any location", Toast.LENGTH_SHORT).show();
                            }else {
                                Uri uri = Uri.parse("http://maps.google.com/maps?q=loc:" + lati + "," +longi);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);
                            }
                        }
                    });
                }catch (ActivityNotFoundException ex){
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, phone);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(userDonorAccount.this, "Invalid Number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}