package com.diagnose.diagnose.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.diagnose.diagnose.R;
import com.diagnose.diagnose.ViewModel.DiagResViewModel;
import com.diagnose.diagnose.db.entity.DiagResEntity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private TextView mTextMessage;
    public static final String EXTRA_DiagRes_ID = "DiagRes_ID";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_diagnose:
                    Button button = findViewById(R.id.button);
                    button.callOnClick();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            DiagResListFragment fragment = new DiagResListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, DiagResListFragment.TAG).commit();
        }
    }

    public void newDiagnose(View view) {
        Intent intent = new Intent(this, DiagnoseActivity.class);
        startActivity(intent);
    }

    public void show(DiagResEntity diagres) {
//        DiagResFragment diagResFragment = DiagResFragment.forDiagRes(diagres.getId());
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .addToBackStack("diagres")
//                .replace(R.id.fragment_container,
//                        diagResFragment, null).commit();

        Intent intent = new Intent(this, DiagResDetailActivity.class);
        intent.putExtra(EXTRA_DiagRes_ID, diagres.getId());
        startActivity(intent);
    }
}
