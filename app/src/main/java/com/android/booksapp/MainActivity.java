package com.android.booksapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editSearch;
    private Button buttonSearch;
    private RecyclerView recyclerView;
    private ArrayList<ItemData> values;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        values = new ArrayList<>();
        showRecView();
        buttonSearch.setOnClickListener(v -> searchBooks());
    }

    private void showRecView() {
        itemAdapter = new ItemAdapter(this, values);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);
    }

    private void initView() {
        editSearch = findViewById(R.id.edit_query);
        buttonSearch = findViewById(R.id.button_search);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void searchBooks(){

        String queryString = editSearch.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() && queryString.length() > 0){
            new FetchBook(this, values, itemAdapter, recyclerView)
                    .execute(queryString);
        }else {
            Toast.makeText(this, "Please check yout internet connection"
                    ,Toast.LENGTH_SHORT).show();
        }

    }
}