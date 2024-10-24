package com.example.onlinequizadminapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlinequizadminapp.Adapters.CategoryAdapter;
import com.example.onlinequizadminapp.Adapters.QuestionAdapter;
import com.example.onlinequizadminapp.Models.CategoryModel;
import com.example.onlinequizadminapp.Models.QuestionModel;
import com.example.onlinequizadminapp.databinding.ActivityMainBinding;
import com.example.onlinequizadminapp.databinding.ActivityQuestionsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    ActivityQuestionsBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    QuestionAdapter adapter;
    ArrayList<QuestionModel> list;

    Dialog loadingDialog;
    private  String catId, subCatId;

    // CREATE CATEGORY -----------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        binding = ActivityQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        catId = getIntent().getStringExtra("catId");
        subCatId = getIntent().getStringExtra("subCatId");
        database = FirebaseDatabase.getInstance();

        list = new ArrayList<>();

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(true);
        loadingDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvQuestions.setLayoutManager(layoutManager);

        adapter = new QuestionAdapter(this, list, catId, subCatId);
        binding.rvQuestions.setAdapter(adapter);

        database.getReference().child("categories").child(catId).child("subCategories").child(subCatId)
                .child("questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.exists()){
                    list.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        QuestionModel model = dataSnapshot.getValue(QuestionModel.class);
                        model.setKey(dataSnapshot.getKey());
                        list.add(model);

                    }

                    adapter.notifyDataSetChanged();
                    loadingDialog.dismiss();
                }
                else {
                    loadingDialog.dismiss();
                    Toast.makeText(QuestionsActivity.this, "catefory not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.dismiss();
                Toast.makeText(QuestionsActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        binding.uploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, UploadQuestionsActivity.class);
                intent.putExtra("catId", catId);
                intent.putExtra("subCatId", subCatId);
                startActivity(intent);
            }
        });

    }

    // END CREATE CATEGORY----------------------------------------------------------------------
}