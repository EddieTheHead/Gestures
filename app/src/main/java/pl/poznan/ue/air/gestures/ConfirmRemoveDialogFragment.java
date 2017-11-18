package pl.poznan.ue.air.gestures;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;

/**
 * Created by Hubert Bossy on 2017-11-15.
 */




public class ConfirmRemoveDialogFragment extends DialogFragment {
    public interface ConfirmRemoveDialogListener {
        void onDialogRemovePositiveClick(DialogFragment dialog);
        void onDialogRemoveNegativeClick(DialogFragment dialog);
    }

    ConfirmRemoveDialogListener listener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ConfirmRemoveDialogListener so we can send events to the host
            listener = (ConfirmRemoveDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ConfirmRemoveDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.remove_question);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                listener.onDialogRemovePositiveClick(ConfirmRemoveDialogFragment.this);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                listener.onDialogRemoveNegativeClick(ConfirmRemoveDialogFragment.this);
            }
        });

        return builder.create();
    }
}
