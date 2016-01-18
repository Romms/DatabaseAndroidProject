package me.dblab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dblab.dblab.R;

public class ChooseTablesDialogFragment extends DialogFragment {
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ChooseTablesDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String table1, String table2, String table3);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    ChooseTablesDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the ChooseTablesDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ChooseTablesDialogListener so we can send events to the host
            mListener = (ChooseTablesDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ChooseTablesDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.choose_tables_fragment, null);
        builder.setView(view)
                .setMessage(R.string.dialog_choose_3_tables)
                .setPositiveButton(R.string.choose, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String table1 = ((TextView) view.findViewById(R.id.table1)).getText().toString();
                        String table2 = ((TextView) view.findViewById(R.id.table2)).getText().toString();
                        String table3 = ((TextView) view.findViewById(R.id.table3)).getText().toString();
                        mListener.onDialogPositiveClick(ChooseTablesDialogFragment.this, table1, table2, table3);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(ChooseTablesDialogFragment.this);
                    }
                });
        return builder.create();
    }
}
