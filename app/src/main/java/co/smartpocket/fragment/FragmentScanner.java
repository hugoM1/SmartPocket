package co.smartpocket.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.zxing.Result;

import co.smartpocket.activity.MyActivityUtil;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by hugo on 9/6/14.
 */
public class FragmentScanner extends Fragment implements
        ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;

    public FragmentScanner(){

    }

    public static FragmentScanner getInstance(){
        FragmentScanner fragmentScanner = new FragmentScanner();
        Bundle args = new Bundle();

        fragmentScanner.setArguments(args);
        return fragmentScanner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        Log.w("FRAGEMTN", "SCANNER");

        mScannerView = new ZXingScannerView(getActivity());   // Programmatically initialize the scanner view

        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setFlash(false);
        mScannerView.setAutoFocus(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();// Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("SCANNER_RESULT", rawResult.getText()); // Prints scan results
        Log.v("SCANNER_RESULT_BARCODE_FORMAT", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        Intent intent = new Intent(getActivity(), MyActivityUtil.class);
        intent.putExtra("code", String.valueOf(rawResult.getText()));
        intent.putExtra("codeFormat", String.valueOf(rawResult.getBarcodeFormat()));
        getActivity().startActivityForResult(intent, 10);
    }
}
