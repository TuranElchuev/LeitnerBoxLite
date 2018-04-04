package com.gmail.at.telchuev.leitnerboxlite;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditActivity extends Activity {

    public static final String INTENT_KEY_ETRY = "entry";

    private EditText et_word,
            et_hint,
            et_example,
            et_example_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        et_word = (EditText)findViewById(R.id.et_word);
        et_hint = (EditText)findViewById(R.id.et_hint);
        et_example = (EditText)findViewById(R.id.et_example);
        et_example_hint = (EditText)findViewById(R.id.et_example_hint);

        ((Button)findViewById(R.id.btn_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry();
            }
        });
    }

    private void saveEntry(){
        Entry e = new Entry();
        e.setWord(et_word.getText().toString());
        e.setHint(et_hint.getText().toString());
        e.setExample(et_example.getText().toString());
        e.setExampleHint(et_example_hint.getText().toString());
        e.toDB();
        Toast.makeText(this, R.string.entry_added, Toast.LENGTH_SHORT).show();
        this.finish();
    }

}
