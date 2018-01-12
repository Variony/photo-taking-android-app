package chuyiheng.p4.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;

import java.util.concurrent.LinkedBlockingDeque;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import chuyiheng.p4.MyTask;
import chuyiheng.p4.R;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Button mImportButton;
    private Spinner mSpinner;
    private Realm mRealm;
    public static ArrayList<String> mPaths;
    public static int count;
    public static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPaths=new ArrayList<>();
        for(File file:new File(Environment.getExternalStorageDirectory(),"DCIM/Camera").listFiles()){
            mPaths.add(file.toString());
        }

        mRealm=Realm.getDefaultInstance();

        if(mRealm.isEmpty()) {

            mImportButton = (Button) findViewById(R.id.importButton);
            mSpinner=(Spinner)findViewById(R.id.spinner);
            ArrayList<Integer> list=new ArrayList<>();
            list.addAll(Arrays.asList(1,2,4,8));
            ArrayAdapter<Integer> adapter=new ArrayAdapter<Integer>(MainActivity.this,android.R.layout.simple_spinner_item, list);
            mSpinner.setAdapter(adapter);

            mImportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    count=0;

                    mProgressDialog=new ProgressDialog(MainActivity.this);
                    mProgressDialog.setTitle("Import Progress");
                    mProgressDialog.setMax(mPaths.size());
                    mProgressDialog.setProgress(0);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.show();


                    int numberOfThreads=Integer.parseInt(mSpinner.getSelectedItem().toString());
                    Executor executor=new ThreadPoolExecutor(numberOfThreads,8,10, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(10));

                    for(int i=0; i<mPaths.size();i++){
                        new MyTask(MainActivity.this).executeOnExecutor(executor,mPaths.get(i));
                    }

                }
            });
    }
        else{
            Intent intent=new Intent(MainActivity.this,GalleryActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
