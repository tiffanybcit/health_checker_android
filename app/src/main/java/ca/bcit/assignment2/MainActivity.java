package ca.bcit.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static java.lang.Character.isDigit;

//main activity for homepage
public class MainActivity extends AppCompatActivity {

    EditText editTextSys;
    EditText editTextDia;
    Spinner s;
    Calendar calendar;

    Button buttonAdd;
    Button report;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] arraySpinner;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    Context context;
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
                Intent intent = new Intent(MainActivity.this, Report.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.item_list);
        recyclerView.setHasFixedSize(true);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        context = getApplicationContext();
        arraySpinner = context.getResources().getStringArray(R.array.familyMember);

        s = (Spinner) findViewById(R.id.spinner_time);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        listItems = new ArrayList<>();
        setQuene();

    }

    // save the current state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("fammember", Integer.toString(s.getSelectedItemPosition()));
        outState.putString("sysreading", editTextSys.getText().toString());
        outState.putString("diareading", editTextDia.getText().toString());
    }

    // restore the current state
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int familyMember = Integer.parseInt(savedInstanceState.getString("fammember"));
        String _sysreading = savedInstanceState.getString("sysreading");
        String _diareading = savedInstanceState.getString("diareading");
        s.setSelection(familyMember);
        editTextSys.setText(_sysreading);
        editTextDia.setText(_diareading);

    }


    //read data from firebase to get to a list
    public void setQuene(){

        db.collection("readings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document :task.getResult()) {
                                String temp_id = (String)document.getId();

                                String temp_fam = (String) document.getData().get("FamilyMember");

                                String temp_sys = (String) document.getData().get("Systolic Pressure");

                                String temp_dia = (String) document.getData().get("Diastolic Pressure");
                                com.google.firebase.Timestamp temp_time =
                                        (com.google.firebase.Timestamp) document.getData().get("Date");

                                String temp_status = (String) document.getData().get("Condition");

                                Entry book123 = new Entry(temp_id, temp_fam, temp_time.toDate(), temp_sys, temp_dia, temp_status);
                                listItems.add(book123);
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

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Function to check if a string
    // contains only digits
    public static boolean onlyDigits(String str)
    {
        // Traverse the string from
        // start to end
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= '0'
                    && str.charAt(i) <= '9') {
                continue;
            } else {
                return false;
            }

        }
        return true;
    }

    //collect data to be used later for firebase
    public void addTask(){
        String fam = s.getSelectedItem().toString();

        String sys1 = editTextSys.getText().toString().trim();

        String dia1 = editTextDia.getText().toString().trim();

        //error-catching scheme
        if(!onlyDigits(sys1)){
            Toast.makeText(this, "You must enter a whole number.", Toast.LENGTH_LONG).show();
            return;
        }
        if(!onlyDigits(dia1)){
            Toast.makeText(this, "You must enter a whole number.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(sys1)) {
            Toast.makeText(this, "You must enter a sys pressure.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(dia1)) {
            Toast.makeText(this, "You must enter a dia pressure.", Toast.LENGTH_LONG).show();
            return;
        }
        calendar = Calendar.getInstance();

        // decide a status
        String status11 = "";
        if(Integer.parseInt(sys1) < 120 && Integer.parseInt(dia1) < 80){
            status11 +="Normal";
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if((Integer.parseInt(sys1) <= 129) && (Integer.parseInt(sys1) >= 120) && Integer.parseInt(dia1) < 80){
            status11 +="Elevated";
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if ((Integer.parseInt(sys1) <= 139 && Integer.parseInt(sys1) >= 130) || (Integer.parseInt(dia1) >= 80
                && Integer.parseInt(dia1) <= 89)){
            status11 +="High Blood Pressure(Stage1)";
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if((Integer.parseInt(sys1) > 180) || (Integer.parseInt(dia1) > 120 )){
            status11 +="Hypertensive Crisis";
            confirm();
        }
        else {
            status11 +="High Blood Pressure(Stage2)";
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
            writeToDatabase(fam, calendar.getTime(), sys1, dia1, status11);
    }

    //write to database
    public void writeToDatabase(String fam, Date date, String sys, String dia, String condition) {

        Map<String, Object> task = new HashMap<>();

        task.put("FamilyMember", fam);
        task.put("Date", date);
        task.put("Systolic Pressure", sys);
        task.put("Diastolic Pressure", dia);
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