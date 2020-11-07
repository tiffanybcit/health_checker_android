package ca.bcit.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editTextSys;
    EditText editTextDia;
    Calendar calendar;

    Button buttonAdd;
    Button report;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private ArrayList<Entry> listItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("db", String.valueOf(db));

        editTextSys = findViewById(R.id.editTextSys);
        editTextDia = findViewById(R.id.editTextDia);
        buttonAdd = findViewById(R.id.buttonAdd);
    report = findViewById(R.id.buttonMonthlyReport);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();

            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReport();

            }
        });
        recyclerView = findViewById(R.id.item_list);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        setQuene();

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("sysreading", editTextSys.getText().toString());
        outState.putString("diareading", editTextDia.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String _sysreading = savedInstanceState.getString("sysreading");
        String _diareading = savedInstanceState.getString("diareading");

        editTextSys.setText(_sysreading);
        editTextDia.setText(_diareading);

    }

    //prepare and send to report page
    public void getReport(){
        int temp_sys = 0;
        int temp_dia = 0;
        int counter = 0;
        int div_temp_sys = 0;
        int div_temp_dia = 0;
        String condition = "";
        Calendar calendar = Calendar.getInstance();
        String deviceId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        Log.d("debug", deviceId);
        String monthNow  = (String) DateFormat.format("MM",   calendar); // 06
//        Log.d("debug", monthNow);
        String yearNow = (String) DateFormat.format("yyyy", calendar); // 2013
//        Log.d("debug", yearNow);
        for (int i = 0; i < listItems.size(); i++) {
            String monthThen = (String) DateFormat.format("MM",  listItems.get(i).get_date());
//            Log.d("debug", monthThen);
            String yearThen = (String) DateFormat.format("yyyy", listItems.get(i).get_date());
//            Log.d("debug", yearThen);
//            Log.d("debug", listItems.get(i).get_serialNum());
            if(deviceId.equals(listItems.get(i).get_serialNum())){

            }
            if(deviceId.equals(listItems.get(i).get_serialNum())) {
                if(monthNow.equals(monthThen) && yearNow.equals(yearThen)) {
                    Log.d("debug", Integer.toString(temp_sys));
                    Log.d("debug", Integer.toString(temp_dia));
                    Log.d("debug", Integer.toString(counter));
                    temp_sys += Integer.parseInt(listItems.get(i).get_sys());
                    temp_dia += Integer.parseInt(listItems.get(i).get_dia());
                    counter = counter + 1;
                }
            }
            }
        if(counter == 0){
            div_temp_sys = 0;
            div_temp_dia = 0;

        } else {
            div_temp_sys = temp_sys / counter;
            div_temp_dia = temp_dia / counter;
        }
        if (div_temp_sys ==0 && div_temp_dia ==0){
            condition += "No reading for this month!";
        }
        else if(div_temp_sys < 120 && div_temp_dia < 80){
            condition +="Normal";
        } else if(div_temp_sys < 129 && div_temp_dia > 120 && div_temp_dia < 80){
            condition +="Elevated";
        } else if ((div_temp_sys < 139 && div_temp_sys > 130) || (div_temp_dia > 80 && div_temp_dia < 89)){
            condition +="High Blood Pressure(Stage1)";
        } else if(div_temp_sys > 180 || div_temp_dia > 120 ){
            condition +="Hypertensive Crisis";
        }
        else {
            condition +="High Blood Pressure(Stage2)";
        }

        Intent intent = new Intent(this, Report.class);
        Toast.makeText(this, "You just clicked for report", Toast.LENGTH_SHORT).show();
        intent.putExtra("id", deviceId);
        intent.putExtra("sys", Integer.toString(div_temp_sys));
        intent.putExtra("dia", Integer.toString(div_temp_dia));
        intent.putExtra("condition", condition);


        startActivity(intent);
    }

    //read data from firebase to get to a list
    public void setQuene(){

        db.collection("readings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String temp_id = (String)document.getId();
                                String temp_serial = (String) document.getData().get("Serial");
//                                Log.d("debug", "Task "+(String) document.getData().get("task"));

                                String temp_sys = (String) document.getData().get("Sys");
//                                Log.d("debug", "Who "+(String) document.getData().get("who"));
                                String temp_dia = (String) document.getData().get("Dia");
                                com.google.firebase.Timestamp temp_time =
                                        (com.google.firebase.Timestamp) document.getData().get("Date");

//                                Log.d("debug", "Date "+temp_time.toDate());
                                String temp_status = (String) document.getData().get("Condition");

                                Entry book123 = new Entry(temp_id, temp_serial, temp_time.toDate(), temp_sys, temp_dia, temp_status);
                                listItems.add(book123);
//                                Log.d("debug", document.getId() + " => " + document.getData());
                            }
                            adapter = new MyAdapter(listItems, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d("debug", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    //the alert
    public void confirm() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Warning");
        builder.setMessage("Consult your doctor immediately!");
        builder.setPositiveButton("Dismiss",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
//        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();


    }

    //collect data to be used later for firebase
    public void addTask(){
        String sys1 = editTextSys.getText().toString().trim();
        String dia1 = editTextDia.getText().toString().trim();
        if (TextUtils.isEmpty(sys1)) {
            Toast.makeText(this, "You must enter a sys pressure.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(dia1)) {
            Toast.makeText(this, "You must enter a dia pressure.", Toast.LENGTH_LONG).show();
            return;
        }
        calendar = Calendar.getInstance();

        String deviceId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

    String status11 = "";
    if(Integer.parseInt(sys1) < 120 && Integer.parseInt(dia1) < 80){
        status11 +="Normal";
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    } else if((Integer.parseInt(sys1) < 129) && (Integer.parseInt(sys1) > 120) && Integer.parseInt(dia1) < 80){
        status11 +="Elevated";
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    } else if ((Integer.parseInt(sys1) < 139 && Integer.parseInt(sys1) > 130) || (Integer.parseInt(dia1) > 80 && Integer.parseInt(dia1) < 89)){
        status11 +="Stage1";
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }else if((Integer.parseInt(sys1) > 180) || (Integer.parseInt(dia1) > 120 )){
        status11 +="Hypertensive Crisis";
        confirm();
    }
    else {
        status11 +="Stage2";
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
        writeToDatabase(deviceId, calendar.getTime(), sys1, dia1, status11);
//        finish();
//        overridePendingTransition( 0, 0);
//        startActivity(getIntent());
//        overridePendingTransition( 0, 0);


    }

    //write to database
    public void writeToDatabase(String serialNum, Date date, String sys, String dia, String condition) {

        Map<String, Object> task = new HashMap<>();
        task.put("Serial", serialNum);
        task.put("Date", date);
        task.put("Sys", sys);
        task.put("Dia", dia);
        task.put("Condition", condition);

        db.collection("readings").document()
                .set(task)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("debug", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("debug", "Error writing document", e);
                    }
                });
    }

}