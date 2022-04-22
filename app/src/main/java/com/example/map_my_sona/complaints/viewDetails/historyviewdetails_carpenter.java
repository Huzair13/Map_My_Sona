package com.example.map_my_sona.complaints.viewDetails;

import static android.R.layout.simple_spinner_dropdown_item;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.map_my_sona.R;
import com.example.map_my_sona.complaints.Complaint_details;
import com.example.map_my_sona.complaints.HistoryDetails.Complaints_HistoryDetails_Carpenter;
import com.example.map_my_sona.complaints.HistoryDetails.Complaints_HistoryDetails_Electricity;
import com.example.map_my_sona.dashboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import papaya.in.sendmail.SendMail;

public class historyviewdetails_carpenter extends AppCompatActivity {

    private DatabaseReference reference_complaints_history_fullView;

    private TextView pro_id,com_status_his;
    private String pro_id_str;
    private Button comp_close;
    private String status;
    private Spinner feedback;
    private DatabaseReference refDash;
    String uref_h;
    MaterialToolbar toolbar;

    AlertDialog.Builder builder_carpenter;

    private TextView staff_name,staff_dep,com_id,staff_mob,powerRating,wexpiry,wperiod,ins_by,ins_date,mob,com_txt,location;

    private String location_Str,uid_str,staff_name_str,staff_dep_str,com_id_str,staff_mob_str,powerRating_str,wexpiry_str,wperiod_str,ins_by_str,ins_date_str,mob_str,com_txt_str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historyviewdetails_carpenter);

        builder_carpenter=new AlertDialog.Builder(this);

        Intent intent=getIntent();
        String com_id_new=intent.getStringExtra("com_ID");

        refDash=FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid());

        staff_name=(TextView)findViewById(R.id.staff_name_unit_his_carpenter);
        staff_dep=(TextView)findViewById(R.id.dep_unit_his_carpenter);
        com_id=(TextView)findViewById(R.id.Comid_unit_his_carpenter);
        staff_mob=(TextView)findViewById(R.id.staff_mob_history_com_carpenter);
        powerRating=(TextView)findViewById(R.id.powerRating_unit_his_carpenter);
        wexpiry=(TextView)findViewById(R.id.warranty_exp_unit_his_carpenter);
        wperiod=(TextView)findViewById(R.id.warranty_unit_his_carpenter);
        ins_by=(TextView)findViewById(R.id.ins_by_unit_his_carpenter);
        ins_date=(TextView)findViewById(R.id.ins_date_unit_his_carpenter);
        mob=(TextView)findViewById(R.id.mob_unit_his_carpenter);
        com_txt=(TextView)findViewById(R.id.com_txt_history_carpenter);
        location=(TextView)findViewById(R.id.location_unit_his_carpenter);


        pro_id=(TextView) findViewById(R.id.Product_ID_history_carpenter);
        com_status_his=(TextView)findViewById(R.id.complaint_status_his_carpenter);

        comp_close=(Button)findViewById(R.id.close_the_com_his_carpenter);
