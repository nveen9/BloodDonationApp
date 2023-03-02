package com.example.blooddonation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class listAdapter extends RecyclerView.Adapter<listAdapter.itemHolder> {

    Context context;
    ArrayList<donor> list;


    public listAdapter(Context context, ArrayList<donor> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public listAdapter.itemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list,parent,false);
        return new itemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull listAdapter.itemHolder holder, int position) {
        donor donor = list.get(position);
        holder.blood.setText(donor.getBlood_type());
        holder.Firstname.setText(donor.getName());
        holder.local.setText(donor.getLocality());

        holder.listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserID = donor.getUser_id();
                Intent intent = new Intent(context,userDonorAccount.class);
                intent.putExtra("UserID",UserID);
                context.startActivity(intent);
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                DocumentReference documentReference = db.collection("donor").document(UserID);
//                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()){
//                            Intent intent = new Intent(context,userDonorAccount.class);
//                            intent.putExtra("UserID",UserID);
//                            context.startActivity(intent);
//                        }
//                    }
//                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class itemHolder extends RecyclerView.ViewHolder{

        TextView blood,local,Firstname;
        ConstraintLayout listBtn;

        public itemHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            blood = itemView.findViewById(R.id.blood);
            local = itemView.findViewById(R.id.local);
            Firstname = itemView.findViewById(R.id.Firstname);
            listBtn = itemView.findViewById(R.id.listBtn);
        }
    }
    public void fltrList(ArrayList<donor> filterList){
        list = filterList;
        notifyDataSetChanged();
    }

}
