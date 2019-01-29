package xyz.glucn.notebook;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<Cursor>
{
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
        mRecyclerView = findViewById(R.id.note_recycle_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyRecyclerAdapter(this, mCursorAdapter);
        mRecyclerView.setAdapter(mAdapter);

        LoaderManager.getInstance(this).initLoader(0, null, this);

    }

    private void insertNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, note);
        Uri uri = getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        Log.i("MainActivity", "onCreate: data inserted " + uri.getLastPathSegment());
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
