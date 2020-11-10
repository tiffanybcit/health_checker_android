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
                getReport();
//setReport();
            }
        });
        recyclerView = findViewById(R.id.item_list);
        recyclerView.setHasFixedSize(true);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView2 = findViewById(R.id.item_list2);
//        recyclerView2.setHasFixedSize(true);
//        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        context = getApplicationContext();
        arraySpinner = context.getResources().getStringArray(R.array.familyMember);

        s = (Spinner) findViewById(R.id.spinner_time);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);


//        String text = spinner.getSelectedItem().toString();
        listItems = new ArrayList<>();
        setQuene();

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("fammember", Integer.toString(s.getSelectedItemPosition()));
        outState.putString("sysreading", editTextSys.getText().toString());
        outState.putString("diareading", editTextDia.getText().toString());
    }

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

//    //prepare and send to report page
    public void getReport(){
        ArrayList<String> familyMemberList = new ArrayList<String>();
        ArrayList<String> sysList = new ArrayList<String>();
        ArrayList<String> diaList = new ArrayList<String>();
        ArrayList<String> conditionList = new ArrayList<String>();
        int temp_sys1 = 0;
        int temp_dia1 = 0;
        int counter1 = 0;
        double div_temp_sys1 = 0;
        double div_temp_dia1 = 0;
        String condition1 = "";

        int temp_sys2 = 0;
        int temp_dia2 = 0;
        int counter2 = 0;
        double div_temp_sys2 = 0;
        double div_temp_dia2 = 0;
        String condition2 = "";

        int temp_sys3 = 0;
        int temp_dia3 = 0;
        int counter3 = 0;
        double div_temp_sys3 = 0;
        double div_temp_dia3 = 0;
        String condition3 = "";

        int temp_sys4 = 0;
        int temp_dia4 = 0;
        int counter4 = 0;
        double div_temp_sys4 = 0;
        double div_temp_dia4 = 0;
        String condition4 = "";

        Calendar calendar = Calendar.getInstance();
        String monthNow  = (String) DateFormat.format("MM",   calendar); // 06
        String yearNow = (String) DateFormat.format("yyyy", calendar); // 2013

        for (int j = 0; j < listItems.size(); j++) {
            String monthThen = (String) DateFormat.format("MM", listItems.get(j).get_date());

            String yearThen = (String) DateFormat.format("yyyy", listItems.get(j).get_date());

            if ("father@home.com".equals(listItems.get(j).get_familyMember())) {
                if (monthNow.equals(monthThen) && yearNow.equals(yearThen)) {

                    temp_sys1 += Integer.parseInt(listItems.get(j).get_sys());
                    temp_dia1 += Integer.parseInt(listItems.get(j).get_dia());
                    counter1 = counter1 + 1;
                }
            }
        }
            if(counter1 == 0){
                div_temp_sys1 = (double)0;
                div_temp_dia1 = (double)0;

            } else {
                div_temp_sys1 = (double)temp_sys1 / counter1;
                div_temp_dia1 = (double)temp_dia1 / counter1;
            }
            if (div_temp_sys1 ==0 && div_temp_dia1 ==0){
                condition1 += "No reading for this month!";
            }
            else if(div_temp_sys1 < 120 && div_temp_dia1 < 80){
                condition1 +="Normal";
            } else if(div_temp_sys1 < 129 && div_temp_dia1 > 120 && div_temp_dia1 < 80){
                condition1 +="Elevated";
            } else if ((div_temp_sys1 < 139 && div_temp_sys1 > 130) || (div_temp_dia1 > 80 && div_temp_dia1 < 89)){
                condition1 +="High Blood Pressure (Stage1)";
            } else if(div_temp_sys1 > 180 || div_temp_dia1 > 120 ){
                condition1 +="Hypertensive Crisis";
            }
            else {
                condition1 +="High Blood Pressure (Stage2)";
            }

            familyMemberList.add("father@home.com");
            DecimalFormat df = new DecimalFormat("0.00");

            sysList.add(df.format(div_temp_sys1));
            diaList.add(df.format(div_temp_dia1));
            conditionList.add(condition1);

        for (int j = 0; j < listItems.size(); j++) {
            String monthThen = (String) DateFormat.format("MM", listItems.get(j).get_date());

            String yearThen = (String) DateFormat.format("yyyy", listItems.get(j).get_date());

            if ("mother@home.com".equals(listItems.get(j).get_familyMember())) {
                if (monthNow.equals(monthThen) && yearNow.equals(yearThen)) {

                    temp_sys2 += Integer.parseInt(listItems.get(j).get_sys());
                    temp_dia2 += Integer.parseInt(listItems.get(j).get_dia());
                    counter2 = counter2 + 1;
                }
            }
        }
        if(counter2 == 0){
            div_temp_sys2 = (double)0;
            div_temp_dia2 = (double)0;

        } else {
            div_temp_sys2 = (double)temp_sys2 / counter2;
            div_temp_dia2 = (double)temp_dia2 / counter2;
        }
        if (div_temp_sys2 ==0 && div_temp_dia2 ==0){
            condition2 += "No reading for this month!";
        }
        else if(div_temp_sys2 < 120 && div_temp_dia2 < 80){
            condition2 +="Normal";
        } else if(div_temp_sys2 < 129 && div_temp_dia2 > 120 && div_temp_dia2 < 80){
            condition2 +="Elevated";
        } else if ((div_temp_sys2 < 139 && div_temp_sys2 > 130) || (div_temp_dia2 > 80 && div_temp_dia2 < 89)){
            condition2 +="High Blood Pressure (Stage1)";
        } else if(div_temp_sys2 > 180 || div_temp_dia2 > 120 ){
            condition2 +="Hypertensive Crisis";
        }
        else {
            condition2 +="High Blood Pressure (Stage2)";
        }

        familyMemberList.add("mother@home.com");
        sysList.add(df.format(div_temp_sys2));
        diaList.add(df.format(div_temp_dia2));
        conditionList.add(condition2);



        for (int j = 0; j < listItems.size(); j++) {
            String monthThen = (String) DateFormat.format("MM", listItems.get(j).get_date());

            String yearThen = (String) DateFormat.format("yyyy", listItems.get(j).get_date());

            if ("grandma@home.com".equals(listItems.get(j).get_familyMember())) {
                if (monthNow.equals(monthThen) && yearNow.equals(yearThen)) {

                    temp_sys3 += Integer.parseInt(listItems.get(j).get_sys());
                    temp_dia3 += Integer.parseInt(listItems.get(j).get_dia());
                    counter3 = counter3 + 1;
                }
            }
        }
        if(counter3 == 0){
            div_temp_sys3 = (double)0;
            div_temp_dia3 = (double)0;

        } else {
            div_temp_sys3 = (double)temp_sys3 / counter3;
            div_temp_dia3 = (double)temp_dia3 / counter3;
        }
        if (div_temp_sys3 ==0 && div_temp_dia3 ==0){
            condition3 += "No reading for this month!";
        }
        else if(div_temp_sys3 < 120 && div_temp_dia3 < 80){
            condition3 +="Normal";
        } else if(div_temp_sys3 < 129 && div_temp_dia3 > 120 && div_temp_dia3 < 80){
            condition3 +="Elevated";
        } else if ((div_temp_sys3 < 139 && div_temp_sys3 > 130) || (div_temp_dia3 > 80 && div_temp_dia3 < 89)){
            condition3 +="High Blood Pressure (Stage1)";
        } else if(div_temp_sys3 > 180 || div_temp_dia3 > 120 ){
            condition3 +="Hypertensive Crisis";
        }
        else {
            condition3 +="High Blood Pressure (Stage2)";
        }

        familyMemberList.add("grandma@home.com");
        sysList.add(df.format(div_temp_sys3));
        diaList.add(df.format(div_temp_dia3));
        conditionList.add(condition3);



        for (int j = 0; j < listItems.size(); j++) {
            String monthThen = (String) DateFormat.format("MM", listItems.get(j).get_date());

            String yearThen = (String) DateFormat.format("yyyy", listItems.get(j).get_date());

            if ("grandpa@home.com".equals(listItems.get(j).get_familyMember())) {
                if (monthNow.equals(monthThen) && yearNow.equals(yearThen)) {

                    temp_sys4 += Integer.parseInt(listItems.get(j).get_sys());
                    temp_dia4 += Integer.parseInt(listItems.get(j).get_dia());
                    counter4 = counter4 + 1;
                }
            }
        }
        if(counter4 == 0){
            div_temp_sys4 = (double)0;
            div_temp_dia4 = (double)0;

        } else {
            div_temp_sys4 = (double)temp_sys4 / counter4;
            div_temp_dia4 = (double)temp_dia4 / counter4;
        }
        if (div_temp_sys4 == 0 && div_temp_dia4 == 0){
            condition4 += "No reading for this month!";
        }
        else if(div_temp_sys4 < 120 && div_temp_dia4 < 80){
            condition4 +="Normal";
        } else if(div_temp_sys4 < 129 && div_temp_dia4 > 120 && div_temp_dia4 < 80){
            condition4 +="Elevated";
        } else if ((div_temp_sys4 < 139 && div_temp_sys4 > 130) || (div_temp_dia4 > 80 && div_temp_dia4 < 89)){
            condition4 +="High Blood Pressure (Stage1)";
        } else if(div_temp_sys4 > 180 || div_temp_dia4 > 120 ){
            condition4 +="Hypertensive Crisis";
        }
        else {
            condition4 +="High Blood Pressure (Stage2)";
        }

        familyMemberList.add("grandpa@home.com");
        sysList.add(df.format(div_temp_sys4));
        diaList.add(df.format(div_temp_dia4));
        conditionList.add(condition4);


        Intent intent = new Intent(this, Report.class);
        Toast.makeText(this, "You just clicked to see your report", Toast.LENGTH_SHORT).show();
        intent.putExtra("familyMem1", familyMemberList.get(0));
        intent.putExtra("sys1", sysList.get(0));
        intent.putExtra("dia1", diaList.get(0));
        intent.putExtra("condition1", conditionList.get(0));

        intent.putExtra("familyMem2", familyMemberList.get(1));
        intent.putExtra("sys2", sysList.get(1));
        intent.putExtra("dia2", diaList.get(1));
        intent.putExtra("condition2", conditionList.get(1));

        intent.putExtra("familyMem3", familyMemberList.get(2));
        intent.putExtra("sys3", sysList.get(2));
        intent.putExtra("dia3", diaList.get(2));
        intent.putExtra("condition3", conditionList.get(2));

        intent.putExtra("familyMem4", familyMemberList.get(3));
        intent.putExtra("sys4", sysList.get(3));
        intent.putExtra("dia4", diaList.get(3));
        intent.putExtra("condition4", conditionList.get(3));

        startActivity(intent);
    }
