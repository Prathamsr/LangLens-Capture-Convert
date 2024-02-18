package com.example.langlens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.HashMap;

public class scan extends AppCompatActivity {
    private ImageView pic, clear, translate;
    private Spinner source;
    private TextView text;
    private Uri image_Uri;
    private TextRecognizer recognizer;
    private HashMap<String, String> map = new HashMap<String, String>();
    private String[] arr = {"English",
            "हिन्दी(Hindi)",
            "한국어(Korean)",
            "Dutch",
            "French",
            "German",
            "Italian",
            "Spanish",
            "Swedish",
            "Turkish"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        pic = findViewById(R.id.imageView2);
        clear = findViewById(R.id.imageView3);
        translate = findViewById(R.id.imageView4);
        source = findViewById(R.id.spinner);
        text = findViewById(R.id.editTextText);
        setLanguage();
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
            }
        });
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(scan.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = text.getText().toString();
                String lang = source.getSelectedItem().toString();
                if (s.equals("Scanned Text")) {
                    s = "";
                }
                Intent i = new Intent(scan.this, translate_text.class);
                i.putExtra("language", lang);
                i.putExtra("data", s);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            image_Uri = data.getData();
            Toast t = Toast.makeText(this, "Image Selected", Toast.LENGTH_LONG);
            t.setGravity(1, 0, 0);
            t.show();
            recongnizeText();

        } else {
            Toast t = Toast.makeText(this, "Image Not Selected", Toast.LENGTH_LONG);
            t.setGravity(1, 0, 0);
            t.show();
        }
    }

    private void recongnizeText() {
        if (image_Uri != null) {
            try {
                InputImage inputImage = InputImage.fromFilePath(scan.this, image_Uri);
                selectLanguage();
                Task<Text> result = recognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text text_genrated) {
                        String recgText = text_genrated.getText();
                        text.setText(recgText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast t = Toast.makeText(getApplicationContext(), "Select Correct Language", Toast.LENGTH_LONG);
                        t.setGravity(1, 0, 0);
                        t.show();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void selectLanguage() {
        String s = source.getSelectedItem().toString();
        if (map.get(s).equals("Latn")) {
            recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        } else if (map.get(s).equals("Deva")) {
            // When using Devanagari script library
            recognizer = TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());
        } else if (map.get(s).equals("Kore")) {
            // When using Korean script library
            recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());
        }
    }

    private void setLanguage() {
        map.put("English", "Latn");
        map.put("हिन्दी(Hindi)", "Deva");
        map.put("한국어(Korean)", "Kore");
        map.put("Dutch", "Latn");
        map.put("French", "Latn");
        map.put("German", "Latn");
        map.put("Italian", "Latn");
        map.put("Spanish", "Latn");
        map.put("Swedish", "Latn");
        map.put("Turkish", "Latn");
        ArrayAdapter adapter = new ArrayAdapter(scan.this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arr);
        source.setAdapter(adapter);
    }
}