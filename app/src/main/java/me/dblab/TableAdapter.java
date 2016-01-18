package me.dblab;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import me.dblab.common.Table;
import me.dblab.exceptions.ColumnNotExistsException;
import me.dblab.exceptions.RowNotExistsException;
import me.dblab.exceptions.TableNotExistsException;

public class TableAdapter extends BaseAdapter {
    private final Context mContext;
    private Table mTable = null;

    public TableAdapter(Context context, String tableName) {
        mContext = context;
        try {
            mTable = DatabaseHolder.getDatabase().getTable(tableName);
        } catch (TableNotExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return mTable.getRowIds().length;
    }

    public int getColumnsCount() {
        return mTable.getColumnNames().length;
    }

    @Override
    public Object getItem(int position) {
        int row = position / getColumnsCount();
        int col = position % getColumnsCount();
        try {
            return mTable.getRow(mTable.getRowIds()[row]).getValue(mTable.getColumnNames()[col]);
        } catch (ColumnNotExistsException | RowNotExistsException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(mContext);
        view.setText(String.valueOf(getItem(position)));
        return view;
    }
}
