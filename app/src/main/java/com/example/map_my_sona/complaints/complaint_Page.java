package com.example.map_my_sona.complaints;

import static android.R.layout.simple_spinner_dropdown_item;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.DynamicsProcessing;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.util.Config;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.animation.content.Content;
import com.example.map_my_sona.ExampleService;
import com.example.map_my_sona.R;
import com.example.map_my_sona.dashboard;
import com.example.map_my_sona.manualentry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import papaya.in.sendmail.SendMail;

public class complaint_Page extends AppCompatActivity {

    private TextView sn, make, model, procurement, powerRating, wexpiry, wperiod, ins_by, ins_date, mob,dep_of_pro;
    private TextView location;
    private String location_str;
    private EditText  complainted_by_name, complainted_by_mob ;
    private EditText other_com;
    private Spinner complainted_by_dep;
    private Spinner complaint_qrcode;
    private Button complaint_subBtn;
    Float rating;
    String rating_str;
    CheckBox vhigh ,high ,low;

    String uref;

    private String complainted_by_dep_str, complainted_by_name_str, complainted_by_mob_str, sn_str, make_str, model_str,
            procurement_str, powerRating_str, wexpiry_str, wperiod_str, ins_by_str, ins_date_str, mob_str,dep_of_pro_str;
    private String complaint_txt,others_com_str;
    String status = "Pending";
    DatabaseReference databaseReference;
    String s,manual_name,manual_mob;
    String FeedBack_str;
    DatabaseReference dbRef;

    TextInputLayout complaint_content;
    AutoCompleteTextView complaint_content_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_page);

        uref= FirebaseAuth.getInstance().getUid();

        //
//        complaint_content=findViewById(R.id.complaint_content);
//        complaint_content_text=findViewById(R.id.complaint_Qrcode);

        sn = (TextView) findViewById(R.id.sn_unit);
        make = (TextView) findViewById(R.id.make_unit);
        model = (TextView) findViewById(R.id.model_unit);
        powerRating = (TextView) findViewById(R.id.powerRating_unit);
        procurement = (TextView) findViewById(R.id.procurement_unit);
        wperiod = (TextView) findViewById(R.id.warranty_unit);
        wexpiry = (TextView) findViewById(R.id.warranty_exp_unit);
        ins_by = (TextView) findViewById(R.id.ins_by_unit);
        ins_date = (TextView) findViewById(R.id.ins_date_unit);
        mob = (TextView) findViewById(R.id.mob_unit);
        dep_of_pro=(TextView)findViewById(R.id.dep_of_pro_unit);
        location=(TextView)findViewById(R.id.scanned_location);

        complaint_subBtn = (Button) findViewById(R.id.button_complaint_submit);

        complaint_qrcode=(Spinner) findViewById(R.id.complaint_Qrcode);
        complainted_by_name = (EditText) findViewById(R.id.scan_qr_com_name);
        complainted_by_mob = (EditText) findViewById(R.id.scan_qr_com_mob);

        other_com=(EditText)findViewById(R.id.others_complaint_qr);

        complainted_by_dep = (Spinner) findViewById(R.id.scan_qr_com_dep);

//        vhigh =(CheckBox)findViewById(R.id.veryhighpriority);
//        high=(CheckBox)findViewById(R.id.highpriority);
//        low =(CheckBox)findViewById(R.id.lowpriority);

        LinearLayout priority = (LinearLayout) findViewById(R.id.prioritylayout);


//        TextView scanText = (TextView) findViewById(R.id.textView);
        s = getIntent().getStringExtra("SCAN_RESULT");
        manual_name=getIntent().getStringExtra("MANUAL_NAME");
        manual_mob=getIntent().getStringExtra("MANUAL_MOB");


        String[] dept_com_scan={"Department ","CSE","IT","ADS","ECE","EEE","MECH","MCT","CIVIL"};
        complainted_by_dep.setAdapter(new ArrayAdapter<String>(this, simple_spinner_dropdown_item,dept_com_scan));

        String[] com_scan={"Complaint","Not Working","Broken","Leakage","Others"};
        complaint_qrcode.setAdapter(new ArrayAdapter<String>(this, simple_spinner_dropdown_item,com_scan));

        databaseReference = FirebaseDatabase.getInstance().getReference("Datas");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mob_str = String.valueOf(snapshot.child(s).child("mob").getValue(Long.class));
                sn_str = String.valueOf(snapshot.child(s).child("sn_no").getValue(Long.class));
                make_str = snapshot.child(s).child("make").getValue(String.class);
                model_str = snapshot.child(s).child("model").getValue(String.class);
                procurement_str = snapshot.child(s).child("procurement").getValue(String.class);
                powerRating_str = snapshot.child(s).child("power_rating").getValue(String.class);
                wexpiry_str = snapshot.child(s).child("wexpiry").getValue(String.class);
                wperiod_str = snapshot.child(s).child("wperiod").getValue(String.class);
                ins_by_str = snapshot.child(s).child("ins_by").getValue(String.class);
                ins_date_str = snapshot.child(s).child("ins_date").getValue(String.class);
                dep_of_pro_str = snapshot.child(s).child("dep_of_pro").getValue(String.class);
                location_str = snapshot.child(s).child("location").getValue(String.class);
                rating = 0.0f;
                rating_str = rating.toString();
                FeedBack_str="None";


                sn.setText(sn_str);
                make.setText(make_str);
                model.setText(model_str);
                procurement.setText(procurement_str);
                powerRating.setText(powerRating_str);
                wexpiry.setText(wexpiry_str);
                wperiod.setText(wperiod_str);
                ins_by.setText(ins_by_str);
                ins_date.setText(ins_date_str);
                mob.setText(mob_str);
                dep_of_pro.setText(dep_of_pro_str);
                location.setText(location_str);

