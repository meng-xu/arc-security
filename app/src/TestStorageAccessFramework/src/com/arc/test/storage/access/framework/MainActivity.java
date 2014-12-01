package com.arc.test.storage.access.framework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends Activity
{
    private static final int CREATE_REQUEST_CODE = 40;
    private static final int READ_REQUEST_CODE = 42;
    private static final int WRITE_REQUEST_CODE = 43;

    private TextView txt;
    
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        txt = (TextView) findViewById(R.id.textView1);
        final EditText edit = (EditText) findViewById(R.id.editText1);
        final Button cbtn = (Button) findViewById(R.id.button1);
	final Button lebtn = (Button) findViewById(R.id.button2);	
	final Button libtn = (Button) findViewById(R.id.button3);  
        final Button sbtn = (Button) findViewById(R.id.button4);  
        
        cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, "newfile.txt");
                
                startActivityForResult(intent, CREATE_REQUEST_CODE);
                
            }
        });    
  
        lebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });
 
        libtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt.setText(readTextFromUri(Uri.parse(edit.getText().toString())));
            }
        });      
 
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    writeTextToUri(edit.getText().toString());
                    txt.setText("Message saved");
                } catch (IOException ex) {
                    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
                    txt.setText(ex.getLocalizedMessage());
                }
            }
        });              
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == CREATE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            txt.setText("Created URI: "+resultData.getData().toString());
        }
        
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK){ 
            Uri uri = resultData.getData();
            txt.setText("Loaded URI: "+uri.toString());
            
            txt.append("\n"+"Actual URI: "+getFileUriFromContentUri(this.getApplicationContext(), uri));
            
            String value = readTextFromUri(uri);
            txt.append("\n"+value);
        }
    }
    
    private String readTextFromUri(Uri uri) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line+"\n");
            }
            inputStream.close();
            reader.close();
            return stringBuilder.toString();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "Unable to load";
    }

    private void writeTextToUri(String path) throws IOException {
        OutputStream outputStream = new FileOutputStream(path);
        outputStream.write("not-anymore".getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public static Uri getFileUriFromContentUri(Context context, Uri uri){
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow("_data");
            return Uri.parse(cursor.getString(column_index));
        } else{
            return null;
        }
    }
}
