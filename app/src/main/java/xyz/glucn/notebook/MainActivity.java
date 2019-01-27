package xyz.glucn.notebook;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertNote("New Note");
    }

    private void insertNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, note);
        Uri uri = getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        Log.i("MainActivity", "onCreate: data inserted " + uri.getLastPathSegment());
    }
}
