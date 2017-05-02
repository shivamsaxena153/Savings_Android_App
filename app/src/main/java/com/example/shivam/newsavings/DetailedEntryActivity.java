package com.example.shivam.newsavings;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class DetailedEntryActivity extends AppCompatActivity {

    SavingDBHelper dbHelper;
    ListView entryListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_entry);
        dbHelper = new SavingDBHelper(this);
        entryListView = (ListView)findViewById(R.id.entryListView);

        final Cursor cur = dbHelper.getAllEntry();
        String[] col = new String[]{SavingDBHelper.SAVING_COLUMN_ID,
                                    SavingDBHelper.SAVING_COLUMN_DATE,
                                    SavingDBHelper.SAVING_COLUMN_AMOUNT,
                                    SavingDBHelper.SAVING_COLUMN_DBTCR};

        int[] widgets = new int[]{R.id.idListView, R.id.dateListView,
                                    R.id.amtListView, R.id.dbtcrListView};

        SimpleCursorAdapter cursorAdapter =
                new SimpleCursorAdapter(this,R.layout.entry_layout,cur,col,widgets,0);
        entryListView.setAdapter(cursorAdapter);
    }
}
