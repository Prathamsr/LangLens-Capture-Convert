package com.example.langlens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity2 extends AppCompatActivity {
    private ConstraintLayout const1,const2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        const1=findViewById(R.id.cons1);
        const2=findViewById(R.id.cons2);
        const1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity2.this,scan.class);
                startActivity(i);
            }
        });
        const2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity2.this, translate_text.class);
                i.putExtra("language","English");
                i.putExtra("data","");
                startActivity(i);
            }
        });

    }
}