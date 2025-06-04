package com.example.techlinkmobileallinone.MachineCheck;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.techlinkmobileallinone.FireSafetyEquipment.FSEAddNewFragment;
import com.example.techlinkmobileallinone.FireSafetyEquipment.FSECheckFragment;
import com.example.techlinkmobileallinone.FireSafetyEquipment.FSEInformationFragment;
import com.example.techlinkmobileallinone.FireSafetyEquipment.FSEMaintenanceFragment;
import com.example.techlinkmobileallinone.FireSafetyEquipment.FSEQRScanFragment;
import com.example.techlinkmobileallinone.FireSafetyEquipment.FireSafetyEquipmentMainActivity;
import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.google.zxing.Result;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MCQRScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MCQRScanFragment extends Fragment {

    Connection connect;
    String ConnectionResult = "";
    CodeScanner codeScanner;
    CodeScannerView scannerView;
    TextView scanTitle;
    SubMethods subMethods = new SubMethods();
    FragmentManager fm;
    private int CAMERA_PERMISSION_REQUEST_CODE = 0;
    private String actionType;

    public MCQRScanFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FireSafetyEquipmentMainActivity activity = (FireSafetyEquipmentMainActivity) getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
        Bundle bundle = getArguments();
        actionType = bundle.getString("actionType");
        View view = inflater.inflate(R.layout.fragment_m_c_q_r_scan, container, false);
        scannerView = (CodeScannerView) view.findViewById(R.id.scanner_view);
        scanTitle = (TextView) view.findViewById(R.id.scanTitle);
        codeScanner = new CodeScanner(view.getContext(), scannerView);

        fm = getFragmentManager();
        if (fm.getBackStackEntryCount() == 1) {
            //Kiểm tra fragment này đã được mở hay chưa nếu đã mở thì chuyển sang loại khác
        }
        Runnable runnable = new Runnable() {
            public void run() {
                scanCode(bundle);
            }
        };
        Thread threadScan = new Thread(runnable);
        threadScan.start();

        return view;
    }

    private void scanCode(Bundle bundleArgs) {
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
                        CheckAndAddBundle(bundleArgs, result.getText().trim());
                    }
                });
            }
        });
    }

    private void CheckAndAddBundle(Bundle bundleArgs, String scanResult) {
        MachineCheckMainActivity activity = (MachineCheckMainActivity) getActivity();
        DatabaseConnector databaseConnector = new DatabaseConnector();
        actionType = bundleArgs.getString("actionType");
        String deviceUUID = bundleArgs.getString("deviceUUID");
        String installDate = bundleArgs.getString("installDate");
        String expDate = bundleArgs.getString("expDate");
        String oldLocation = bundleArgs.getString("oldLocation");
        try {
            if (scanResult != null && scanResult.length() > 0) {
                connect = databaseConnector.sqlDeviceMaintenanceCon();
                if (connect != null) {
                    Statement st = connect.createStatement();
                    if (fm.getBackStackEntryCount() == 1) {
                        ResultSet rs = st.executeQuery("select device_type_uuid, device_type_name, device_location, device_manager from pccc_info where device_uuid = '" + scanResult + "'");
                        while (rs.next()) {
                            if (rs.getString(1) == null || rs.getString(1).length() == 0) {
                                subMethods.showInformationDialog(getString(R.string.informationTitle), "Thiết bị không có trong hệ thống\n该设备不在系统中", activity);
                                activity.returnToHome();
                            } else {

                                Bundle bundleAddArgs = new Bundle();
                                bundleAddArgs.putString("deviceUUID", scanResult);
                                bundleAddArgs.putString("deviceTypeID", rs.getString(1));
                                bundleAddArgs.putString("deviceName", rs.getString(2));
                                bundleAddArgs.putString("deviceLocation", rs.getString(3));
                                bundleAddArgs.putString("deviceManager", rs.getString(4));
                                switch (actionType) {
                                    case "maintenance":
                                        if (rs.getString(3) == null || rs.getString(3).length() == 0) {
                                            subMethods.showInformationDialog(getString(R.string.informationTitle), "Thiết bị chưa được thêm vị trí\n未添加此设备的位置", activity);
                                            activity.returnToHome();
                                        } else {
                                            Fragment fragmentMaintenance = new FSEMaintenanceFragment();
                                            activity.enableViews(true);
                                            activity.replaceFragment(fragmentMaintenance, true, "maintenance", bundleAddArgs);
                                        }
                                        break;
                                    case "check":
                                        if (rs.getString(3) == null || rs.getString(3).length() == 0) {
                                            subMethods.showInformationDialog(getString(R.string.informationTitle), "Thiết bị chưa được thêm vị trí\n未添加此设备的位置", activity);
                                            activity.returnToHome();
                                        } else {
                                            Fragment fragmentCheck = new FSECheckFragment();
                                            activity.enableViews(true);
                                            activity.replaceFragment(fragmentCheck, true, "check", bundleAddArgs);
                                        }
                                        break;
                                    case "changeLocation":
                                        if (rs.getString(3) == null || rs.getString(3).length() == 0) {
                                            subMethods.showInformationDialog(getString(R.string.informationTitle), "Thiết bị chưa được thêm vị trí\n未添加此设备的位置", activity);
                                            activity.returnToHome();
                                        } else {
                                            Fragment fragmentChangeLocation = new FSEQRScanFragment();
                                            activity.enableViews(true);
                                            bundleAddArgs.putString("oldLocation", rs.getString(3));
                                            activity.replaceFragment(fragmentChangeLocation, true, "changeLocation", bundleAddArgs);
                                        }
                                        break;
                                    case "add":
                                        if (rs.getString(3) != null && rs.getString(3).length() != 0) {
                                            subMethods.showInformationDialog(getString(R.string.informationTitle), "Thiết bị đã có thông tin\n设备已有信息", activity);
                                            activity.returnToHome();
                                        } else {
                                            Fragment fragmentAdd = new FSEAddNewFragment();
                                            activity.enableViews(true);
                                            activity.replaceFragment(fragmentAdd, true, "add", bundleAddArgs);
                                        }
                                        break;
                                    case "information":
                                        if (rs.getString(3) == null || rs.getString(3).length() == 0) {
                                            subMethods.showInformationDialog(getString(R.string.informationTitle), "Thiết bị chưa được thêm vị trí\n未添加此设备的位置", activity);
                                            activity.returnToHome();
                                        } else {
                                            Fragment fragmentInformation = new FSEInformationFragment();
                                            activity.enableViews(true);
                                            activity.replaceFragment(fragmentInformation, true, "information", bundleAddArgs);
                                        }
                                        break;
                                }
                            }
                        }
                    }
                }
                codeScanner.releaseResources();
            } else {
                subMethods.showInformationDialog(getString(R.string.errorTitle), "Không thể nhận diện QR!\n无法识别二维码!", activity);
                activity.returnToHome();
            }
        } catch (Exception ex) {
            codeScanner.releaseResources();
            subMethods.showInformationDialog(getString(R.string.errorTitle), "Lỗi hệ thống!\n系统错误！\n" + ex.getMessage(), activity);
        }

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