package com.example.techlinkmobileallinone.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techlinkmobileallinone.HomeActivity;
import com.example.techlinkmobileallinone.LoginActivity;
import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.model.ButtonDataClass;

public class ButtonAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private Context context;
    private List<ButtonDataClass> dataList;
    SubMethods subMethods = new SubMethods();
    public void setSearchList(List<ButtonDataClass> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }
    public ButtonAdapter(Context context, List<ButtonDataClass> dataList){
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_button_recycler, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recImage.setImageResource(dataList.get(position).getDataImage());
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        holder.recDept.setText(dataList.get(position).getDataDept());
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataList.get(holder.getAdapterPosition()).getDataPermission())
                {
                    Intent intent = new Intent(context, dataList.get(holder.getAdapterPosition()).getDataActivity());
                    intent.putExtra("empCode", dataList.get(holder.getAdapterPosition()).getUserCode());
                    intent.putExtra("empName", dataList.get(holder.getAdapterPosition()).getUserName());
                    context.startActivity(intent);
                }else{
                    subMethods.showInformationDialog("Thông báo 报信", "Không có quyền hạn để truy cập.\n无权限访问。", (Activity) context);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recImage;
    TextView recTitle, recDept;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recTitle = itemView.findViewById(R.id.recTitle);
        recDept = itemView.findViewById(R.id.recDept);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