//    //read data from firebase to get to a list
//    public void setReport(){
//        int temp_sys1 = 0;
//        int temp_dia1 = 0;
//        int counter1 = 0;
//        int div_temp_sys1 = 0;
//        int div_temp_dia1 = 0;
//        String condition1 = "";
//
//
//        Calendar calendar = Calendar.getInstance();
//        String monthNow  = (String) DateFormat.format("MM",   calendar); // 06
//        String yearNow = (String) DateFormat.format("yyyy", calendar); // 2013
//    for (int i = 0; i < arraySpinner.length; i++) {
//        for (int j = 0; j < listItems.size(); j++) {
//        String monthThen = (String) DateFormat.format("MM", listItems.get(j).get_date());
//
//        String yearThen = (String) DateFormat.format("yyyy", listItems.get(j).get_date());
//
//        if ("father@home.com".equals(listItems.get(j).get_familyMember())) {
//            if (monthNow.equals(monthThen) && yearNow.equals(yearThen)) {
//
//                temp_sys1 += Integer.parseInt(listItems.get(j).get_sys());
//                temp_dia1 += Integer.parseInt(listItems.get(j).get_dia());
//                counter1 = counter1 + 1;
//            }
//        }
//    }
//    if (counter1 == 0) {
//        div_temp_sys1 = 0;
//        div_temp_dia1 = 0;
//
//    } else {
//        div_temp_sys1 = temp_sys1 / counter1;
//        div_temp_dia1 = temp_dia1 / counter1;
//    }
//    if (div_temp_sys1 == 0 && div_temp_dia1 == 0) {
//        condition1 += "No reading for this month!";
//    } else if (div_temp_sys1 < 120 && div_temp_dia1 < 80) {
//        condition1 += "Normal";
//    } else if (div_temp_sys1 < 129 && div_temp_dia1 > 120 && div_temp_dia1 < 80) {
//        condition1 += "Elevated";
//    } else if ((div_temp_sys1 < 139 && div_temp_sys1 > 130) || (div_temp_dia1 > 80 && div_temp_dia1 < 89)) {
//        condition1 += "High Blood Pressure(Stage1)";
//    } else if (div_temp_sys1 > 180 || div_temp_dia1 > 120) {
//        condition1 += "Hypertensive Crisis";
//    } else {
//        condition1 += "High Blood Pressure(Stage2)";
//    }
//    ReportElement temp = new ReportElement(arraySpinner[i], div_temp_sys1, div_temp_dia1, condition1);
//    familyConditions.add(temp);
//
//}
//        adapter2 = new ReportAdapter(familyConditions, getApplicationContext());
//        recyclerView2.setAdapter(adapter2);
//        Intent intent = new Intent(this, Report.class);
//        Toast.makeText(this, "You just clicked for report", Toast.LENGTH_SHORT).show();
//        intent.putExtra("familyMem1", familyConditions);
//    }
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
//                                Log.d("debug", "id "+(String)document.getId());
                                String temp_fam = (String) document.getData().get("FamilyMember");
