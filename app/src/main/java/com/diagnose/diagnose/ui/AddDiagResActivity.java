package com.diagnose.diagnose.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.diagnose.diagnose.R;
import com.diagnose.diagnose.db.entity.DiagResEntity;

public class AddDiagResActivity extends AppCompatActivity {

    public String tmpFileExtra, photoPathExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddiagres);

        DiagResAddFragment diagResAddFragment = (DiagResAddFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        Intent intent = getIntent();
        tmpFileExtra = intent.getStringExtra(DiagnoseActivity.EXTRA_TmpFilePath);
        photoPathExtra = intent.getStringExtra(DiagnoseActivity.EXTRA_PhotoPath);

        if (diagResAddFragment == null) {
            diagResAddFragment = DiagResAddFragment.newInstance(tmpFileExtra, photoPathExtra);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, diagResAddFragment, null).commit();

        }
    }
}
