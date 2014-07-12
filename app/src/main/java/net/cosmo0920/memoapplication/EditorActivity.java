package net.cosmo0920.memoapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.cosmo0920.memoapplication.R;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class EditorActivity extends Activity {
    private static final int CREATE_DOCUMENT_REQUEST = 11;
    EditText mEditText;
    Uri mUri;
    final String TAG = "MemoApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mUri = intent.getData();

        prepareEditText(mUri);
        prepareSaveButton();
    }

    private void prepareEditText(Uri uri) {
        mEditText = (EditText)findViewById(R.id.edit_text);

        if (uri == null)
            return;
        InputStream is = null;
        StringBuilder sb = new StringBuilder();

        try {
            is = getContentResolver().openInputStream(mUri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            mEditText.setText(sb.toString());
            mEditText.setText(sb.toString());
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    private void prepareSaveButton() {
        Button button = (Button)findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUri == null) {
                    createNewFile();
                } else {
                    try {
                        String text = mEditText.getText().toString();
                        save(mUri, text);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, e.toString());
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        });
    }

    private void save(Uri uri, String text) throws FileNotFoundException, IOException {
        OutputStream os = null;
        try {
            os = getContentResolver().openOutputStream(uri);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(text);
            bw.flush();
            noticeSave();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    private void noticeSave() {
        Toast.makeText(getApplicationContext(), "The text has been saved",
                Toast.LENGTH_SHORT).show();
    }

    private void createNewFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "untitled.txt");
        startActivityForResult(intent, CREATE_DOCUMENT_REQUEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == CREATE_DOCUMENT_REQUEST) {
            if (resultCode != RESULT_OK)
                return;

            try {
                String text = mEditText.getText().toString();
                mUri = intent.getData();
                save(mUri, text);
            } catch (FileNotFoundException e) {
                Log.e(TAG, e.toString());
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }
}
