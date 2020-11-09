package ca.bcit.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

//monthly report
public class Report extends AppCompatActivity {
    TextView fam1;
    TextView sys1;
    TextView dia1;
    TextView condition1;

    TextView fam2;
    TextView sys2;
    TextView dia2;
    TextView condition2;


    TextView fam3;
    TextView sys3;
    TextView dia3;
    TextView condition3;


    TextView fam4;
    TextView sys4;
    TextView dia4;
    TextView condition4;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.title2);
        fam1 = findViewById(R.id.family1);
        sys1 = findViewById(R.id.avrsys1);
        dia1 = findViewById(R.id.avrdia1);
        condition1 = findViewById(R.id.avrcondition1);

        fam2 = findViewById(R.id.family2);
        sys2 = findViewById(R.id.avrsys2);
        dia2 = findViewById(R.id.avrdia2);

        condition2 = findViewById(R.id.avrcondition2);


        fam3 = findViewById(R.id.family3);
        sys3 = findViewById(R.id.avrsys3);
        dia3 = findViewById(R.id.avrdia3);

        condition3 = findViewById(R.id.avrcondition3);


        fam4 = findViewById(R.id.family4);
        sys4 = findViewById(R.id.avrsys4);
        dia4 = findViewById(R.id.avrdia4);

        condition4 = findViewById(R.id.avrcondition4);


        //Received from the last page
        Intent intent = getIntent();

        fam1.setText("Family Member: "+ intent.getStringExtra("familyMem1"));
        sys1.setText("Sys: " + intent.getStringExtra("sys1"));
        dia1.setText("Dia: " + intent.getStringExtra("dia1"));
        condition1.setText("Condition: "+ intent.getStringExtra("condition1"));

        fam2.setText("Family Member: "+ intent.getStringExtra("familyMem2"));
        sys2.setText("Sys: " + intent.getStringExtra("sys2"));
        dia2.setText("Dia: " + intent.getStringExtra("dia2"));
        condition2.setText("Condition: "+ intent.getStringExtra("condition2"));


        fam3.setText("Family Member: "+ intent.getStringExtra("familyMem3"));
        sys3.setText("Sys: " + intent.getStringExtra("sys3"));
        dia3.setText("Dia: " + intent.getStringExtra("dia3"));
        condition3.setText("Condition: "+ intent.getStringExtra("condition3"));


        fam4.setText("Family Member: "+ intent.getStringExtra("familyMem4"));
        sys4.setText("Sys: " + intent.getStringExtra("sys4"));
        dia4.setText("Dia: " + intent.getStringExtra("dia4"));
        condition4.setText("Condition: "+ intent.getStringExtra("condition4"));


        Calendar calendar = Calendar.getInstance();

        String monthNow  = (String) DateFormat.format("MMM",   calendar); // JUN
        String yearNow = (String) DateFormat.format("yyyy", calendar); // 2013
        title.setText("Month-to-date average readings for " + monthNow + " " + yearNow);
    }


}
