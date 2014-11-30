package com.arc.test.storage.external;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends Activity
{
    
    static final String FILENAME="TestStorageExternal.txt";
    static final String SECRET="my-external";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final TextView txt = (TextView) findViewById(R.id.textView1);
        final Button tbtn = (Button) findViewById(R.id.button0);
	final Button sbtn = (Button) findViewById(R.id.button1);	
	final Button lbtn = (Button) findViewById(R.id.button2);  
        
        tbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt.setText("Readable: "+isExternalStorageReadable()+", Writable: "+isExternalStorageWritable());
            }
        });
        
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = new File(Environment.getExternalStorageDirectory(), FILENAME);

                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(SECRET.getBytes());
                    fos.close();
                    
                    txt.setText("Secret: \""+SECRET+"\" saved to "+file.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
        
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    
                    File file = new File(Environment.getExternalStorageDirectory(), FILENAME);
                    
                    FileInputStream fis = new FileInputStream(file);

                    BufferedReader r = new BufferedReader(new InputStreamReader(fis));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line);
                    }

                    txt.setText("Secret: \""+total+"\" loaded from "+file.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
