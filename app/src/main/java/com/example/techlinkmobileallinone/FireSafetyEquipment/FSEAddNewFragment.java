package com.example.techlinkmobileallinone.FireSafetyEquipment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;


public class FSEAddNewFragment extends Fragment {
    Connection connect;
    String ConnectionResult = "";
    TextView addDeviceInfoName;
    Button installDatePicker, expDatePicker, scanSaveNewButton;
    private DatePickerDialog installDatePickerDialog, expDatePickerDialog;
    private String deviceUUID, deviceName, deviceTypeID, expDate, installDate;
    SubMethods subMethods = new SubMethods();

    public FSEAddNewFragment() {
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
        View view = inflater.inflate(R.layout.fragment_f_s_e_add_new, container, false);

        Bundle bundleArgs = getArguments();
        deviceUUID = bundleArgs.getString("deviceUUID");
        deviceName = bundleArgs.getString("deviceName");
        deviceTypeID = bundleArgs.getString("deviceTypeID");

        addDeviceInfoName = (TextView) view.findViewById(R.id.addDeviceInfoName);
        expDatePicker = (Button) view.findViewById(R.id.expDatePicker);
        installDatePicker = (Button) view.findViewById(R.id.installDatePicker);
        scanSaveNewButton = (Button) view.findViewById(R.id.scanSaveNewButton);

        addDeviceInfoName.setText("Tên thiết bị 设备名称:\n" + deviceName);
        initExpDatePicker();
        initInstallDatePicker();
        installDatePicker.setText(getEstInstallDate());
        expDatePicker.setText(getEstExpDate(deviceTypeID));
        installDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                installDatePickerDialog.show();
            }
        });
        expDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expDatePickerDialog.show();
            }
        });
        scanSaveNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FSEQRScanFragment();
                Bundle bundleAddArgs = new Bundle();
                bundleAddArgs.putString("deviceUUID", deviceUUID);
                bundleAddArgs.putString("deviceTypeID", deviceTypeID);
                bundleAddArgs.putString("installDate", installDate);
                bundleAddArgs.putString("expDate", expDate);
                activity.enableViews(true);
                activity.replaceFragment(fragment, true, "add", bundleAddArgs);
            }
        });

        return view;
    }
    private String getEstInstallDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (installDate == null  || installDate.length() == 0)
            installDate = makeSQLDate(day, month, year);
        return makeDateString(day, month, year);
    }
    private String getEstExpDate(String deviceTypeID) {
        FireSafetyEquipmentMainActivity activity = (FireSafetyEquipmentMainActivity)getActivity();
        Calendar cal = Calendar.getInstance();
        try {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            connect = databaseConnector.sqlDeviceMaintenanceCon();
            if (connect!=null)
            {
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery("select maximum_exp_date from pccc_type_info where device_type_uuid = '" + deviceTypeID + "'");
                while (rs.next()) {
                    cal.add(Calendar.MONTH, Integer.valueOf(rs.getString(1)));
                }
            }
            else{
                subMethods.showErrorDialog(getString(R.string.errorTitle), getString(R.string.sqlNotConnect), activity);
            }
        } catch (Exception ex) {
            subMethods.showErrorDialog(getString(R.string.errorTitle), ex.getMessage(), activity);
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (expDate == null  || expDate.length() == 0)
            expDate = makeSQLDate(day, month, year);
        return makeDateString(day, month, year);
    }
    private void initInstallDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                installDatePicker.setText(date);
                installDate = makeSQLDate(day, month, year);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        installDatePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
    }
    private void initExpDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                expDatePicker.setText(date);
                expDate = makeSQLDate(day, month, year);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        expDatePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
    }
    private String makeDateString(int day, int month, int year) {
        return String.format("%02d", day) + " / " + String.format("%02d", month) + " / " + year;
    }
    private String makeSQLDate(int day, int month, int year) {
        return year +"-"+ String.format("%02d", month) + "-" + String.format("%02d", day) + " 00:00:00";
    }
}