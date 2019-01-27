package xyz.glucn.notebook;

import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBOpenHelper dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
    }
}
