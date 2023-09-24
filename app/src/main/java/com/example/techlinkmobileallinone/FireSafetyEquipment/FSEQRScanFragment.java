package com.example.techlinkmobileallinone.FireSafetyEquipment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.google.zxing.Result;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class FSEQRScanFragment extends Fragment {
    Connection connect;
    String ConnectionResult = "";
    CodeScanner codeScanner;
    CodeScannerView scannerView;
    TextView scanTitle;
    SubMethods subMethods = new SubMethods();
    FragmentManager fm;
    public FSEQRScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String actionType = bundle.getString("actionType");
        FireSafetyEquipmentMainActivity activity = (FireSafetyEquipmentMainActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_f_s_e_q_r_scan, container, false);
        scannerView = (CodeScannerView) view.findViewById(R.id.scanner_view);
        scanTitle = (TextView) view.findViewById(R.id.scanTitle);
        codeScanner = new CodeScanner(view.getContext(), scannerView);
        fm = getFragmentManager();
        if(fm.getBackStackEntryCount() == 1)
        {
            scanTitle.setText("Quét tem thiết bị\n扫描设备标签");
        }else{
            scanTitle.setText("Quét tem vị trí\n扫描位置标记");
        }
        return view;
    }
    private void scanCode(Fragment fragment, Activity activity)
    {
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.CONTINUOUS);
        codeScanner.setScanMode(ScanMode.SINGLE);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(true);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(fm.getBackStackEntryCount() == 1)
                            {

                            }else{

                            }
                        } catch (Exception ex) {
                            subMethods.showInformationDialog(getString(R.string.errorTitle), ex.getMessage(), getActivity());
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}