package com.gmail.at.telchuev.leitnerboxlite;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

    private final String FR_MAIN_TAG = "fr_main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getFragmentManager().findFragmentByTag(FR_MAIN_TAG) == null) {
            getFragmentManager().beginTransaction().add(R.id.frame_main, new FragmentMain(), FR_MAIN_TAG).commit();
        }
        Utility.importFile();
    }

}
