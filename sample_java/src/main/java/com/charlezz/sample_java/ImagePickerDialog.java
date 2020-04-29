package com.charlezz.sample_java;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import life.sabujak.pickle.Pickle;
import life.sabujak.pickle.data.entity.PickleResult;
import life.sabujak.pickle.ui.common.OnResultListener;

public class ImagePickerDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final String[] items = new String[]{"Basic","Insta"};
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (items[which]){
                            case "Basic":
                                Pickle.start(getParentFragmentManager(), new OnResultListener() {
                                    @Override
                                    public void onSuccess(PickleResult result) {

                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });
                                break;
                            case "Insta":
                                break;
                        }
                    }
                })
                .create();
        return dialog;
    }
}
