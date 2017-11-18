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




public class ConfirmSaveDialogFragment extends DialogFragment {
    public interface ConfirmSaveDialogListener {
        void onDialogSavePositiveClick(DialogFragment dialog);
        void onDialogSaveNegativeClick(DialogFragment dialog);
        void onDialogSaveNeutralClick(DialogFragment dialog);
    }

    ConfirmSaveDialogListener listener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ConfirmSaveDialogListener so we can send events to the host
            listener = (ConfirmSaveDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ConfirmSaveDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.save_changes_question);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                listener.onDialogSavePositiveClick(ConfirmSaveDialogFragment.this);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                listener.onDialogSaveNegativeClick(ConfirmSaveDialogFragment.this);
            }
        });
        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onDialogSaveNeutralClick(ConfirmSaveDialogFragment.this);
            }
        });
        return builder.create();
    }
}
