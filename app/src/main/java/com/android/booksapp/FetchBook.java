package com.android.booksapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchBook extends AsyncTask<String, Void, String> {

    private ArrayList<ItemData> values;
    private ItemAdapter itemAdapter;
    @SuppressLint("StaticFieldLeak")
    private final RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    Context context;

    public FetchBook(Context context, ArrayList<ItemData> values,
                     ItemAdapter itemAdapter, RecyclerView recyclerView){
        this.context = context;
        this.values = values;
        this.itemAdapter = itemAdapter;
        this.recyclerView = recyclerView;
    }


    @Override
    protected String doInBackground(String... strings) {

        String queryString = strings[0];

        HttpURLConnection urlConnection;
        BufferedReader reader;
        String bookJSONString = null;
        String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
        String QUERY_PARAM = "q";

        Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, queryString).build();

        try {

            URL requesURL = new URL(builtURI.toString());
            urlConnection = (HttpURLConnection) requesURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line).append("\n");
            }

            if (builder.length() == 0){
                return null;
            }
            bookJSONString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookJSONString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        values = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemArray = jsonObject.getJSONArray("items");
            String title;
            String author;
            String image;
            String desc;

            int i = 0;
            while (i < itemArray.length()){
                JSONObject book = itemArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                try {
                    title = volumeInfo.getString("title");

                    if (volumeInfo.has("authors")) {
                        author = volumeInfo.getString("authors");
                    } else {
                        author = "";
                    }
                    if (volumeInfo.has("description")) {
                        desc = volumeInfo.getString("description");
                    } else {
                        desc = "";
                    }
                    if (volumeInfo.has("imageLinks")) {
                        image = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                    } else {
                        image = "";
                    }

                    ItemData itemData = new ItemData();
                    itemData.itemTitle = title;
                    itemData.itemAuthor = author;
                    itemData.itemDescription = desc;
                    itemData.itemImage = image;

                    values.add(itemData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.itemAdapter = new ItemAdapter(context,values);
        this.recyclerView.setAdapter(this.itemAdapter);
    }
}
