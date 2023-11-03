package com.kukuhAditya.newsApi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {
ListView Member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Daftar anggota kelompok 1
        String[] members = {
                "41521110012 - Septian Pramana R",
                "41519120044 - Reza Arrofi",
                "41520110069 - Malik Syah",
                "41521010118 - Kukuh Aditya",
                "41519120073 - Syauqii Fayyadh Hilal Z",
                "41521120048 - Ricky Miftahuddin"

        };

        // Inisialisasi ListView
        ListView listView = findViewById(R.id.listViewMembers);

        // Menggunakan ArrayAdapter untuk mengisi ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, members);

        // Menghubungkan adapter dengan ListView
        listView.setAdapter(adapter);
    }
}