//        feedback =(Spinner)findViewById(R.id.feedback);
//
//
//        String[] feebac={"Feedback ","Excellent","Good","Not bad" ,"Bad"};
//        feedback.setAdapter(new ArrayAdapter<String>(this, simple_spinner_dropdown_item,feebac));

        reference_complaints_history_fullView= FirebaseDatabase.getInstance().getReference("complaints").child("Carpenter").child(com_id_new);

        reference_complaints_history_fullView.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Complaint_details complaint_details=snapshot.getValue(Complaint_details.class);

                staff_name_str=complaint_details.getCom_by_name();
                staff_mob_str=complaint_details.getCom_by_mob();
                staff_dep_str=complaint_details.getCom_by_dep();
                com_id_str=complaint_details.getKey();
                powerRating_str=complaint_details.getPower_rating();
                wexpiry_str=complaint_details.getWexpiry();
                wperiod_str=complaint_details.getWperiod();
                ins_by_str=complaint_details.getIns_by();
                ins_date_str=complaint_details.getIns_date();
                mob_str=complaint_details.getMob();
                com_txt_str=complaint_details.getCom_txt();
                pro_id_str=complaint_details.getUniqueId();
                location_Str=complaint_details.getLocation();

                uid_str=complaint_details.getUID();
                uref_h= FirebaseAuth.getInstance().getUid();

                status=complaint_details.getStatus();


                staff_name.setText(staff_name_str);
                staff_mob.setText(staff_mob_str);
                staff_dep.setText(staff_dep_str);
                com_id.setText(com_id_str);
                powerRating.setText(powerRating_str);
                wexpiry.setText(wexpiry_str);
                wperiod.setText(wperiod_str);
                ins_by.setText(ins_by_str);
                ins_date.setText(ins_date_str);
                mob.setText(mob_str);
                com_txt.setText(com_txt_str);
                location.setText(location_Str);

                pro_id.setText(pro_id_str);
                com_status_his.setText(status);

                if (status.equals("Pending")){
                    com_status_his.setBackgroundResource(R.color.Red);

                }else{
                    com_status_his.setBackgroundResource(R.color.green);
                }
                com_status_his.setTextColor(getResources().getColor(R.color.white));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(historyviewdetails_carpenter.this, "Something Went Wrong !!! ", Toast.LENGTH_SHORT).show();

            }
        });

        comp_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("Pending")){
                    HashMap hp=new HashMap();
                    hp.put("status","Completed");

                    refDash.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String pos=snapshot.child("position").getValue(String.class);
                            if (snapshot.exists()){
                                if(pos.equals("admin")){
                                    builder_carpenter.setTitle("Alert")
                                            .setMessage("Are you sure to close the complaint ??")
                                            .setCancelable(true)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    reference_complaints_history_fullView.updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
                                                        @Override
                                                        public void onSuccess(Object o) {
                                                            Toast.makeText(historyviewdetails_carpenter.this, "Complaint closed", Toast.LENGTH_SHORT).show();

                                                            if(ContextCompat.checkSelfPermission(historyviewdetails_carpenter.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                                                                sendMessage();
                                                            }
                                                            else{
                                                                ActivityCompat.requestPermissions(historyviewdetails_carpenter.this,
                                                                        new String[]{Manifest.permission.SEND_SMS},
                                                                        100);
                                                            }

                                                            Intent intent=new Intent(historyviewdetails_carpenter.this, Complaints_HistoryDetails_Carpenter.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);


                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(historyviewdetails_carpenter.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            })
                                            .show();
                                }
                                else if(uid_str.equals(uref_h)){
                                    builder_carpenter.setTitle("Alert")
                                            .setMessage("Are you sure to close the complaint ??")
                                            .setCancelable(true)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    reference_complaints_history_fullView.updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
                                                        @Override
                                                        public void onSuccess(Object o) {
                                                            Toast.makeText(historyviewdetails_carpenter.this, "Complaint closed", Toast.LENGTH_SHORT).show();
                                                            Intent intent=new Intent(historyviewdetails_carpenter.this, Complaints_HistoryDetails_Carpenter.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);


                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(historyviewdetails_carpenter.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            })
                                            .show();
                                }
                                else{
                                    Toast.makeText(historyviewdetails_carpenter.this, "You are unable to close this complaint , as it is not filled by you.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    if(uid_str.equals(uref_h)){
                        HashMap hp=new HashMap();
                        hp.put("status","Pending");

                        builder_carpenter.setTitle("Alert")
                                .setMessage("Are you sure to open the complaint again ??")
                                .setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        reference_complaints_history_fullView.updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {

                                                SendMail mail=new SendMail("mapmysona@gmail.com",
                                                        "mms@2022",
                                                        "ahamedhuzair13@gmail.com",
                                                        "Complaint Reopened",
                                                        "Complaint which is closed by you has been reopened by the person " +
                                                                "who has filed the complaint\n"+"Please Recheck the complaint and give a solution as soon as possible"
                                                );
                                                mail.execute();

                                                Toast.makeText(historyviewdetails_carpenter.this, "Complaint opened Again", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(historyviewdetails_carpenter.this, Complaints_HistoryDetails_Electricity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(historyviewdetails_carpenter.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();

                    }else{
                        Toast.makeText(historyviewdetails_carpenter.this, "You are unable to close this complaint , as it is not filled by you.", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(historyviewdetails_carpenter.this, "It has already been solved and closed", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        toolbar= findViewById(R.id.topAppBar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(getApplicationContext(),"your icon was clicked",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(historyviewdetails_carpenter.this, dashboard.class));
//            }
//        });
    }

    private void sendMessage() {
        String sphone=staff_mob.getText().toString().trim();
        String sMessage="The complaint filled has been solved. \n check before closing the complaint \n If not, place it as pending";

        if(!sphone.equals("") && !sMessage.equals("")){
            SmsManager smsManager=SmsManager.getDefault();

            smsManager.sendTextMessage(sphone,null,sMessage,null,null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            sendMessage();
        }
    }

}