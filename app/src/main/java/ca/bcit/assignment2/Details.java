package ca.bcit.assignment2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
    String[] arraySpinner;
    Context context;
    Spinner familyMem;
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
        context = getApplicationContext();
        arraySpinner = context.getResources().getStringArray(R.array.familyMember);

        familyMem = findViewById(R.id.familyMem);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        familyMem.setAdapter(adapter);



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
//        article.get_familyMember();
        getIndex(familyMem, article.get_familyMember());
        familyMem.setSelection(getIndex(familyMem, article.get_familyMember()));
        String dateString = format.format(article.get_date());

        String date = dateString.substring(0, 11);
        String time = dateString.substring(11);


        String dateLabel = "Date: " + date;
        Date.setText(dateLabel);

        String timeLabel = "Time: " + time;
        Time.setText(timeLabel);

        sys1.setText(article.get_sys());
        dia1.setText(article.get_dia());

        //delete from database
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(article.get_id());
                Intent intent = new Intent(view.getContext(), MainActivity.class);


                view.getContext().startActivity(intent);
            }
        });

        //edit the existing item
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInput(article.get_id());
//                Intent intent = new Intent(view.getContext(), MainActivity.class);

//                view.getContext().startActivity(intent);
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
                        Toast.makeText(context, "Delete Successful!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
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
    //get user input to be used later in update function
    public void getInput(String id){
        String fam = familyMem.getSelectedItem().toString();
        String sys = sys1.getText().toString().trim();
        String dia = dia1.getText().toString().trim();
        if(!onlyDigits(sys)){
            Toast.makeText(this, "You must enter a whole number.", Toast.LENGTH_LONG).show();
            return;
        }
        if(!onlyDigits(dia)){
            Toast.makeText(this, "You must enter a whole number.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(sys)) {
            Toast.makeText(this, "You must enter your systolic pressure.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(dia)) {
            Toast.makeText(this, "You must enter your diastolic pressure.", Toast.LENGTH_LONG).show();
            return;
        }

        String status11 = "";
        if(Integer.parseInt(sys) < 120 && Integer.parseInt(dia) < 80){
            status11 +="Normal";
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if((Integer.parseInt(sys) < 129) && (Integer.parseInt(sys) > 120) && Integer.parseInt(dia) < 80){
            status11 +="Elevated";
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if ((Integer.parseInt(sys) < 139 && Integer.parseInt(sys) > 130) || (Integer.parseInt(dia) > 80 && Integer.parseInt(dia) < 89)){
            status11 +="High Blood Pressure(Stage1)";
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if((Integer.parseInt(sys) > 180) || (Integer.parseInt(dia) > 120 )){
            status11 +="Hypertensive Crisis";
            confirm();
        } else {
            status11 +="High Blood Pressure(Stage2)";
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }
        update(fam, status11, sys, dia, id);


//        finish();
//        overridePendingTransition( 0, 0);
//        startActivity(getIntent());
//        overridePendingTransition( 0, 0);

    }

    //update helper function
    public void update(String fam, String status, String sys, String dia, String id){
        Map<String, Object> temp = new HashMap<>();
        temp.put("FamilyMember", fam);
        temp.put("Systolic Pressure", sys);
        temp.put("Diastolic Pressure", dia);
        temp.put("Condition", status);
        Calendar calendar = Calendar.getInstance();
        temp.put("Date", calendar.getTime());

        db.collection("readings").document(id)
                .set(temp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag", "DocumentSnapshot successfully written!");
                        Toast.makeText(context, "Update Successful!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Tag", "Error writing document", e);
                    }
                });
    }
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
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
}
