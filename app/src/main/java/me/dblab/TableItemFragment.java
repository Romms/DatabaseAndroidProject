package me.dblab;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.dblab.dblab.R;

import me.dblab.common.Database;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TableItemFragment extends android.app.Fragment {
    private static final String ARG_TABLE_NAMES = "me.dblab.table-names";

    private OnListFragmentInteractionListener mListener;
    private String[] mTableNames;
    private static String[] listOfTableNames;

    public TableItemFragment() {
    }

    @SuppressWarnings("unused")
    public static TableItemFragment newInstance() {
        TableItemFragment fragment = new TableItemFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_TABLE_NAMES, getListOfTableNames());
        fragment.setArguments(args);
        return fragment;
    }

    public static String[] getListOfTableNames() {
        Database database = DatabaseHolder.getDatabase();
        return database != null ? database.getTableNames() : new String[0];
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTableNames = getArguments().getStringArray(ARG_TABLE_NAMES);
        } else {
            mTableNames = new String[0];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.table_list, container, false);

        Context context = view.getContext();
        ListView listView = (ListView) view.findViewById(R.id.table_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, mTableNames);
        listView.setAdapter(adapter);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tableName = ((TextView) view).getText().toString();
                mListener.onListFragmentInteraction(tableName);
            }
        };

        listView.setOnItemClickListener(listener);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(String item);
    }
}
