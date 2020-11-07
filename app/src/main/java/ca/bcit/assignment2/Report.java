package ca.bcit.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

//monthly report
public class Report extends AppCompatActivity {
    TextView device;
    TextView sys;
    TextView dia;
    TextView condition;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.title2);
        device = findViewById(R.id.deviceID);
        sys = findViewById(R.id.avrsys);
        dia = findViewById(R.id.avrdia);

        condition = findViewById(R.id.avrcondition);



        //Received from the last page
        Intent intent = getIntent();

        device.setText("Device ID: "+ intent.getStringExtra("id"));
        sys.setText("Sys: " + intent.getStringExtra("sys"));
        dia.setText("Dia: " + intent.getStringExtra("dia"));
        condition.setText("Condition: "+ intent.getStringExtra("condition"));
        Calendar calendar = Calendar.getInstance();

        String monthNow  = (String) DateFormat.format("MMM",   calendar); // JUN
        String yearNow = (String) DateFormat.format("yyyy", calendar); // 2013
        title.setText("Month-to-date average readings for " + monthNow + " " + yearNow);
    }

}
