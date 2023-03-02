package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class DonnieTabFragment extends Fragment {

    EditText name, email, password, cpassword, nic, contact_no;
    TextInputLayout passwordLayout,cpasswordLayout;
    TextView dob, age;
    Button save;

    Spinner gender;
    ArrayList<String> choose;

    String selectedG = "";

    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_donnie_tab_fragment, container, false);

        name = root.findViewById(R.id.name);
        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        cpassword = root.findViewById(R.id.cpassword);
        passwordLayout = root.findViewById(R.id.passwordLayout);
        cpasswordLayout = root.findViewById(R.id.cpasswordLayout);
        nic = root.findViewById(R.id.nic);
        dob = root.findViewById(R.id.dob);
        age = root.findViewById(R.id.age);
        gender = root.findViewById(R.id.gender);
        contact_no = root.findViewById(R.id.contact_no);
        save = root.findViewById(R.id.save);

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

        return root;
    }
}