package ca.bcit.assignment2;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//this class is used to display a detailed page where delete and update can be performed
public class Details extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView title;
    TextView DeviceNum;
    TextView Date;
    TextView Time;
    EditText sys1;
    EditText dia1;
    Button delete;
    Button edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DeviceNum = findViewById(R.id.deviceID2);
        Date = findViewById(R.id.date2);
        Time = findViewById(R.id.time2);

        sys1 = findViewById(R.id.sys2);
        dia1 = findViewById(R.id.dia2);
        delete = findViewById(R.id.textDeleteEntry);
        edit = findViewById(R.id.textEditEntry);


        //Received from the last page
        Intent intent = getIntent();
        final Entry article = (Entry) intent.getSerializableExtra("entry");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.US);


        String dateString = format.format(article.get_date());

        String date = dateString.substring(0, 11);
        String time = dateString.substring(11);

        DeviceNum.setText("Device ID: " + article.get_serialNum());
        Date.setText("Date: " + date);

        Time.setText("Time: " + time);

        sys1.setText(article.get_sys());
        dia1.setText(article.get_dia());

        //delete from database
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(article.get_id());
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                Toast.makeText(view.getContext(), "Delete Successful!", Toast.LENGTH_SHORT).show();
                view.getContext().startActivity(intent);
            }
        });

        //edit the existing item
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInput(article.get_serialNum(), article.get_id());
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                Toast.makeText(view.getContext(), "Update Successful!", Toast.LENGTH_SHORT).show();
                view.getContext().startActivity(intent);
            }
        });

    }

    //delete from database function
    public void delete(String id){

        db.collection("readings").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
    }

    //get user input to be used later in update function
    public void getInput(String num, String id){
        String sys = sys1.getText().toString().trim();
        String dia = dia1.getText().toString().trim();
        if (TextUtils.isEmpty(sys)) {
            Toast.makeText(this, "You must enter a sys reading.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(dia)) {
            Toast.makeText(this, "You must enter a dia reading.", Toast.LENGTH_LONG).show();
            return;
        }
        String status11 = "";
        if(Integer.parseInt(sys) < 120 && Integer.parseInt(dia) < 80){
            status11 +="Normal";
        } else if((Integer.parseInt(sys) < 129) && (Integer.parseInt(sys) > 120) && Integer.parseInt(dia) < 80){
            status11 +="Elevated";
        } else if ((Integer.parseInt(sys) < 139 && Integer.parseInt(sys) > 130) || (Integer.parseInt(dia) > 80 && Integer.parseInt(dia) < 89)){
            status11 +="High Blood Pressure(Stage1)";
        } else if((Integer.parseInt(sys) > 180) || (Integer.parseInt(dia) > 120 )){
            status11 +="Hypertensive Crisis";
        } else {
            status11 +="High Blood Pressure(Stage2)";
        }
        update(num, status11, sys, dia, id);
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);

    }

    //update helper function
    public void update(String serial, String status, String sys, String dia, String id){
        Map<String, Object> temp = new HashMap<>();
        temp.put("Serial", serial);
        temp.put("Sys", sys);
        temp.put("Dia", dia);
        temp.put("Condition", status);
        Calendar calendar = Calendar.getInstance();
        temp.put("Date", calendar.getTime());

        db.collection("readings").document(id)
                .set(temp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Tag", "Error writing document", e);
                    }
                });
    }
}
