package com.gmail.at.telchuev.leitnerboxlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentMain extends Fragment implements View.OnClickListener{

    private Spinner spinner_box;

    private ArrayList<Entry> vocabData;

    private String selectedBox;

    private ViewPager pager;

    private CustomViewPagerAdapter pagerAdapter;
    private ArrayAdapter<String> spinnerAdapter;

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

        spinner_box = (Spinner)v.findViewById(R.id.spinner_box);
        setupSpinner();

        pager = (ViewPager)v.findViewById(R.id.view_pager);
        setupPager();

        ((ImageButton)v.findViewById(R.id.btn_add)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btn_repeat)).setOnClickListener(this);
        ((ImageButton)v.findViewById(R.id.btn_hint)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btn_know)).setOnClickListener(this);
        ((ImageButton)v.findViewById(R.id.btn_edit)).setOnClickListener(this);

        return v;
    }

    private void setupPager(){
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position > 0){
                    vocabData.get(position - 1).setShowHint(false);
                }
                if(position < vocabData.size() - 1){
                    vocabData.get(position + 1).setShowHint(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(pagerAdapter != null){
            pager.setAdapter(pagerAdapter);
        }
    }

    private void setupSpinner(){

        if(spinnerAdapter == null) {
            spinnerAdapter = new ArrayAdapter<String>(MyApp.getAppContext(), R.layout.spinner_item, Utility.getSpinnerData());
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        }

        spinner_box.setAdapter(spinnerAdapter);

        spinner_box.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String box = parent.getItemAtPosition(position).toString();
                if(!box.equals(selectedBox)){
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

    private void updatePagerAdapter(boolean keepPage){
        int currentPage = pager.getCurrentItem();
        pagerAdapter = new CustomViewPagerAdapter();
        pager.setAdapter(pagerAdapter);
        if(keepPage && currentPage < pagerAdapter.getCount()){
            pager.setCurrentItem(currentPage, false);
        }
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
                    vocabData.get(pager.getCurrentItem()).refresh();
                    updatePagerAdapter(true);
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
                Intent intentAdd = new Intent(MyApp.getAppContext(), AddEditActivity.class);
                intentAdd.setAction(AddEditActivity.ACTION_ADD);
                startActivityForResult(intentAdd, AddEditActivity.INTENT_ADD);
                break;
            case R.id.btn_edit:
                if(entryValid()){
                    Intent intentEdit = new Intent(MyApp.getAppContext(), AddEditActivity.class);
                    intentEdit.setAction(AddEditActivity.ACTION_EDIT);
                    intentEdit.putExtra(AddEditActivity.INTENT_KEY_ENTRY_ID, vocabData.get(pager.getCurrentItem()).getId());
                    startActivityForResult(intentEdit, AddEditActivity.INTENT_EDIT);
                }
                break;
            case R.id.btn_hint:
                hint();
                break;
            case R.id.btn_repeat:
                if((entryValid())) {
                    vocabData.get(pager.getCurrentItem()).repeat();
                    removeCurrentEntry();
                    switchEntry();
                }
                break;
            case R.id.btn_know:
                if((entryValid())) {
                    vocabData.get(pager.getCurrentItem()).know();
                    removeCurrentEntry();
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

    private boolean dataExists(){
        return vocabData != null && !vocabData.isEmpty();
    }

    private void hint(){
        if(entryValid()){
            Entry e = vocabData.get(pager.getCurrentItem());
            e.setShowHint(!e.isShowHint());
            updatePagerAdapter(true);
        }
    }

    private void removeCurrentEntry(){
        if(entryValid()){
            vocabData.remove(pager.getCurrentItem());
        }
    }

    private boolean entryValid(){
        return pager.getAdapter() != null
                && pager.getAdapter().getCount() > 0
                && vocabData != null
                && vocabData.size() == pager.getAdapter().getCount();
    }

    private void switchEntry(){
        Collections.shuffle(vocabData);
        updatePagerAdapter(false);
    }

    private class CustomViewPagerAdapter extends PagerAdapter{

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ViewGroup view = (ViewGroup)LayoutInflater.from(MyApp.getAppContext()).inflate(R.layout.page_view_item, container, false);

            container.addView(view);

            TextView tv_word = (TextView)view.findViewById(R.id.tv_word);
            TextView tv_hint = (TextView)view.findViewById(R.id.tv_hint);
            TextView tv_example = (TextView)view.findViewById(R.id.tv_example);
            TextView tv_example_hint = (TextView)view.findViewById(R.id.tv_example_hint);

            Entry e = vocabData.get(position);
            tv_word.setText(e.getWord());
            tv_hint.setText(e.getHint());
            tv_example.setText(e.getExample());
            tv_example_hint.setText(e.getExampleHint());

            if(e.isShowHint()){
                tv_hint.setVisibility(View.VISIBLE);
                tv_example_hint.setVisibility(View.VISIBLE);
            }

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return dataExists() ? vocabData.size() : 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

}
