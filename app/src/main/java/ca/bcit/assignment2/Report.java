package ca.bcit.assignment2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//monthly report
public class Report extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView title;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter2;
    Context context;
    private ArrayList<Entry> listItems;
    private ArrayList<AverageReading> listItems2;
    ArrayList<String> familyMemberList = new ArrayList<String>();
    ArrayList<String> sysList = new ArrayList<String>();
    ArrayList<String> diaList = new ArrayList<String>();
    ArrayList<String> conditionList = new ArrayList<String>();
    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = findViewById(R.id.title2);
        recyclerView = findViewById(R.id.monthlyReportList);
        recyclerView.setHasFixedSize(true);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        context = getApplicationContext();
        listItems = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        String monthNow  = (String) DateFormat.format("MMM",   calendar); // JUN
        String yearNow = (String) DateFormat.format("yyyy", calendar); // 2013
        String monthToDate = "Month-to-date average readings for " + monthNow + " " + yearNow;
        title.setText(monthToDate);


        db.collection("readings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document :task.getResult()) {
                                String temp_id = (String)document.getId();
                                Log.d("debug", temp_id);

                                String temp_fam = (String) document.getData().get("FamilyMember");

                                String temp_sys = (String) document.getData().get("Systolic Pressure");

                                String temp_dia = (String) document.getData().get("Diastolic Pressure");
                                com.google.firebase.Timestamp temp_time =
                                        (com.google.firebase.Timestamp) document.getData().get("Date");

                                String temp_status = (String) document.getData().get("Condition");

                                Entry book123 = new Entry(temp_id, temp_fam, temp_time.toDate(), temp_sys, temp_dia, temp_status);
                                listItems.add(book123);
                            }
                            getReport();
                            listItems2 = new ArrayList<>();
                            setQuene();
                        } else {
                            Log.d("debug", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    //read data from firebase to get to a list
    public void setQuene(){

        for (int k = 0; k < familyMemberList.size(); k++) {
            String temp_fam = familyMemberList.get(k);
            String temp_sys = sysList.get(k);
            String temp_dia = diaList.get(k);
            String temp_status = conditionList.get(k);
            AverageReading book1234 = new AverageReading(temp_fam, temp_sys, temp_dia, temp_status);
            listItems2.add(book1234);
        }
            adapter2 = new MyReportAdapter(listItems2, getApplicationContext());
            recyclerView.setAdapter(adapter2);
    }
    void getReport(){
        // get first family member
        int temp_sys1 = 0;
        int temp_dia1 = 0;
        int counter1 = 0;
        double div_temp_sys1 = 0;
        double div_temp_dia1 = 0;
        String condition1 = "";

        // get second family member
        int temp_sys2 = 0;
        int temp_dia2 = 0;
        int counter2 = 0;
        double div_temp_sys2 = 0;
        double div_temp_dia2 = 0;
        String condition2 = "";

        // get third family member
        int temp_sys3 = 0;
        int temp_dia3 = 0;
        int counter3 = 0;
        double div_temp_sys3 = 0;
        double div_temp_dia3 = 0;
        String condition3 = "";

        // get fourth family member
        int temp_sys4 = 0;
        int temp_dia4 = 0;
        int counter4 = 0;
        double div_temp_sys4 = 0;
        double div_temp_dia4 = 0;
        String condition4 = "";

        //date and time now
        Calendar calendar = Calendar.getInstance();
        String monthNow  = (String) DateFormat.format("MM",   calendar); // 06
        String yearNow = (String) DateFormat.format("yyyy", calendar); // 2013

        // for first family member
        for (int j = 0; j < listItems.size(); j++) {

            String monthThen = (String) DateFormat.format("MM", listItems.get(j).get_date());

            String yearThen = (String) DateFormat.format("yyyy", listItems.get(j).get_date());

            if ("father@home.com".equals(listItems.get(j).get_familyMember())) {
                if (monthNow.equals(monthThen) && yearNow.equals(yearThen)) {
                    if(listItems.get(j).get_sys() == null || listItems.get(j).get_dia() == null){
                        continue;
                    }

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
        } else if(div_temp_sys1 <= 129 && div_temp_dia1 >= 120 && div_temp_dia1 < 80){
            condition1 +="Elevated";
        } else if ((div_temp_sys1 <= 139 && div_temp_sys1 >= 130) || (div_temp_dia1 >= 80 && div_temp_dia1 <= 89)){
            condition1 +="High Blood Pressure (Stage1)";
        } else if(div_temp_sys1 > 180 || div_temp_dia1 > 120 ){
            condition1 +="Hypertensive Crisis";
        }
        else {
            condition1 +="High Blood Pressure (Stage2)";
        }

        //add to arraylist and will be used later
        if(counter1 == 0){

        } else {
            familyMemberList.add("father@home.com");
            DecimalFormat df = new DecimalFormat("0.00");

            sysList.add(df.format(div_temp_sys1));
            diaList.add(df.format(div_temp_dia1));
            conditionList.add(condition1);
        }

        // for second family member
        for (int j = 0; j < listItems.size(); j++) {

            String monthThen = (String) DateFormat.format("MM", listItems.get(j).get_date());

            String yearThen = (String) DateFormat.format("yyyy", listItems.get(j).get_date());


            if ("mother@home.com".equals(listItems.get(j).get_familyMember())) {
                if (monthNow.equals(monthThen) && yearNow.equals(yearThen)) {
                    if(listItems.get(j).get_sys() == null || listItems.get(j).get_dia() == null){
                        continue;
                    }

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
        } else if(div_temp_sys2 <= 129 && div_temp_dia2 >= 120 && div_temp_dia2 < 80){
            condition2 +="Elevated";
        } else if ((div_temp_sys2 <= 139 && div_temp_sys2 >= 130) || (div_temp_dia2 >= 80 && div_temp_dia2 <= 89)){
            condition2 +="High Blood Pressure (Stage1)";
        } else if(div_temp_sys2 > 180 || div_temp_dia2 > 120 ){
            condition2 +="Hypertensive Crisis";
        }
        else {
            condition2 +="High Blood Pressure (Stage2)";
        }
        if(counter2 == 0){

        } else {
            //add to arraylist and will be used later
            familyMemberList.add("mother@home.com");
            sysList.add(df.format(div_temp_sys2));
            diaList.add(df.format(div_temp_dia2));
            conditionList.add(condition2);
        }

        // for third family member
        for (int j = 0; j < listItems.size(); j++) {

            String monthThen = (String) DateFormat.format("MM", listItems.get(j).get_date());

            String yearThen = (String) DateFormat.format("yyyy", listItems.get(j).get_date());

            if ("grandma@home.com".equals(listItems.get(j).get_familyMember())) {
                if (monthNow.equals(monthThen) && yearNow.equals(yearThen)) {
                    if(listItems.get(j).get_sys() == null || listItems.get(j).get_dia() == null){
                        continue;
                    }

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
        } else if(div_temp_sys3 <= 129 && div_temp_dia3 >= 120 && div_temp_dia3 < 80){
            condition3 +="Elevated";
        } else if ((div_temp_sys3 <= 139 && div_temp_sys3 >= 130) || (div_temp_dia3 >= 80 && div_temp_dia3 <= 89)){
            condition3 +="High Blood Pressure (Stage1)";
        } else if(div_temp_sys3 > 180 || div_temp_dia3 > 120 ){
            condition3 +="Hypertensive Crisis";
        }
        else {
            condition3 +="High Blood Pressure (Stage2)";
        }
        if(counter3 == 0){

        } else {
            //add to arraylist and will be used later
            familyMemberList.add("grandma@home.com");
            sysList.add(df.format(div_temp_sys3));
            diaList.add(df.format(div_temp_dia3));
            conditionList.add(condition3);
        }


        // for fourth family member
        for (int j = 0; j < listItems.size(); j++) {

            String monthThen = (String) DateFormat.format("MM", listItems.get(j).get_date());

            String yearThen = (String) DateFormat.format("yyyy", listItems.get(j).get_date());

            if ("grandpa@home.com".equals(listItems.get(j).get_familyMember())) {
                if (monthNow.equals(monthThen) && yearNow.equals(yearThen)) {
                    if(listItems.get(j).get_sys() == null || listItems.get(j).get_dia() == null){
                        continue;
                    }

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
        } else if(div_temp_sys4 <= 129 && div_temp_dia4 >= 120 && div_temp_dia4 < 80){
            condition4 +="Elevated";
        } else if ((div_temp_sys4 <= 139 && div_temp_sys4 >= 130) || (div_temp_dia4 >= 80 && div_temp_dia4 <= 89)){
            condition4 +="High Blood Pressure (Stage1)";
        } else if(div_temp_sys4 > 180 || div_temp_dia4 > 120 ){
            condition4 +="Hypertensive Crisis";
        }
        else {
            condition4 +="High Blood Pressure (Stage2)";
        }

        if(counter4 == 0){

        } else {
            //add to arraylist and will be used later
            familyMemberList.add("grandpa@home.com");
            sysList.add(df.format(div_temp_sys4));
            diaList.add(df.format(div_temp_dia4));
            conditionList.add(condition4);
        }
    }


}