//                                Log.d("debug", "Task "+(String) document.getData().get("task"));

                                String temp_sys = (String) document.getData().get("Systolic Pressure");
//                                Log.d("debug", "Who "+(String) document.getData().get("who"));
                                String temp_dia = (String) document.getData().get("Diastolic Pressure");
                                com.google.firebase.Timestamp temp_time =
                                        (com.google.firebase.Timestamp) document.getData().get("Date");

//                                Log.d("debug", "Date "+temp_time.toDate());
                                String temp_status = (String) document.getData().get("Condition");

                                Entry book123 = new Entry(temp_id, temp_fam, temp_time.toDate(), temp_sys, temp_dia, temp_status);
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
    // Function to check if a string
    // contains only digits
    public static boolean onlyDigits(String str)
    {
        // Traverse the string from
        // start to end
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= '0'
                    && str.charAt(i) <= '9') {
//                Log.d("debug", Character.toString(str.charAt(i)));
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
//        Log.d("debugg", sys1);
        String dia1 = editTextDia.getText().toString().trim();
//        Log.d("debugg", dia1);
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

//        String deviceId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

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
        status11 +="High Blood Pressure(Stage1)";
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }else if((Integer.parseInt(sys1) > 180) || (Integer.parseInt(dia1) > 120 )){
        status11 +="Hypertensive Crisis";
        confirm();
    }
    else {
        status11 +="High Blood Pressure(Stage2)";
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
        writeToDatabase(fam, calendar.getTime(), sys1, dia1, status11);
//        finish();
//        overridePendingTransition( 0, 0);
//        startActivity(getIntent());
//        overridePendingTransition( 0, 0);


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