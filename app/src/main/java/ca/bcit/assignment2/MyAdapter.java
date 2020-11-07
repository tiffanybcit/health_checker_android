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
Adapter class
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Entry> articleList;
    private Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public MyAdapter(ArrayList<Entry> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(v);
    }




    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        final Entry item = articleList.get(position);
        holder.line1.setText("Device ID: " + item.get_serialNum());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.US);
        String dateString = format.format(item.get_date());

        holder.line2.setText("Date: " + dateString);
        holder.line3.setText("Sys: " + item.get_sys());
        holder.line4.setText("Dia: " + item.get_dia());
        holder.line5.setText("Condition: "+ item.get_condition());
        holder.overall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), Details.class);
                Toast.makeText(context, "You just clicked something", Toast.LENGTH_SHORT).show();
                intent.putExtra("entry", item);
                view.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return articleList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView line1;
        public TextView line2;
        public TextView line3;
        public TextView line4;
        public TextView line5;
        public Button delete;
        public Button edit;
        public LinearLayout overall;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            line1 = itemView.findViewById(R.id.textViewSerialNum);
            line2 = itemView.findViewById(R.id.textViewDate);
            line3 = itemView.findViewById(R.id.textViewSysReading);
            line4 = itemView.findViewById(R.id.textViewDiaReading);
            line5 = itemView.findViewById(R.id.textViewCondition);
            delete = itemView.findViewById(R.id.textDeleteEntry);
            edit = itemView.findViewById(R.id.textEditEntry);
            overall = itemView.findViewById(R.id.previewSummary);
        }
    }

}

