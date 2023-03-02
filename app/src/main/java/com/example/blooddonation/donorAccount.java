package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import cn.iwgang.countdownview.CountdownView;

public class donorAccount extends AppCompatActivity {

    TextView location,bt,map,currentLocation,last,points,nameD;
    Button logout;
    ImageView uploadCrd;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    StorageReference storageRef;

    String userID,name,date;
    Uri imgUr;

    long id = 0;

    CountdownView cd;
    ConstraintLayout nextdonate;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_account);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));

        map = findViewById(R.id.map);
        location = findViewById(R.id.location);
        nameD = findViewById(R.id.name);
        bt = findViewById(R.id.bloodtype);
        currentLocation = findViewById(R.id.currentLocation);
        uploadCrd = findViewById(R.id.uploadCrd);
        cd = findViewById(R.id.countdownView);
        last = findViewById(R.id.last);
        nextdonate = findViewById(R.id.nextdonate);
        logout = findViewById(R.id.logout);
        points = findViewById(R.id.points);

        storageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("donor").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                bt.setText(value.getString("blood_type"));
                location.setText(value.getString("address"));
                nameD.setText(value.getString("name"));
                name = value.getString("name");
                double point = value.getDouble("points");
                points.setText(String.valueOf((int) point));
                last.setText("Last Donation: "+value.getString("date"));

                String lastt = value.getString("date");

                if (lastt.equals("Never")){
                    nextdonate.setVisibility(View.GONE);
                }else {
                    try {
                        Date now = new Date();

                        SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyy");
                        String before = value.getString("date");
                        Date dt1;
                        dt1 = format.parse(before);
                        long cc =dt1.getTime();
                        long abc =cc+(30*24*60*60*1000L);
                        Log.d("123", String.valueOf(abc)+ "cc " +cc);

                        long currentDate = now.getTime();
                        long countdownDonateDate = abc - currentDate;
                        cd.start(countdownDonateDate);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        db.collection("cardupload").orderBy("id").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()){
                    if (dc.getType() == DocumentChange.Type.ADDED){
                        String id = dc.getDocument().get("id").toString();
                        if (id.equals(userID)){
                            String pic = dc.getDocument().get("card").toString();
                            Picasso.get().load(pic).into(uploadCrd);
                        }
                    }
                }
            }
        });

//        StorageReference ImagesRefPP = storageRef.child("donor/" + userID+"/" + "card");
//        ImagesRefPP.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(uploadCrd);
//            }
//        });

        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(donorAccount.this, "Points Add when Upload Donation Card", Toast.LENGTH_LONG).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(donorAccount.this,MainActivity.class);
                startActivity(intent);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(donorAccount.this,locationPick.class);
                intent.putExtra("Donor","DEF");
                startActivity(intent);
            }
        });

        uploadCrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pic = new Intent();
                pic.setAction(Intent.ACTION_GET_CONTENT);
                pic.setType("image/*");
                startActivityForResult(pic,2);
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(donorAccount.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    RealtimeLocation();
                } else {
                    ActivityCompat.requestPermissions(donorAccount.this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2 && resultCode==RESULT_OK && data !=null){
            imgUr = data.getData();
            uploadCrd.setImageURI(imgUr);
            saveCrd();
        }
    }

    private void saveCrd() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        CollectionReference collection = db.collection("cardupload");
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    id = task.getResult().size();
                    Log.d("123", String.valueOf(id));
                }
            }
        });

        StorageReference ImagesRefP = storageRef.child("donor/" + userID+"/" + UUID.randomUUID().toString()+"/" + "card");

        ImagesRefP.putFile(imgUr).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ImagesRefP.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("card", uri.toString());
                        user.put("name", name);
                        user.put("id", mAuth.getUid());
                        user.put("date","");
                        user.put("num", String.valueOf(id+1));
                        user.put("status", "not");
                        DocumentReference documentReference = db.collection("cardupload").document(String.valueOf(id+1));
                        documentReference.set(user);
                        Log.d("12",uri.toString());
                        progressDialog.dismiss();
                        Toast.makeText(donorAccount.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(donorAccount.this, "Image Uploaded Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                double progressDialogPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploading " + (int)progressDialogPercent + "%");
            }
        });
    }

    private void RealtimeLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Location> task) {
                Location locations = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(donorAccount.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(locations.getLatitude(), locations.getLongitude(), 1);
                        double latitude = addresses.get(0).getLatitude();
                        double longitude = addresses.get(0).getLongitude();
                        String locality = addresses.get(0).getLocality();
                        String address = addresses.get(0).getAddressLine(0);
                        location.setText(address);

                        FirebaseUser uid = FirebaseAuth.getInstance().getCurrentUser();
                        String userID = uid.getUid();
                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("donor").document(userID);
                        Map<String,Object> userData = new HashMap<>();
                        userData.put("latitude",latitude);
                        userData.put("longitude",longitude);
                        if (addresses.get(0).getLocality()==null){
                            userData.put("locality","");
                        }else {
                            userData.put("locality",locality);
                        }
                        userData.put("address",address);
                        documentReference.update(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(donorAccount.this, "Location set Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}