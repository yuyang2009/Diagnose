package com.diagnose.diagnose.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.diagnose.diagnose.R;

public class DiagResDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagres_detail);

        Intent intent = getIntent();

        int diagresID = intent.getIntExtra(MainActivity.EXTRA_DiagRes_ID, 0);


        if (savedInstanceState == null) {
            DiagResFragment fragment = DiagResFragment.forDiagRes(diagresID);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, fragment, null).commit();
        }
    }
}
