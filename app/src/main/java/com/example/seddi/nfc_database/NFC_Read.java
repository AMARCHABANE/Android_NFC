package com.example.seddi.nfc_database;

import android.app.DialogFragment;
import android.content.Context;
import android.net.sip.SipAudioCall;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by seddi on 01/02/2018.
 */

public class NFC_Read extends DialogFragment {
    public static final String TAG = NFC_Read.class.getSimpleName();
    public static NFC_Read newInstance() {

        return new NFC_Read();
    }
    private TextView mTvMessage;
    private Listener mListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_nfc_read,container,false);
        initViews(view);
        return view;
    }
    private void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (MainActivity)context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef){

        readFromNFC(ndef);
    }

    private void readFromNFC(Ndef ndef) {

        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String message = new String(ndefMessage.getRecords()[1].getType());


            //   String msg = new String (ndefMessage.getRecords()[0].getType())


            Log.e(TAG, "readFromNFC: "+message);
            mTvMessage.setText(message);
            ndef.close();

        } catch (IOException | FormatException e) {
            e.printStackTrace();

        }
    }
}
