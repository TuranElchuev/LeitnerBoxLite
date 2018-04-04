package com.gmail.at.telchuev.leitnerboxlite;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class FragmentMain extends Fragment implements View.OnClickListener{

    private TextView tv_word, tv_hint, tv_example, tv_example_hint, tv_box;

    private ArrayList<Entry> data;
    private int index = -1;

    private boolean hint = false;

    public FragmentMain() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);

        initializeData();
        findNextEntryIndex();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        ((Button)v.findViewById(R.id.btn_add)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btn_repeat)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btn_skip)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btn_hint)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btn_know)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btn_vocabulary)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btn_box)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btn_edit)).setOnClickListener(this);

        tv_word = (TextView)v.findViewById(R.id.tv_word);
        tv_hint = (TextView)v.findViewById(R.id.tv_hint);
        tv_example = (TextView)v.findViewById(R.id.tv_example);
        tv_example_hint = (TextView)v.findViewById(R.id.tv_example_hint);
        tv_box = (TextView)v.findViewById(R.id.tv_box);

        this.setDataToView();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                Intent intent = new Intent(getContext(), AddEditActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_edit:
                if(indexValid()){

                }
                break;
            case R.id.btn_vocabulary:
                break;
            case R.id.btn_box:
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
                    data.get(index).skip();
                    switchEntry();
                }
                break;
            case R.id.btn_repeat:
                if((indexValid())) {
                    data.get(index).repeat();
                    switchEntry();
                }
                break;
            case R.id.btn_know:
                if((indexValid())) {
                    data.get(index).know();
                    switchEntry();
                }
                break;
            default:
                break;
        }
    }

    private void initializeData(){
        data = Utility.getLowestBoxVocabulary();
    }

    private void findNextEntryIndex(){
        if(!dataExists()){
            index = -1;
            return;
        }

        Random rnd = new Random();
        index = rnd.nextInt(data.size());
    }

    private void setDataToView(){
        tv_word.setText("");
        tv_hint.setText("");
        tv_example.setText("");
        tv_example_hint.setText("");
        tv_box.setText("");

        hideHint();

        if(!indexValid()){
            return;
        }

        Entry e = data.get(index);
        tv_word.setText(e.getWord());
        tv_hint.setText(e.getHint());
        tv_example.setText(e.getExample());
        tv_example_hint.setText(e.getExampleHint());
        tv_box.setText(getString(R.string.box) + ": " + e.getBoxNumber());
    }

    private boolean indexValid(){
        return dataExists()
                && index > -1
                && index < data.size();
    }

    private boolean dataExists(){
        return data != null && !data.isEmpty();
    }

    private void removeEntry(){
        if(indexValid()){
            data.remove(index);
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
