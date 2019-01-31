package xyz.glucn.notebook;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

class NotesCursorAdapter extends CursorAdapter {

    NotesCursorAdapter(Context context) {
        super(context, null, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context)
                .inflate(R.layout.note_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String txt = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));

        txt = txt.replace('\n', ' ');

        TextView tv = view.findViewById(R.id.note_list_item_text);
        tv.setText(txt);
    }
}
