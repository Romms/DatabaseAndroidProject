package me.dblab;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.dblab.dblab.R;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements TableItemFragment.OnListFragmentInteractionListener, DatabaseLoaderListener, ChooseTablesDialogFragment.ChooseTablesDialogListener {
    public static final String EXTRA_TABLE_NAME = "org.dblab.TABLE_NAME";

    //private final static String baseUri = "http://192.168.0.2:8080/db?action=";
    private final static String baseUri = "https://infinite-lowlands-4845.herokuapp.com/db?action=";
    private final static String getDatabaseUri = baseUri + "getDatabase";
    private final static String intersectTablesUri = baseUri + "intersectTables";
    private final static String productTablesUri = baseUri + "productTables";

    DatabaseLoader databaseLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Create new table is not implemented", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TableItemFragment fragment = TableItemFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main_container, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            makeRefresh();
            return true;
        } else if (id == R.id.action_product) {
            makeProduct();
        } else if (id == R.id.action_intersect) {
            makeIntersection();
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeProduct() {
        DialogFragment newFragment = new ChooseTablesDialogFragment();
        Bundle args = new Bundle();
        args.putString("uri", productTablesUri);
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "choose-tables");
    }

    private void makeIntersection() {
        DialogFragment newFragment = new ChooseTablesDialogFragment();
        Bundle args = new Bundle();
        args.putString("uri", intersectTablesUri);
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "choose-tables");
    }

    private void makeRefresh() {
        if (databaseLoader != null) {
            databaseLoader.stop();
            databaseLoader = null;
        }

        try {
            databaseLoader = new DatabaseLoader(this);
            databaseLoader.execute(new URL(getDatabaseUri));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void onDatabaseLoaded() {
        databaseLoader = null;

        TableItemFragment fragment = TableItemFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(String tableName) {
        Log.i("MAKSYM", tableName);

        Intent intent = new Intent(this, TableViewActivity.class);
        intent.putExtra(EXTRA_TABLE_NAME, tableName);
        startActivity(intent);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String table1, String table2, String table3) {
        String uri = dialog.getArguments().getString("uri");
        uri = uri + "&table1=" + table1;
        uri = uri + "&table2=" + table2;
        uri = uri + "&table3=" + table3;

        if (databaseLoader != null) {
            databaseLoader.stop();
            databaseLoader = null;
        }

        try {
            databaseLoader = new DatabaseLoader(this);
            databaseLoader.execute(new URL(uri));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }
}
