package com.example.techlinkmobileallinone.FireSafetyEquipment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.techlinkmobileallinone.MainActivity;
import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.example.techlinkmobileallinone.main.SettingFragment;
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
    private int CAMERA_PERMISSION_REQUEST_CODE = 0;
    private String actionType;

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
        FireSafetyEquipmentMainActivity activity = (FireSafetyEquipmentMainActivity) getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
        Bundle bundle = getArguments();
        actionType = bundle.getString("actionType");
        View view = inflater.inflate(R.layout.fragment_f_s_e_q_r_scan, container, false);
        scannerView = (CodeScannerView) view.findViewById(R.id.scanner_view);
        scanTitle = (TextView) view.findViewById(R.id.scanTitle);
        codeScanner = new CodeScanner(view.getContext(), scannerView);

        fm = getFragmentManager();
        if (fm.getBackStackEntryCount() == 1) {
            scanTitle.setText("Quét tem thiết bị\n扫描设备标签");
            if (actionType == "changeLocation")
                subMethods.showInformationDialog(getString(R.string.informationTitle), "Hãy quét tem thiết bị muốn chuyển!\n请扫描您要传输的设备标签！", activity);
            if (actionType == "insight")
                subMethods.showInformationDialog(getString(R.string.informationTitle), "Hãy quét tem thiết bị muốn chuyển!\n请扫描您要传输的设备标签！", activity);
        } else {
            if (actionType == "changeLocation" && fm.getBackStackEntryCount() == 2) {
                scanTitle.setText("Quét tem thiết bị\n扫描设备标签");
                subMethods.showInformationDialog(getString(R.string.informationTitle), "Hãy quét tem thiết bị thay thế!\n扫描替换设备标签！", activity);
            } else {
                scanTitle.setText("Quét tem vị trí\n扫描位置标记");
                subMethods.showInformationDialog(getString(R.string.informationTitle), "Quét tem vị trí\n扫描位置标记", activity);
            }
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
        FireSafetyEquipmentMainActivity activity = (FireSafetyEquipmentMainActivity) getActivity();
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
                                    case "insight":
                                        if (rs.getString(3) == null || rs.getString(3).length() == 0) {
                                            subMethods.showInformationDialog(getString(R.string.informationTitle), "Thiết bị chưa được thêm vị trí\n未添加此设备的位置", activity);
                                            activity.returnToHome();
                                        } else {
                                            Fragment fragmentChangeLocation = new FSEQRScanFragment();
                                            activity.enableViews(true);
                                            activity.replaceFragment(fragmentChangeLocation, true, "insight", bundleAddArgs);
                                        }
                                        break;
                                }
                            }
                        }
                    } else {
                        if (actionType == "changeLocation" && fm.getBackStackEntryCount() == 2) {
                            ResultSet rs1 = st.executeQuery("select device_type_uuid, device_type_name, device_location, device_manager from pccc_info where device_uuid = '" + scanResult + "'");
                            while (rs1.next()) {
                                if (rs1.getString(1) == null || rs1.getString(1).length() == 0) {
                                    subMethods.showInformationDialog(getString(R.string.informationTitle), "Thiết bị không có trong hệ thống\n该设备不在系统中", activity);
                                    activity.returnToHome();
                                }else{
                                    String newLocation = rs1.getString(3);
                                    if(newLocation == null || newLocation.length() == 0)
                                    {
                                        subMethods.showInformationDialog(getString(R.string.informationTitle), "Thiết bị chưa được thêm vị trí\n未添加此设备的位置", activity);
                                        activity.returnToHome();
                                    }else{
                                        Statement stUpdate = connect.createStatement();
                                        String updateDeviceLocationQuery = "exec Update_device_location '"+deviceUUID+"', '"+oldLocation+"', '"+scanResult+"', '"+newLocation+"'";
                                        stUpdate.executeUpdate(updateDeviceLocationQuery);
                                        activity.returnToHome();
                                        subMethods.showSuccessDialog(getString(R.string.successTitle), "Thay đổi vị trí thiết bị thành công\n设备位置更改成功", activity);
                                    }
                                }
                            }

                        }
                        else if (actionType == "insight" && fm.getBackStackEntryCount() == 2)
                        {
                            String[] locationQR = scanResult.split("[;]");
                            if (locationQR.length > 1)
                            {
                                String location = locationQR[0].trim();
                                String manager = locationQR[1].trim();
                                String updateDeviceLocationQuery = "update pccc_info set device_location = N'" + location + "', update_date = GETDATE() where device_uuid = '" + deviceUUID + "'";
                                st.executeUpdate(updateDeviceLocationQuery);
                                activity.returnToHome();
                                subMethods.showSuccessDialog(getString(R.string.successTitle), "Thay đổi vị trí thiết bị thành công\n设备位置更改成功", activity);
                            }
                        }
                        else {
                            String[] locationQR = scanResult.split("[;]");
                            if (locationQR.length > 1) {
                                String location = locationQR[0].trim();
                                String manager = locationQR[1].trim();
                                switch (actionType) {
                                    case "add":
                                        String addDeviceInfoQuery = "Update pccc_info set device_location = N'" + location + "', installed_date = '" + installDate + "', expire_date = '" + expDate + "', update_date = GETDATE() where device_uuid = '" + deviceUUID + "'";
                                        st.executeUpdate(addDeviceInfoQuery);
                                        activity.returnToHome();
                                        subMethods.showSuccessDialog(getString(R.string.successTitle), "Thêm mới thiết bị thành công\n新设备添加成功", activity);
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