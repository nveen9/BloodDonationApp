package com.example.blooddonation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class cardUpload extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<card> list;
    cardUploadAdapter adapter;
    TextView resulttTxt;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_upload);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));

        recyclerView = findViewById(R.id.list);
        resulttTxt = findViewById(R.id.resultTxt);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        list = new ArrayList<>();
        adapter = new cardUploadAdapter(this, list);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        changeListner();

    }

    private void changeListner() {
        db.collection("cardupload").orderBy("num", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()){
                    if (dc.getType() == DocumentChange.Type.ADDED){
                        list.add(dc.getDocument().toObject(card.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

}