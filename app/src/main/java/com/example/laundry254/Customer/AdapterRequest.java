package com.example.laundry254.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundry254.Model.Requests;
import com.example.laundry254.R;

import java.util.List;

public class AdapterRequest extends RecyclerView.Adapter<AdapterRequest.myHolder>  {

   Context context;
   List<Requests> requestsList;
   //constructor

    public AdapterRequest(Context context, List<Requests> requestsList) {
        this.context = context;
        this.requestsList = requestsList;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //inflate layout (row_user.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_request, parent);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int i) {

        //getdata
        final String requsetName = requestsList.get(i).getName();
        String requestDate = requestsList.get(i).getRequestDate().toString();
        String requsetDesc = requestsList.get(i).getDescription();
        String requsetPickdate = requestsList.get(i).getPickDate().toString();
        String requestStastus = requestsList.get(i).getStatus();

        //set data
        holder.mNameTv.setText(requsetName);
        holder.mPlaceDateTv.setText(requestDate);
        holder.mDescriprionTv.setText(requsetDesc);
        holder.mPickdate.setText(requsetPickdate);
        holder.mStatus.setText(requestStastus);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" +requsetName, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return  requestsList.size();
    }


    //view holder class

    class myHolder extends RecyclerView.ViewHolder{

        TextView mNameTv, mPlaceDateTv,mDescriprionTv, mPickdate, mStatus;
        Button btn_view;
        public myHolder(@NonNull View itemView) {
            super(itemView);

            //init views

            mNameTv = itemView.findViewById(R.id.tvRequestName);
            mPlaceDateTv = itemView.findViewById(R.id.tvRequestDate);
            mDescriprionTv = itemView.findViewById(R.id.tvRequestDesc);
            mPickdate = itemView.findViewById(R.id.tvPickdate);
            mStatus = itemView.findViewById(R.id.tvStatus);
            btn_view = itemView.findViewById(R.id.btnView);
        }
    }
}
