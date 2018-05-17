package com.gmail.at.telchuev.leitnerboxlite;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditActivity extends Activity {

    public static final int INTENT_ADD = 9000;
    public static final int INTENT_EDIT = 9001;
    public static final String ACTION_ADD = "9000";
    public static final String ACTION_EDIT = "9001";
    public static final String INTENT_KEY_ENTRY_ID = "entry_id";

    private EditText et_word,
            et_hint,
            et_example,
            et_example_hint;

    private Entry entry_;

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


        entry_ = new Entry();
        switch (getIntent().getAction()){
            case ACTION_ADD:
                break;
            case ACTION_EDIT:
                entry_.init(getIntent().getIntExtra(INTENT_KEY_ENTRY_ID, -1));

                et_word.setText(entry_.getWord());
                et_hint.setText(entry_.getHint());
                et_example.setText(entry_.getExample());
                et_example_hint.setText(entry_.getExampleHint());
                break;
            default:
                break;
        }
    }

    private void saveEntry(){
        entry_.setWord(et_word.getText().toString().trim());
        entry_.setHint(et_hint.getText().toString().trim());
        entry_.setExample(et_example.getText().toString().trim());
        entry_.setExampleHint(et_example_hint.getText().toString().trim());
        if(entry_.isEmpty()){
            Toast.makeText(this, R.string.entry_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        entry_.toDB();
        Toast.makeText(this, R.string.entry_saved, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        this.finish();
    }

}
