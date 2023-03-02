package com.example.blooddonation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class donorListing extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<donor> list;
    listAdapter adapter;
    TextView resulttTxt;
    SearchView searchView;
    FirebaseFirestore db;

    imgSliderAdapter adapter1;
    SliderView sliderView;

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_listing);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        recyclerView = findViewById(R.id.list);
        resulttTxt = findViewById(R.id.resultTxt);
        searchView = findViewById(R.id.searchbr);
        constraintLayout = findViewById(R.id.constraintlist);

        list = new ArrayList<donor>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new listAdapter(this, list);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        changeListner();

        sliderView = findViewById(R.id.sliderWork);

        String userID = "ITRLUs26IES9aC5Ndsg5E9NYqjY2";

        DocumentReference documentReference = db.collection("admin").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("ResourceType")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String w1 = value.getString("ad1");
                String w2 = value.getString("ad2");
                String w3 = value.getString("ad3");

                if (w1 == null && w2 == null && w3 == null){
                    String[] sliderList = {};

                    adapter1 = new imgSliderAdapter(sliderList);
                    sliderView.setSliderAdapter(adapter1);

                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) searchView.getLayoutParams();
                    params.topMargin = 200;

                    ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
                    params1.topMargin = 350;

                    constraintLayout.setBackgroundResource(R.drawable.listbackempty);
                }else {
                    String[] sliderList = {w1,w2,w3};

                    adapter1 = new imgSliderAdapter(sliderList);
                    sliderView.setSliderAdapter(adapter1);
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                    sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                    sliderView.startAutoCycle();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

    }

    private void filter(String newText) {
        ArrayList<donor> filterList = new ArrayList<>();

        for (donor itemS: list){
            if (itemS.getBlood_type().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(itemS);
            }
        }
        if (filterList.isEmpty()){
            String txt = "Sorry, No results found!";
            resulttTxt.setText(txt);
        }else {
            resulttTxt.setText(null);
        }
        adapter.fltrList(filterList);
    }

    private void changeListner() {
        db.collection("donor").orderBy("blood_type", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()){
                    if (dc.getType() == DocumentChange.Type.ADDED){
                        SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyy");
                        long daysGap = 0;

                        String before = dc.getDocument().get("date").toString();

                        Calendar calendar = Calendar.getInstance();
                        String now = format.format(calendar.getTime());

                        Date dt1 = null;
                        Date dt2 = null;

                        try {
                            dt1 = format.parse(before);
                            dt2 = format.parse(now);

                            long days = dt2.getTime() - dt1.getTime();
                            Log.d("123", String.valueOf(days));

                            daysGap = days/(24*60*60*1000);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (daysGap>20 || before.equals("Never")){
                            list.add(dc.getDocument().toObject(donor.class));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

}