//                if(!manual_name.equals("null")){
//                    complainted_by_name.setText(manual_name);
//                }
//                if(!manual_mob.equals("null")){
//                    complainted_by_mob.setText(manual_mob);
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        complaint_qrcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                complaint_txt=complaint_qrcode.getSelectedItem().toString();
                if(complaint_txt.equals("Others")){
                    other_com.setVisibility(View.VISIBLE);
                }
                else{
                    other_com.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        complainted_by_dep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                complainted_by_dep_str=complainted_by_dep.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        String[] complaint = {"Light Not working", "Network issue", "Fan not working", "Bathroom Problem", "Furniture defects"};
//        ArrayAdapter<String> complaintAdapter = new ArrayAdapter<>(complaint_Page.this, R.layout.dropdowncomplaintcontent, complaint);
//        complaint_content_text.setAdapter(complaintAdapter);


        complaint_subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(complaint_txt.equals("Others") && other_com.getText().toString().isEmpty()){
                    Toast.makeText(complaint_Page.this,"Please specify your complaint",Toast.LENGTH_SHORT).show();
                    other_com.requestFocus();
                }
                else if (complaint_txt.equals("Complaint")) {
                    Toast.makeText(complaint_Page.this, "Please select the Complaint", Toast.LENGTH_SHORT).show();
                    complaint_qrcode.requestFocus();
                }else if (complainted_by_name.getText().toString().isEmpty()) {
                    complainted_by_name.setError("Empty");
                    complainted_by_name.requestFocus();
                } else if (complainted_by_mob.getText().toString().isEmpty()) {
                    complainted_by_mob.setError("Empty");
                    complainted_by_mob.requestFocus();
                } else if (complainted_by_dep_str.equals("Dept")) {
                    Toast.makeText(complaint_Page.this, "Please select the Dept", Toast.LENGTH_SHORT).show();
                    complainted_by_dep.requestFocus();
                }
//                else if (vhigh.getText().toString().isEmpty()) {
//                    vhigh.setError("Empty");
//                    vhigh.requestFocus();
//                }
                else if(dep_of_pro_str.equals("Electricity")){

                    SendMail mail=new SendMail("mapmysona@gmail.com",
                            "mms@2022",
                            "ahamedhuzair13@gmail.com",//panneerselvamm@sonatech.ac.in
                            "Complaint in "+dep_of_pro_str+" Department",
                            "Complained by "+ complainted_by_name.getText().toString() +"\n" +
                                    "COMPLAINT: "+ complaint_txt
                    );
                    mail.execute();
                    submitComplaint();
                }
                else if(dep_of_pro_str.equals("Plumber")){

                    SendMail mail=new SendMail("mapmysona@gmail.com",
                            "mms@2022",
                            "adhiyaman@sonatech.ac.in",
                            "Complaint in "+dep_of_pro_str+" Department",
                            "Complained by "+ complainted_by_name.getText().toString() +"\n" +
                                    "COMPLAINT: "+ complaint_txt
                    );
                    mail.execute();
                    submitComplaint();

                }
                else if(dep_of_pro_str.equals("Network")){

                    SendMail mail=new SendMail("mapmysona@gmail.com",
                            "mms@2022",
                            "sakthivel@sonatech.ac.in",
                            "Complaint in "+dep_of_pro_str+" Department",
                            "Complained by "+ complainted_by_name.getText().toString() +"\n" +
                                    "COMPLAINT: "+ complaint_txt
                    );
                    mail.execute();
                    submitComplaint();

                }
                else{
                    SendMail mail=new SendMail("mapmysona@gmail.com",
                            "mms@2022",
                            "adhiyaman@sonatech.ac.in",
                            "Complaint in "+dep_of_pro_str+" Department",
                            "Complained by "+ complainted_by_name.getText().toString() +"\n" +
                                    "COMPLAINT: "+ complaint_txt
                    );
                    mail.execute();
                    submitComplaint();
                }
            }

        });
        //scanText.setText(s);

//        String input = complaint_txt;
//        Intent serviceIntent = new Intent(this, ExampleService.class);
//        serviceIntent.putExtra("inputExtra", input);
//        ContextCompat.startForegroundService(this, serviceIntent);
    }


    private void submitComplaint() {

        dbRef = FirebaseDatabase.getInstance().getReference().child("complaints").child(dep_of_pro_str);
        final String uniqueKey = dbRef.push().getKey();

        complainted_by_name_str = complainted_by_name.getText().toString();
        complainted_by_mob_str = complainted_by_mob.getText().toString();
        if(complaint_txt.equals("Others")){
            complaint_txt=other_com.getText().toString();
        }

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());


        Complaint_details complaint_details = new Complaint_details(complainted_by_name_str, complainted_by_mob_str,
                complainted_by_dep_str, complaint_txt, sn_str,
                make_str, model_str, procurement_str,
                powerRating_str, wperiod_str, wexpiry_str, ins_by_str, ins_date_str, mob_str, date, time, uniqueKey, s,
                status,dep_of_pro_str,uref,location_str,rating_str,FeedBack_str);

        dbRef.child(uniqueKey).setValue(complaint_details).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(complaint_Page.this, "Complaint Registered Successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(complaint_Page.this, dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(complaint_Page.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
//
//    public void startService(View v) {
//        String input = complaint_qrcode.getText().toString();
//        String input = "Hello Vicky";
//        Intent serviceIntent = new Intent(this, ExampleService.class);
//         serviceIntent.putExtra("inputExtra", input);
//
//        ContextCompat.startForegroundService(this, serviceIntent);
//    }
}