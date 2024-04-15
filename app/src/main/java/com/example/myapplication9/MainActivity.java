package com.example.myapplication9;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_FILE_NAME = "file_name";
    private static final String KEY_FILE_CONTENT = "file_content";
    private EditText fileNameEditText, fileContentEditText;
    private Button createButton, appendButton, readButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileNameEditText = findViewById(R.id.fileNameEditText);
        fileContentEditText = findViewById(R.id.fileContentEditText);
        createButton = findViewById(R.id.createButton);
        appendButton = findViewById(R.id.appendButton);
        readButton = findViewById(R.id.readButton);
        deleteButton = findViewById(R.id.deleteButton);


        if (savedInstanceState != null) {
            fileNameEditText.setText(savedInstanceState.getString(KEY_FILE_NAME));
            fileContentEditText.setText(savedInstanceState.getString(KEY_FILE_CONTENT));
        }

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile();
            }
        });

        appendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToFile();
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFile();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });
    }

    private void createFile() {
        String fileName = fileNameEditText.getText().toString();
        String fileContent = fileContentEditText.getText().toString();
        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(fileContent.getBytes());
            fos.close();
            Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendToFile() {
        String fileName = fileNameEditText.getText().toString();
        String fileContent = fileContentEditText.getText().toString();
        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_APPEND);
            fos.write(fileContent.getBytes());
            fos.close();
            Toast.makeText(this, "Content appended to file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile() {
        String fileName = fileNameEditText.getText().toString();
        try {
            FileInputStream fis = openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            fileContentEditText.setText(stringBuilder.toString());
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete the file?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteFile();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private void deleteFile() {
        String fileName = fileNameEditText.getText().toString();
        File file = new File(getFilesDir(), fileName);
        if (file.exists()) {
            if (file.delete()) {
                Toast.makeText(this, "File deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Сохранение содержимого EditText в объект Bundle перед уничтожением активности
        outState.putString(KEY_FILE_NAME, fileNameEditText.getText().toString());
        outState.putString(KEY_FILE_CONTENT, fileContentEditText.getText().toString());
    }
}
