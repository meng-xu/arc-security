package com.arc.test.permission;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final WebView web = (WebView) findViewById(R.id.webview);
        
        web.loadUrl("https://www.google.com");
        
    }
}
