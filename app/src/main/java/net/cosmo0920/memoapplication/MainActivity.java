package net.cosmo0920.memoapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    private static final int OPEN_DOCUMENT_REQUEST = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareNewButton();
        prepareOpenButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void prepareNewButton() {
        Button button = (Button)findViewById(R.id.new_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchEditorActivity(null);
            }
        });
    }

    private void prepareOpenButton() {
        Button button = (Button)findViewById(R.id.open_button);
        button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               openDocument();
           }
        });
    }

    private void launchEditorActivity(Uri uri) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }

    private void openDocument() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/plain");
        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == OPEN_DOCUMENT_REQUEST) {
            if (resultCode != RESULT_OK)
                return;

            Uri uri = intent.getData();
            launchEditorActivity(uri);
        }
    }
}
