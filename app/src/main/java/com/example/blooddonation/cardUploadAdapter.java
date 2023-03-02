package com.example.blooddonation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class cardUploadAdapter extends RecyclerView.Adapter<cardUploadAdapter.itemHolder> {

    Context context;
    ArrayList<card> list;

    String datee, uID;
    double points;
    FirebaseFirestore db;


    public cardUploadAdapter(Context context, ArrayList<card> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public cardUploadAdapter.itemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_upload_list, parent, false);
        return new itemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull cardUploadAdapter.itemHolder holder, int position) {
        int pos = position;
        card donor = list.get(pos);
        holder.id.setText(donor.getId());
        holder.name.setText(donor.getName());
        Glide.with(context).load(list.get(pos).getCard()).into(holder.cardPic);
        holder.date.setText(donor.getDate());

        holder.cardPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(context)
                        .asBitmap()
                        .load(list.get(pos).getCard())
                        .into(new CustomTarget<Bitmap>(CustomTarget.SIZE_ORIGINAL, CustomTarget.SIZE_ORIGINAL) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                saveImage(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
        });

        db = FirebaseFirestore.getInstance();

        db.collection("cardupload").orderBy("num").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    String num = dc.getDocument().getString("num");
                    int n = Integer.parseInt(num);
                    int nn = pos + 1;
                    if (n == nn) {
                        db.collection("cardupload").document(num).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                String status = value.getString("status");
                                if (status.equals("ok")) {
                                    holder.accept.setVisibility(View.GONE);
                                } else {
                                    holder.accept.setVisibility(View.VISIBLE);

                                    uID = donor.getId();

                                    DocumentReference documentReference = db.collection("donor").document(uID);
                                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            points = value.getDouble("points");
                                            Log.d("123", String.valueOf(points));
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Alert");
                builder.setMessage("Checked on this ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        final int year = calendar.get(Calendar.YEAR);
                        final int month = calendar.get(Calendar.MONTH);
                        final int day = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                month = month + 1;
                                datee = day + "/" + month + "/" + year;
                                holder.date.setText(datee);

                                db = FirebaseFirestore.getInstance();
                                uID = donor.getId();

//                                SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
//                                        SharedPreferences.Editor editor = preferences.edit();
//                                        editor.putString(donor.getId()+"point", String.valueOf(points));
//                                        editor.commit();

//                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//                                String val = preferences.getString("point","");
//
//                                double p = Double.parseDouble(val);
//                                Log.d("1234", val);

                                Map<String, Object> user = new HashMap<>();
                                user.put("date", datee);
                                user.put("points", points + 10);
                                DocumentReference documentReference1 = db.collection("donor").document(uID);
                                documentReference1.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            setData(pos, holder.accept);
                                        }
                                    }
                                });
                            }
                        }, year, month, day);
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                        datePickerDialog.show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private String saveImage(Bitmap resource) {
        String savedImagePath = null;

        String imageFileName = UUID.randomUUID().toString() + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/BloodDonation Saved");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                resource.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            Toast.makeText(context, "Image Downloaded", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String savedImagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(savedImagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    private void setData(int position, Button accept) {
        db.collection("cardupload").orderBy("num", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    String num = dc.getDocument().getString("num");
                    int n = Integer.parseInt(num);
                    int nn = position + 1;
                    if (n == nn) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("date", datee);
                        user.put("status", "ok");
                        db.collection("cardupload").document(num).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    accept.setVisibility(View.GONE);
                                    Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class itemHolder extends RecyclerView.ViewHolder {

        TextView name, id, date;
        Button accept;
        ImageView cardPic;

        public itemHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            id = itemView.findViewById(R.id.id);
            date = itemView.findViewById(R.id.date);
            cardPic = itemView.findViewById(R.id.cardPic);
            accept = itemView.findViewById(R.id.btnaccept);
        }
    }

    public void fltrList(ArrayList<card> filterList) {
        list = filterList;
        notifyDataSetChanged();
    }

}
