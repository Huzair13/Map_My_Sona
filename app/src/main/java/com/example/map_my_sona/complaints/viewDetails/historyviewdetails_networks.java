package com.example.map_my_sona.complaints.viewDetails;

import static android.R.layout.simple_spinner_dropdown_item;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.map_my_sona.R;
import com.example.map_my_sona.complaints.Complaint_details;
import com.example.map_my_sona.complaints.HistoryDetails.Complaints_HistoryDetails_Networks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class historyviewdetails_networks extends AppCompatActivity {

    private DatabaseReference reference_complaints_history_fullView;

    private TextView pro_id,com_status_his;
    private String pro_id_str;
    private Button comp_close;
    private String status;
    private Spinner feedback;

    AlertDialog.Builder builder_networks;

    private TextView staff_name,staff_dep,com_id,staff_mob,powerRating,wexpiry,wperiod,ins_by,ins_date,mob,com_txt;

    private String staff_name_str,staff_dep_str,com_id_str,staff_mob_str,powerRating_str,wexpiry_str,wperiod_str,ins_by_str,ins_date_str,mob_str,com_txt_str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historyviewdetails_networks);

        builder_networks=new AlertDialog.Builder(this);

        Intent intent=getIntent();
        String com_id_new=intent.getStringExtra("com_ID");


        staff_name=(TextView)findViewById(R.id.staff_name_unit_his_networks);
        staff_dep=(TextView)findViewById(R.id.dep_unit_his_networks);
        com_id=(TextView)findViewById(R.id.Comid_unit_his_networks);
        staff_mob=(TextView)findViewById(R.id.staff_mob_history_com_networks);
        powerRating=(TextView)findViewById(R.id.powerRating_unit_his_networks);
        wexpiry=(TextView)findViewById(R.id.warranty_exp_unit_his_networks);
        wperiod=(TextView)findViewById(R.id.warranty_unit_his_networks);
        ins_by=(TextView)findViewById(R.id.ins_by_unit_his_networks);
        ins_date=(TextView)findViewById(R.id.ins_date_unit_his_networks);
        mob=(TextView)findViewById(R.id.mob_unit_his_networks);
        com_txt=(TextView)findViewById(R.id.com_txt_history_networks);


        pro_id=(TextView) findViewById(R.id.Product_ID_history_networks);
        com_status_his=(TextView)findViewById(R.id.complaint_status_his_networks);
        comp_close=(Button)findViewById(R.id.close_the_com_his_networks);
        feedback=(Spinner) findViewById(R.id.feedback);


        String[] feebac={"Feedback ","Excellent","Good","Not bad" ,"Bad"};
        feedback.setAdapter(new ArrayAdapter<String>(this, simple_spinner_dropdown_item,feebac));
        reference_complaints_history_fullView= FirebaseDatabase.getInstance().getReference("complaints").child("Networks").child(com_id_new);

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
                Toast.makeText(historyviewdetails_networks.this, "Something Went Wrong !!! ", Toast.LENGTH_SHORT).show();

            }
        });

        comp_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("Pending")){
                    HashMap hp=new HashMap();
                    hp.put("status","Completed");

                    builder_networks.setTitle("Alert")
                            .setMessage("Are you sure to close the complaint ??")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    reference_complaints_history_fullView.updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            Toast.makeText(historyviewdetails_networks.this, "Complaint closed", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(historyviewdetails_networks.this, Complaints_HistoryDetails_Networks.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(historyviewdetails_networks.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(historyviewdetails_networks.this, "This complaint is already solved and closed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}