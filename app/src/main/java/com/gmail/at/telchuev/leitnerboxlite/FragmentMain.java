package com.gmail.at.telchuev.leitnerboxlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class FragmentMain extends Fragment implements View.OnClickListener{

    private TextView tv_word, tv_hint, tv_example, tv_example_hint;

    private Spinner spinner_box;

    private ArrayList<Entry> vocabData;
    private int index = -1;

    private boolean hint = false;

    private String selectedBox;

    public FragmentMain() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        setupSpinner(v);

        ((ImageButton)v.findViewById(R.id.btn_add)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btn_repeat)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btn_skip)).setOnClickListener(this);
        ((ImageButton)v.findViewById(R.id.btn_hint)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btn_know)).setOnClickListener(this);
        ((ImageButton)v.findViewById(R.id.btn_edit)).setOnClickListener(this);

        tv_word = (TextView)v.findViewById(R.id.tv_word);
        tv_hint = (TextView)v.findViewById(R.id.tv_hint);
        tv_example = (TextView)v.findViewById(R.id.tv_example);
        tv_example_hint = (TextView)v.findViewById(R.id.tv_example_hint);

        this.setDataToView();
        return v;
    }

    private void setupSpinner(View v){
        spinner_box = (Spinner)v.findViewById(R.id.spinner_box);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Utility.getNonEmptyBoxes());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_box.setAdapter(adapter);

        spinner_box.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String box = parent.getItemAtPosition(position).toString();
                if(!box.equals(selectedBox)) {
                    selectedBox = box;
                    initializeData();
                    switchEntry();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case AddEditActivity.INTENT_ADD:
                if(resultCode == Activity.RESULT_OK){
                    initializeData();
                    switchEntry();
                }
                break;
            case AddEditActivity.INTENT_EDIT:
                if(resultCode == Activity.RESULT_OK){
                    vocabData.get(index).refresh();
                    setDataToView();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                Intent intentAdd = new Intent(getContext(), AddEditActivity.class);
                intentAdd.setAction(AddEditActivity.ACTION_ADD);
                startActivityForResult(intentAdd, AddEditActivity.INTENT_ADD);
                break;
            case R.id.btn_edit:
                if(indexValid()){
                    Intent intentEdit = new Intent(getContext(), AddEditActivity.class);
                    intentEdit.setAction(AddEditActivity.ACTION_EDIT);
                    intentEdit.putExtra(AddEditActivity.INTENT_KEY_ENTRY_ID, vocabData.get(index).getId());
                    startActivityForResult(intentEdit, AddEditActivity.INTENT_EDIT);
                }
                break;
            case R.id.btn_hint:
                if(hint){
                    hideHint();
                }else{
                    showHint();
                }
                break;
            case R.id.btn_skip:
                if((indexValid())) {
                    vocabData.get(index).skip();
                    switchEntry();
                }
                break;
            case R.id.btn_repeat:
                if((indexValid())) {
                    vocabData.get(index).repeat();
                    switchEntry();
                }
                break;
            case R.id.btn_know:
                if((indexValid())) {
                    vocabData.get(index).know();
                    switchEntry();
                }
                break;
            default:
                break;
        }
    }

    private void initializeData(){
        vocabData = Utility.getBoxVocabulary(selectedBox);
    }

    private void findNextEntryIndex(){
        if(!dataExists()){ // might run out of Entries in current box
            initializeData();
            if(!dataExists()) {
                Toast.makeText(getContext(), R.string.no_entries, Toast.LENGTH_SHORT).show();
                index = -1;
                return;
            }
        }

        Random rnd = new Random();
        index = rnd.nextInt(vocabData.size());
    }

    private void setDataToView(){
        tv_word.setText("");
        tv_hint.setText("");
        tv_example.setText("");
        tv_example_hint.setText("");

        hideHint();

        if(!indexValid()){
            return;
        }

        Entry e = vocabData.get(index);
        tv_word.setText(e.getWord());
        tv_hint.setText(e.getHint());
        tv_example.setText(e.getExample());
        tv_example_hint.setText(e.getExampleHint());
    }

    private boolean indexValid(){
        return dataExists()
                && index > -1
                && index < vocabData.size();
    }

    private boolean dataExists(){
        return vocabData != null && !vocabData.isEmpty();
    }

    private void removeEntry(){
        if(indexValid()){
            vocabData.remove(index);
        }
    }

    private void switchEntry(){
        removeEntry();
        findNextEntryIndex();
        setDataToView();
    }

    private void showHint(){
        hint = true;
        tv_hint.setVisibility(View.VISIBLE);
        tv_example_hint.setVisibility(View.VISIBLE);
    }

    private void hideHint(){
        hint = false;
        tv_hint.setVisibility(View.INVISIBLE);
        tv_example_hint.setVisibility(View.INVISIBLE);
    }

}
