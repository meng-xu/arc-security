package com.arc.test.app.id;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView txt = (TextView) findViewById(R.id.textView1);
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            int[] gids = getPackageManager().getPackageGids(this.getPackageName());
            
            String gidString = "";
            for(int gid : gids){
                gidString = gidString+gid+" ";
            }
            
            txt.setText("UID: "+info.uid+"\n"+"GIDs: "+gidString);
            
        } catch (PackageManager.NameNotFoundException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
