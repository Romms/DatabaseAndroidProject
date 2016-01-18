package me.dblab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.dblab.dblab.R;

import me.dblab.common.Table;
import me.dblab.exceptions.ColumnNotExistsException;
import me.dblab.exceptions.RowNotExistsException;
import me.dblab.exceptions.TableNotExistsException;

public class TableViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String tableName = intent.getStringExtra(MainActivity.EXTRA_TABLE_NAME);

        setTitle(tableName);

        TableLayout layout = (TableLayout) findViewById(R.id.table_grid);
        try {
            Table table = DatabaseHolder.getDatabase().getTable(tableName);
            String[] rowIds = table.getRowIds();
            String[] columns = table.getColumnNames();
            for (String rowId : rowIds) {
                TableRow row = new TableRow(this);
                for (String column : columns) {
                    TextView textView = new TextView(this);
                    textView.setText(table.getRow(rowId).getValue(column).toString());
                    final int padding = 5;
                    textView.setPadding(padding, padding, padding, padding);
                    row.addView(textView);
                }
                layout.addView(row);
            }
        } catch (TableNotExistsException | ColumnNotExistsException | RowNotExistsException e) {
            e.printStackTrace();
        }
    }
}
