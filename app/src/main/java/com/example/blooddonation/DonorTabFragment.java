package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DonorTabFragment extends Fragment {

    EditText name, email, password, cpassword, nic, contact_no, weight;
    TextInputLayout passwordLayout,cpasswordLayout;
    TextView dob, age, address, date;
    Button save, location, clocation, never, dateselect;
    ProgressBar probar;

    LinearLayout linearLayout,linearLayout1,linearLayout2;

    Spinner gender, blood_grp;
    ArrayList<String> choose,chooseBlood;

    String selectedG = "";
    String selectedB = "";

    double latitude,longitude,La,Lo;
    String locality,Loc;

    String nm,mail,pass,cpass,nicNum,a,b,add,num,wei,datee;
    String y;
    Integer m;
    Integer d;

    float v = 0;

    FusedLocationProviderClient fusedLocationProviderClient;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_donor_tab_fragment, container, false);

        name = root.findViewById(R.id.name);
        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        cpassword = root.findViewById(R.id.cpassword);
        passwordLayout = root.findViewById(R.id.passwordLayout);
        cpasswordLayout = root.findViewById(R.id.cpasswordLayout);
        nic = root.findViewById(R.id.nic);
        dob = root.findViewById(R.id.dob);
        age = root.findViewById(R.id.age);
        location = root.findViewById(R.id.location);
        clocation = root.findViewById(R.id.clocation);
        gender = root.findViewById(R.id.gender);
        address = root.findViewById(R.id.address);
        contact_no = root.findViewById(R.id.contact_no);
        blood_grp = root.findViewById(R.id.blood_grp);
        weight = root.findViewById(R.id.weight);
        save = root.findViewById(R.id.save);
        probar = root.findViewById(R.id.loading);
        date = root.findViewById(R.id.date);
        dateselect = root.findViewById(R.id.dateselect);
        never = root.findViewById(R.id.never);
        linearLayout = root.findViewById(R.id.linearLayout);
        linearLayout1 = root.findViewById(R.id.linearLayout1);
        linearLayout2 = root.findViewById(R.id.linearLayout2);

        name.setTranslationX(800);
        nic.setTranslationX(800);
        dob.setTranslationX(800);
        age.setTranslationX(800);
        gender.setTranslationX(800);
        email.setTranslationX(800);
        password.setTranslationX(800);
        cpassword.setTranslationX(800);
        passwordLayout.setTranslationX(800);
        cpasswordLayout.setTranslationX(800);
        location.setTranslationX(800);
        clocation.setTranslationX(800);
        address.setTranslationX(800);
        contact_no.setTranslationX(800);
        blood_grp.setTranslationX(800);
        weight.setTranslationX(800);
        save.setTranslationX(800);
        dateselect.setTranslationX(800);
        never.setTranslationX(800);
        linearLayout.setTranslationX(800);
        linearLayout1.setTranslationX(800);
        linearLayout2.setTranslationX(800);

        name.setAlpha(v);
        nic.setAlpha(v);
        dob.setAlpha(v);
        age.setAlpha(v);
        gender.setAlpha(v);
        email.setAlpha(v);
        password.setAlpha(v);
        cpassword.setAlpha(v);
        passwordLayout.setAlpha(v);
        cpasswordLayout.setAlpha(v);
        location.setAlpha(v);
        clocation.setAlpha(v);
        address.setAlpha(v);
        contact_no.setAlpha(v);
        blood_grp.setAlpha(v);
        weight.setAlpha(v);
        save.setAlpha(v);
        dateselect.setAlpha(v);
        never.setAlpha(v);
        linearLayout.setAlpha(v);
        linearLayout1.setAlpha(v);
        linearLayout2.setAlpha(v);

        name.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        nic.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        dob.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        age.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        gender.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        cpassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        passwordLayout.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        cpasswordLayout.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        location.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        clocation.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        address.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        contact_no.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        blood_grp.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        weight.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        save.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        dateselect.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        never.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        linearLayout.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        linearLayout1.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        linearLayout2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        choose = new ArrayList<>();
        choose.add("Male");
        choose.add("Female");
        choose.add("Other");

        gender.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, choose));
        gender.setPrompt("Gender");
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                selectedG = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chooseBlood = new ArrayList<>();
        chooseBlood.add("A+");
        chooseBlood.add("A-");
        chooseBlood.add("B+");
        chooseBlood.add("B-");
        chooseBlood.add("AB+");
        chooseBlood.add("AB-");
        chooseBlood.add("O+");
        chooseBlood.add("O-");

        blood_grp.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, chooseBlood));
        blood_grp.setPrompt("Choose the Blood Group");
        blood_grp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                selectedB = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        dateselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        datee = day+"/"+month+"/"+year;
                        date.setText(datee);
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        never.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date.setText("Never");
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    RealtimeLocation();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        clocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),locationPick.class);
                intent.putExtra("Donor","ABC");
                startActivityForResult(intent,1000);
            }
        });

        nic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nicNum = nic.getText().toString();
                if (nicNum.length() == 10 || nicNum.length() == 12) {
                    if (nicNum.length() == 10) {
                        y = "19" + Integer.parseInt(nicNum.substring(0, 2));
                        d = Integer.parseInt(nicNum.substring(2, 5));
                    } else {
                        y = nicNum.substring(0, 4);
                        d = Integer.parseInt(nicNum.substring(4, 7));
                    }
                    if (d > 500) {
                        d = d - 500;
                    } else {

                    }
                    if (d < 1 && d > 366) {
//                        Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                    } else {
                        if (d > 335) {
                            d = d - 335;
                            m = 12;
                        } else if (d > 305) {
                            d = d - 305;
                            m = 11;
                        } else if (d > 274) {
                            d = d - 274;
                            m = 10;
                        } else if (d > 244) {
                            d = d - 244;
                            m = 9;
                        } else if (d > 213) {
                            d = d - 213;
                            m = 8;
                        } else if (d > 182) {
                            d = d - 182;
                            m = 7;
                        } else if (d > 152) {
                            d = d - 152;
                            m = 6;
                        } else if (d > 121) {
                            d = d - 121;
                            m = 5;
                        } else if (d > 91) {
                            d = d - 91;
                            m = 4;
                        } else if (d > 60) {
                            d = d - 60;
                            m = 3;
                        } else if (d < 32) {
                            d = d;
                            m = 1;
                        } else if (d > 31) {
                            d = d - 31;
                            m = 2;
                        }
                        dob.setText(d + "." + m + "." + y);
                        Calendar calendar = Calendar.getInstance();
                        int a = calendar.get(Calendar.YEAR);
                        int b = calendar.get(Calendar.MONTH);
                        int c = calendar.get(Calendar.DAY_OF_MONTH);
                        int b1 = b+1;
                        int z = a - Integer.parseInt(y);
                        if (m>b1){
                            z--;
                        }else if ((m==b1) && (d > c)){
                            z--;
                        }
                        age.setText((String.valueOf(z)));
                    }

                } else {
//                    Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        La = bundle.getDouble("latitude");
        Lo = bundle.getDouble("longitude");
        Loc = data.getStringExtra("locality");
        String Add = data.getStringExtra("Address");
        address.setText(Add);
    }

    private void RealtimeLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                if (address != null) {
                    try {
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(locations.getLatitude(), locations.getLongitude(), 1);
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                        locality = addresses.get(0).getLocality();
                        String addrs = addresses.get(0).getAddressLine(0);
                        address.setText(addrs);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void insertData() {
        nm = name.getText().toString();
        mail = email.getText().toString();
        pass = password.getText().toString();
        cpass = cpassword.getText().toString();
        nicNum = nic.getText().toString();
        num = contact_no.getText().toString();
        wei = weight.getText().toString();
        a = age.getText().toString();
        b = dob.getText().toString();
        add = address.getText().toString();
        if (nm.isEmpty()){
            name.setError("Field is Empty");
            name.requestFocus();
        }
        else if (mail.isEmpty()){
            email.setError("Field is Empty");
            email.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Provide valid Email");
            email.requestFocus();
        }
        else if (pass.isEmpty()){
            password.setError("Field is Empty");
            password.requestFocus();
        }
        else if (pass.length() < 6){
            password.setError("Minimum 6 Characters");
            password.requestFocus();
        }
        else if (!pass.equals(cpass)){
            cpassword.setError("Password not Match");
            cpassword.requestFocus();
        }
        else if (num.isEmpty()){
            contact_no.setError("Field is Empty");
            contact_no.requestFocus();
        }
        else if (num.length() < 9 || num.length() > 10){
            contact_no.setError("Invalid Number");
            contact_no.requestFocus();
        }
        else if (wei.isEmpty()){
            weight.setError("Field is Empty");
            weight.requestFocus();
        }
        else if (Double.parseDouble(wei) < 50){
            weight.setError("Sorry, weight must be over 50KG to donate Blood");
            weight.requestFocus();
        }
        else if (nicNum.length() < 10 || nicNum.length() == 11 || nicNum.length() > 12){
            nic.setError("Invalid NIC");
            nic.requestFocus();
        }
        else if (add.equals("Address")){
            Toast.makeText(getActivity(), "Provide the Address", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.d("1234","Name " +nm+"\n"+"Number "+num +"\n" + "NIC " +nicNum +"\n"+"Gender "+selectedG
                    +"\n"+"BG "+selectedB+"\n"+"Lati "+latitude+"\n"+"Longi "+longitude+"\n"+"Local "+locality+"\n"+"Address "+add
                    +"\n"+"Map "+"\n"+"Lati "+La+"\n"+"Longi "+Lo+"\n"+"Local "+Loc+"\n"+"Address "+add);

            if (La!=0.0){
                probar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();

                            String dateee = date.getText().toString();

                            if (dateee.equals("Never")){
                                DocumentReference documentReference = db.collection("donor").document(uid);
                                Map<String,Object> userData = new HashMap<>();
                                userData.put("name",nm);
                                userData.put("email",mail);
                                userData.put("password",pass);
                                userData.put("user_id",uid);
                                userData.put("nic",nicNum);
                                userData.put("dob",b);
                                userData.put("age",a);
                                userData.put("gender",selectedG);
                                userData.put("blood_type",selectedB);
                                userData.put("latitude",La);
                                userData.put("longitude",Lo);
                                userData.put("locality",Loc);
                                userData.put("address",add);
                                userData.put("contact_no",num);
                                userData.put("weight",wei);
                                userData.put("date","Never");
                                userData.put("points",0);

                                documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        probar.setVisibility(View.GONE);
                                        Intent intent = new Intent(getActivity(),donorAccount.class);
                                        startActivity(intent);
                                    }
                                });
                            }else {
                                DocumentReference documentReference = db.collection("donor").document(uid);
                                Map<String,Object> userData = new HashMap<>();
                                userData.put("name",nm);
                                userData.put("email",mail);
                                userData.put("password",pass);
                                userData.put("user_id",uid);
                                userData.put("nic",nicNum);
                                userData.put("dob",b);
                                userData.put("age",a);
                                userData.put("gender",selectedG);
                                userData.put("blood_type",selectedB);
                                userData.put("latitude",La);
                                userData.put("longitude",Lo);
                                userData.put("locality",Loc);
                                userData.put("address",add);
                                userData.put("contact_no",num);
                                userData.put("weight",wei);
                                userData.put("date",datee);
                                userData.put("points",0);

                                documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        probar.setVisibility(View.GONE);
                                        Intent intent = new Intent(getActivity(),donorAccount.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }else {
                            probar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Email already exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                probar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();

                            String dateee = date.getText().toString();

                            if (dateee.equals("Never")){
                                DocumentReference documentReference = db.collection("donor").document(uid);
                                Map<String,Object> userData = new HashMap<>();
                                userData.put("name",nm);
                                userData.put("email",mail);
                                userData.put("password",pass);
                                userData.put("user_id",uid);
                                userData.put("nic",nicNum);
                                userData.put("dob",b);
                                userData.put("age",a);
                                userData.put("gender",selectedG);
                                userData.put("blood_type",selectedB);
                                userData.put("latitude",latitude);
                                userData.put("longitude",longitude);
                                userData.put("locality",locality);
                                userData.put("address",add);
                                userData.put("contact_no",num);
                                userData.put("weight",wei);
                                userData.put("date","Never");
                                userData.put("points",0);

                                documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        probar.setVisibility(View.GONE);
                                        Intent intent = new Intent(getActivity(),donorAccount.class);
                                        startActivity(intent);
                                    }
                                });
                            }else {
                                DocumentReference documentReference = db.collection("donor").document(uid);
                                Map<String,Object> userData = new HashMap<>();
                                userData.put("name",nm);
                                userData.put("email",mail);
                                userData.put("password",pass);
                                userData.put("user_id",uid);
                                userData.put("nic",nicNum);
                                userData.put("dob",b);
                                userData.put("age",a);
                                userData.put("gender",selectedG);
                                userData.put("blood_type",selectedB);
                                userData.put("latitude",latitude);
                                userData.put("longitude",longitude);
                                userData.put("locality",locality);
                                userData.put("address",add);
                                userData.put("contact_no",num);
                                userData.put("weight",wei);
                                userData.put("date",datee);
                                userData.put("points",0);

                                documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        probar.setVisibility(View.GONE);
                                        Intent intent = new Intent(getActivity(),donorAccount.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }else {
                            probar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Email already exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}