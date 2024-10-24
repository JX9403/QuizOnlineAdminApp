package com.example.onlinequizadminapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinequizadminapp.Models.CategoryModel;
import com.example.onlinequizadminapp.Models.SubCategoryModel;
import com.example.onlinequizadminapp.QuestionsActivity;
import com.example.onlinequizadminapp.R;
import com.example.onlinequizadminapp.SubCategoryActivity;
import com.example.onlinequizadminapp.databinding.RvCategoryDesignBinding;
import com.example.onlinequizadminapp.databinding.RvSubcategoryDesignBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.viewHolder>{

    Context context;
    ArrayList<SubCategoryModel> list;

    private  String catId;
    private  String subCatId;


    public SubCategoryAdapter(Context context, ArrayList<SubCategoryModel> list, String catId) {
        this.context = context;
        this.list = list;
        this.catId = catId;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_subcategory_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        SubCategoryModel subCategoryModel = list.get(position);
        holder.binding.subCategoryName.setText(subCategoryModel.getCategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuestionsActivity.class);
                intent.putExtra("catId", catId);
                intent.putExtra("subCatId", subCategoryModel.getKey());

                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this category?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseDatabase.getInstance().getReference().child("categories").child(catId).child("subCategories").child(subCategoryModel.getKey())
                            .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                });
                builder.setNegativeButton("No", (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public  class viewHolder extends  RecyclerView.ViewHolder{
        RvSubcategoryDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = RvSubcategoryDesignBinding.bind(itemView);
        }
    }
}
