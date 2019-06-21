package com.mityukovalexander.helloworld;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {

    private EditText titleEdit;
    private EditText priceEdit;
    private Button addButton;

    private String title;
    private String price;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        titleEdit = findViewById(R.id.titleText);
        priceEdit = findViewById(R.id.priceText);
        addButton = findViewById(R.id.addButton);

        titleEdit.addTextChangedListener(new TextWatcher() {
            // Удалил пустые строки, чтобы просто так место не занимать TODO remove this comment
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                 title = s.toString();
                 changeTextColorButton();
            }
        });

        priceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                price = s.toString();
                changeTextColorButton();
            }
        });


    }

    private void changeTextColorButton(){
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(price)) {
            addButton.setTextColor(ContextCompat.getColor(this, R.color.addButtonTextColor));
        } else {
            addButton.setTextColor(ContextCompat.getColor(this, R.color.addButtonColorInactive));
        }
    }
}
