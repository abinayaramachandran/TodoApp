package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button addBtn;
    EditText newItem;
    RecyclerView todoList;
    ItemsAdapter itemsAdapter;

    List<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.addBtn);
        newItem = findViewById(R.id.newItem);
        todoList = findViewById(R.id.todoItems);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void OnItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                saveItems();
                Toast.makeText(getApplicationContext() , "Item is removed" , Toast.LENGTH_SHORT).show();

            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        todoList.setAdapter(itemsAdapter);
        todoList.setLayoutManager(new LinearLayoutManager(this));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = newItem.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size() -1 );
                newItem.setText("");
                Toast.makeText(getApplicationContext() , "Item is added" , Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });


    }
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch(IOException e){
            Log.e("MainActivity", "Error reading Items", e);
            items = new ArrayList<>();
        }
    }
    private void saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e){
            Log.e("MainActivity" , "Error writing Items", e);
        }
    }
}