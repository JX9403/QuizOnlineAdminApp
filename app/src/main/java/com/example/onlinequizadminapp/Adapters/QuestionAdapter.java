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

import com.example.onlinequizadminapp.Models.QuestionModel;
import com.example.onlinequizadminapp.Models.SubCategoryModel;
import com.example.onlinequizadminapp.QuestionsActivity;
import com.example.onlinequizadminapp.R;
import com.example.onlinequizadminapp.databinding.RvSubcategoryDesignBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.viewHolder>{

    Context context;
    ArrayList<QuestionModel> list;

    private  String catId;
    private  String subCatId;


    public QuestionAdapter(Context context, ArrayList<QuestionModel> list, String catId, String subCatId) {
        this.context = context;
        this.list = list;
        this.catId = catId;
        this.subCatId = subCatId;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_subcategory_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        QuestionModel subCategoryModel = list.get(position);
        holder.binding.subCategoryName.setText(subCategoryModel.getQuestion());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this category?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseDatabase.getInstance().getReference().child("categories").child(catId).child("subCategories").child(subCatId)
                            .child("questions").child(subCategoryModel.getKey())
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
