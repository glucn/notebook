package xyz.glucn.notebook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {

    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        editor = findViewById(R.id.editText);
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);
        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(R.string.new_note);
        } else {
            action = Intent.ACTION_EDIT;
            setTitle(R.string.edit_note);

            noteFilter = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();
            Cursor cursor = getContentResolver().query(uri, DBOpenHelper.ALL_COLUMNS, noteFilter, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                oldText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
                cursor.close();
            }
            editor.setText(oldText);
            editor.requestFocus();
        }
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete_note:
                deleteNote();
                break;
        }
        return true;
    }

    private void finishEditing() {
        String text = editor.getText().toString().trim();
        switch (action) {
            case Intent.ACTION_INSERT:
                if (text.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    insertNote(text);
                }
                break;
            case Intent.ACTION_EDIT:
                if (text.length() == 0) {
                    deleteNote();
                } else if (oldText.equals(text)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateNote(text);
                }
                break;
        }

        finish();
    }

    private void deleteNote() {
        getContentResolver().delete(NotesProvider.CONTENT_URI, noteFilter, null);
        Toast.makeText(this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateNote(String text) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, text);
        getContentResolver().update(NotesProvider.CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String text) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, text);
        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        Toast.makeText(this, getString(R.string.note_created), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

}
