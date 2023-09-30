package com.example.pr17;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etPeople, etRegion, etAdditionalField;
    private Button btnAdd;
    private ListView listView;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etPeople = findViewById(R.id.etPeople);
        etRegion = findViewById(R.id.etRegion);
        etAdditionalField = findViewById(R.id.etAdditionalField);
        btnAdd = findViewById(R.id.btnAdd);
        listView = findViewById(R.id.listView);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnimal();
                loadAnimals();
            }
        });

        loadAnimals();
    }

    private void addAnimal() {
        String name = etName.getText().toString().trim();
        String population = etPeople.getText().toString().trim();
        String region = etRegion.getText().toString().trim();
        String hippieCount = etAdditionalField.getText().toString().trim();

        if (name.isEmpty() || population.isEmpty() || region.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("population", population);
        values.put("region", region);
        values.put("hippie_count", hippieCount);

        db.insert("mytable", null, values);

        etName.setText("");
        etPeople.setText("");
        etRegion.setText("");
        etAdditionalField.setText("");

        Toast.makeText(this, "Животное добавлено", Toast.LENGTH_SHORT).show();
    }

    private void loadAnimals() {
        List<String> animalsList = new ArrayList<>();
        Cursor cursor = db.query("mytable", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String population = cursor.getString(cursor.getColumnIndex("population"));
                String region = cursor.getString(cursor.getColumnIndex("region"));
                String hippieCount = cursor.getString(cursor.getColumnIndex("hippie_count"));

                animalsList.add(name + " (Популяция: " + population + " млн.), " +
                        region + ", Живут как хиппи: " + hippieCount);
            } while (cursor.moveToNext());
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, animalsList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
