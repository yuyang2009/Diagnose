package com.diagnose.diagnose.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.diagnose.diagnose.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DiagnoseActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private Button startBtn, resetBtn, pauseBtn;
    private long stopTime = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath, mTmpFilePath;
    private List<String> tmpList = new ArrayList<>();

    public static final String EXTRA_PhotoPath = "PhotoPath";
    public static final String EXTRA_TmpFilePath = "TmpFilePath";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    Intent intent = new Intent(DiagnoseActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_diagnose:
//                    mTextMessage.setText(R.string.title_diagnose);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose);

        Intent intent = getIntent();
        chronometer = (Chronometer)findViewById(R.id.chronometer);
        startBtn = (Button)findViewById(R.id.startBtn);
        pauseBtn = (Button)findViewById(R.id.pauseBtn);
        resetBtn = (Button)findViewById(R.id.resetBtn);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_diagnose);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void saveBtnonClick(View view) {
        if(mCurrentPhotoPath == null) {
//            public void showEmptyTaskError() {
//                Snackbar.make(mTitle, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();
//            }
            CharSequence text = "Please take a photo first!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            return;
        }
        this.pauseBtn.callOnClick();
        try {
            mTmpFilePath = createTemperatureFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(DiagnoseActivity.this, AddDiagResActivity.class);
        intent.putExtra(EXTRA_PhotoPath, mCurrentPhotoPath);
        intent.putExtra(EXTRA_TmpFilePath, mTmpFilePath);
        startActivity(intent);
    }

    public void startBtnonClick(View view) {
        chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
        chronometer.start();
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time =  SystemClock.elapsedRealtime() - chronometer.getBase();
                tmpList.add(Long.toString(time / 1000));
            }
        });
        startBtn.setVisibility(View.INVISIBLE);
        pauseBtn.setVisibility(View.VISIBLE);
    }

    public void pauseBtnonClick(View view) {
        stopTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
        startBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.INVISIBLE);
    }

    public void resetBtnonClick(View view) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        stopTime = 0;
        chronometer.stop();
        startBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.INVISIBLE);
    }

    public void dispatchTAKEPictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }

            if(photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.diagnose.diagnose.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView mImageView =  (ImageView) findViewById(R.id.imageView);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mImageView.setImageURI(contentUri);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String createTemperatureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String tmpFileName = "tmp_" + timeStamp + ".dat";
        FileOutputStream fos = openFileOutput(tmpFileName, Context.MODE_PRIVATE);
        String tmpStr = TextUtils.join(", ", tmpList);
        fos.write(tmpStr.getBytes());
        fos.close();
        return getFileStreamPath(tmpFileName).getAbsolutePath();
    }
//    public void AnalysisBtnonClick(View view) {
//        Intent newDiagRestIntent = new Intent(this, NewDiagResActivity.class);
//        startActivityForResult(newDiagRestIntent, NEW_WORD_ACTIVITY_REQUEST_CODE);
//    }


}
