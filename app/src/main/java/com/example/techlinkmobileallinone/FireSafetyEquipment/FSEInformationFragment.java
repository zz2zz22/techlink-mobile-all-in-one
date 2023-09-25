package com.example.techlinkmobileallinone.FireSafetyEquipment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.example.techlinkmobileallinone.controller.UUIDGenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class FSEInformationFragment extends Fragment {
    Connection connect;
    String ConnectionResult = "";
    TextView informationName, informationLocation, informationManager, informationExpDate, informationNearestMaintenance, informationNearestCheck;
    private String deviceUUID, deviceName, deviceTypeID, deviceLocation, deviceManager, expDate, maintenanceDate, checkDate;
    SubMethods subMethods = new SubMethods();

    public FSEInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FireSafetyEquipmentMainActivity activity = (FireSafetyEquipmentMainActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_f_s_e_information, container, false);
        Bundle bundleArgs = getArguments();
        deviceUUID = bundleArgs.getString("deviceUUID");
        deviceName = bundleArgs.getString("deviceName");
        deviceTypeID = bundleArgs.getString("deviceTypeID");
        deviceLocation = bundleArgs.getString("deviceLocation");
        deviceManager = bundleArgs.getString("deviceManager");

        informationName = (TextView)view.findViewById(R.id.informationName);
        informationLocation = (TextView) view.findViewById(R.id.informationLocation);
        informationManager = (TextView) view.findViewById(R.id.informationManager);
        informationExpDate = (TextView) view.findViewById(R.id.informationExpDate);
        informationNearestMaintenance = (TextView) view.findViewById(R.id.informationNearestMaintenance);
        informationNearestCheck = (TextView) view.findViewById(R.id.informationNearestCheck);

        informationName.setText("Tên thiết bị 设备名称:\n" + deviceName);
        informationLocation.setText("Vị trí 定址:\n" + deviceLocation);
        informationManager.setText("Quản lý 经理:\n" + deviceManager);
        GetDeviceInformation();
        informationExpDate.setText(expDate);
        informationNearestMaintenance.setText(maintenanceDate);
        informationNearestCheck.setText(checkDate);
        return view;
    }

    private void GetDeviceInformation()
    {
        FireSafetyEquipmentMainActivity activity = (FireSafetyEquipmentMainActivity)getActivity();
        try {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            connect = databaseConnector.sqlDeviceMaintenanceCon();
            if (connect != null) {
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery("select expire_date,newest_maintenance_date, newest_check_date from pccc_info where device_uuid = '" + deviceUUID + "'");
                while (rs.next()) {
                    expDate = rs.getString(1);
                    maintenanceDate = rs.getString(2);
                    checkDate = rs.getString(3);
                }
            } else {
                subMethods.showErrorDialog(getString(R.string.errorTitle), getString(R.string.sqlNotConnect), activity);
            }
        } catch (Exception ex) {
            subMethods.showInformationDialog(getString(R.string.errorTitle), "Lỗi hệ thống!\n系统错误！\n" + ex.getMessage(), activity);
        }
    }
}