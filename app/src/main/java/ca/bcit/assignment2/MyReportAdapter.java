package ca.bcit.assignment2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/*
Adapter class for the report
 */
public class MyReportAdapter extends RecyclerView.Adapter<MyReportAdapter.ViewHolder> {
    private ArrayList<AverageReading> articleList;
    private Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public MyReportAdapter(ArrayList<AverageReading> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_preview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReportAdapter.ViewHolder holder, int position) {
        final AverageReading item = articleList.get(position);
        String line1Holder = "Family Member: " + item.getFamilyMember();
        holder.line1.setText(line1Holder);


        String line3Holder = "Systolic Pressure: " + item.getSys();
        holder.line3.setText(line3Holder);
        String line4Holder = "Diastolic Pressure: " + item.getDia();
        holder.line4.setText(line4Holder);
        String line5Holder = "Condition: "+ item.getCondition();
        holder.line5.setText(line5Holder);

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView line1;
        public TextView line3;
        public TextView line4;
        public TextView line5;
        public Button delete;
        public Button edit;
        public LinearLayout overall;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            line1 = itemView.findViewById(R.id.textViewFamilyMember2);
            line3 = itemView.findViewById(R.id.textViewSysReading2);
            line4 = itemView.findViewById(R.id.textViewDiaReading2);
            line5 = itemView.findViewById(R.id.textViewCondition2);
            delete = itemView.findViewById(R.id.textDeleteEntry);
            overall = itemView.findViewById(R.id.previewSummary2);
        }
    }

}

