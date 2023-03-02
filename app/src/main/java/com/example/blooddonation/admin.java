package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class admin extends AppCompatActivity {

    Button crd,logout;
    ImageView work1,work2,work3;

    Uri imgUr;
    String userID;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));

        crd = findViewById(R.id.crd);
        work1 = findViewById(R.id.work1);
        work2 = findViewById(R.id.work2);
        work3 = findViewById(R.id.work3);
        logout = findViewById(R.id.logout);

        storageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        StorageReference ImagesRef = storageRef.child("admin/" + userID+"/" + "1");
        ImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(work1);
            }
        });

        StorageReference ImagesRef1 = storageRef.child("admin/" + userID+"/" + "2");
        ImagesRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(work2);
            }
        });

        StorageReference ImagesRef2 = storageRef.child("admin/" + userID+"/" + "3");
        ImagesRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(work3);
            }
        });

        db.collection("cardupload").orderBy("status").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    String stat = dc.getDocument().getString("status");
                    if (stat.equals("not")) {
                        String name = dc.getDocument().getString("name");
                        Log.d("1234",name);

                        NotificationManagerCompat manager = NotificationManagerCompat.from(admin.this);

                        NotificationChannel channel = new NotificationChannel("1bb","channel",NotificationManager.IMPORTANCE_DEFAULT);
                        Intent intent = new Intent(admin.this, cardUpload.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(admin.this, 0, intent, PendingIntent.FLAG_MUTABLE);

                        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        String message = name + "'s donation details, Check on this. ";

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(admin.this,"1bb");
                        builder.setContentTitle("Donation");
                        builder.setContentText(message);
                        builder.setSmallIcon(R.drawable.ic_noti);
                        builder.setSound(uri);
                        builder.setAutoCancel(true);
                        builder.setContentIntent(pendingIntent);
                        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                        manager.createNotificationChannel(channel);
//                        manager.notify(1, builder.build());

                        SharedPreferences preferences = getSharedPreferences(admin.class.getSimpleName(), Context.MODE_PRIVATE);
                        int notific = preferences.getInt("number", 0);
                        manager.notify(notific, builder.build());
                        SharedPreferences.Editor editor = preferences.edit();
                        notific++;
                        editor.putInt("number", notific);
                        editor.commit();

                    }
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(admin.this,MainActivity.class);
                startActivity(intent);
            }
        });

        crd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this,cardUpload.class);
                startActivity(intent);
            }
        });

        work1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pic = new Intent();
                pic.setAction(Intent.ACTION_GET_CONTENT);
                pic.setType("image/*");
                startActivityForResult(pic,5);
            }
        });

        work2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pic = new Intent();
                pic.setAction(Intent.ACTION_GET_CONTENT);
                pic.setType("image/*");
                startActivityForResult(pic,6);
            }
        });

        work3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pic = new Intent();
                pic.setAction(Intent.ACTION_GET_CONTENT);
                pic.setType("image/*");
                startActivityForResult(pic,7);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==5 && resultCode==RESULT_OK && data !=null){
            imgUr = data.getData();
            work1.setImageURI(imgUr);
            saveimgWrk1();
        }
        if (requestCode==6 && resultCode==RESULT_OK && data !=null){
            imgUr = data.getData();
            work2.setImageURI(imgUr);
            saveimgWrk2();
        }
        if (requestCode==7 && resultCode==RESULT_OK && data !=null){
            imgUr = data.getData();
            work3.setImageURI(imgUr);
            saveimgWrk3();
        }
    }

    private void saveimgWrk1() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        StorageReference ImagesRefP = storageRef.child("admin/" + userID+"/" + "1");

        ImagesRefP.putFile(imgUr).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ImagesRefP.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DocumentReference documentReference = db.collection("admin").document(userID);
                        documentReference.update("ad1",uri.toString());
                        Log.d("12",uri.toString());
                        progressDialog.dismiss();
                        Toast.makeText(admin.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(admin.this, "Image Uploaded Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                double progressDialogPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploading " + (int)progressDialogPercent + "%");
            }
        });
    }

    private void saveimgWrk2() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        StorageReference ImagesRefP = storageRef.child("admin/" + userID+"/" + "2");

        ImagesRefP.putFile(imgUr).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ImagesRefP.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DocumentReference documentReference = db.collection("admin").document(userID);
                        documentReference.update("ad2",uri.toString());
                        Log.d("12",uri.toString());
                        progressDialog.dismiss();
                        Toast.makeText(admin.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(admin.this, "Image Uploaded Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                double progressDialogPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploading " + (int)progressDialogPercent + "%");
            }
        });
    }

    private void saveimgWrk3() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        StorageReference ImagesRefP = storageRef.child("admin/" + userID+"/" + "3");

        ImagesRefP.putFile(imgUr).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ImagesRefP.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DocumentReference documentReference = db.collection("admin").document(userID);
                        documentReference.update("ad3",uri.toString());
                        Log.d("12",uri.toString());
                        progressDialog.dismiss();
                        Toast.makeText(admin.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(admin.this, "Image Uploaded Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                double progressDialogPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploading " + (int)progressDialogPercent + "%");
            }
        });
    }
}