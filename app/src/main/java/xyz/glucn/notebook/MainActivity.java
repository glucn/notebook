package xyz.glucn.notebook;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = "MainActivity";
    private CursorAdapter mCursorAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // insertNote("New Note");

        String[] from = {DBOpenHelper.NOTE_TEXT};
        int[] to = {android.R.id.text1};

        mCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);
        LoaderManager.getInstance(this).initLoader(0, null, this);

        mRecyclerView = findViewById(R.id.note_recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerAdapter(this, mCursorAdapter);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_sample:
                insertSampleData();
                return true;
            case R.id.action_clear_data:
                clearData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void insertNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, note);
        Uri uri = getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        Log.i("MainActivity", "onCreate: data inserted " + uri.getLastPathSegment());
    }

    private void insertSampleData() {
        insertNote("Simple Note");
        insertNote("Multi-line\nnote");
        insertNote("Very very very very very very very very very very very long text");
        // restartLoader();
    }

    private void clearData() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    getContentResolver().delete(NotesProvider.CONTENT_URI, null, null);

                    Toast.makeText(MainActivity.this, "Delete all notes", Toast.LENGTH_LONG).show();
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton(android.R.string.yes, dialogClickListener)
                .setNegativeButton(android.R.string.no, dialogClickListener)
                .show();
    }

    private void restartLoader() {
        LoaderManager.getInstance(this).restartLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, NotesProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS,
                null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
