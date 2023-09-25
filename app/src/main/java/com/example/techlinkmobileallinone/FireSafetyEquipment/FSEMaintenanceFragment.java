package com.example.techlinkmobileallinone.FireSafetyEquipment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techlinkmobileallinone.HomeActivity;
import com.example.techlinkmobileallinone.LoginActivity;
import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.example.techlinkmobileallinone.controller.UUIDGenerator;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FSEMaintenanceFragment extends Fragment {
    Connection connect;
    String ConnectionResult = "";
    TextView maintenanceDeviceInfoName, maintenanceDeviceInfoLocation, maintenanceDeviceInfoManager;
    Button expDatePicker;
    EditText maintenanceNoteInput;
    CardView startMaintenanceCard, finishMaintenanceCard;
    private DatePickerDialog expDatePickerDialog;
    private String deviceUUID, deviceName, deviceTypeID, deviceLocation, deviceManager, expDate, empCode, remark;
    SubMethods subMethods = new SubMethods();
    SimpleDateFormat simpleDateFormat;

    public FSEMaintenanceFragment() {
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
        View view = inflater.inflate(R.layout.fragment_f_s_e_maintenance_main, container, false);
        Bundle bundleArgs = getArguments();
        deviceUUID = bundleArgs.getString("deviceUUID");
        deviceName = bundleArgs.getString("deviceName");
        deviceTypeID = bundleArgs.getString("deviceTypeID");
        deviceLocation = bundleArgs.getString("deviceLocation");
        deviceManager = bundleArgs.getString("deviceManager");
        Bundle bundle = activity.getUserData();
        empCode = bundle.getString("empCode");

        maintenanceDeviceInfoName = (TextView) view.findViewById(R.id.maintenanceDeviceInfoName);
        maintenanceDeviceInfoLocation = (TextView) view.findViewById(R.id.maintenanceDeviceInfoLocation);
        maintenanceDeviceInfoManager = (TextView) view.findViewById(R.id.maintenanceDeviceInfoManager);
        expDatePicker = (Button) view.findViewById(R.id.expDatePicker);
        maintenanceNoteInput = (EditText) view.findViewById(R.id.maintenanceNoteInput);
        startMaintenanceCard = (CardView) view.findViewById(R.id.startMaintenanceCard);
        finishMaintenanceCard = (CardView) view.findViewById(R.id.finishMaintenanceCard);

        maintenanceDeviceInfoName.setText("Tên thiết bị 设备名称:\n" + deviceName);
        maintenanceDeviceInfoLocation.setText("Vị trí 定址:\n" + deviceLocation);
        maintenanceDeviceInfoManager.setText("Quản lý 经理:\n" + deviceManager);
        initExpDatePicker();
        expDatePicker.setText(getEstExpDate(deviceTypeID));
        expDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expDatePickerDialog.show();
            }
        });

        startMaintenanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
                alert.setTitle(getString(R.string.informationTitle));
                alert.setMessage("Tiến hành bảo trì thiết bị này ?\n对此设备进行维护吗？");
                alert.setPositiveButton("Đồng ý 同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        executeSaveNewMaintenanceInfo("1");
                    }
                });
                alert.setNegativeButton("Hủy bỏ 撤消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });

        finishMaintenanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
                alert.setTitle(getString(R.string.informationTitle));
                alert.setMessage("Hoàn tất bảo trì thiết bị này ?\n该设备的维护完成了吗？");
                alert.setPositiveButton("Đồng ý 同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        executeSaveNewMaintenanceInfo("0");
                    }
                });
                alert.setNegativeButton("Hủy bỏ 撤消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });

        return view;
    }

    public void executeSaveNewMaintenanceInfo(String type) {
        FireSafetyEquipmentMainActivity activity = (FireSafetyEquipmentMainActivity)getActivity();
        try {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            connect = databaseConnector.sqlDeviceMaintenanceCon();
            if (connect != null) {
                String uuid = UUIDGenerator.getAscId();
                Statement st = connect.createStatement();
                remark = maintenanceNoteInput.getText().toString().trim();
                String insertRecord = "Insert into pccc_maintain_record (uuid, device_uuid, maintenance_type, remark,  create_date, maintain_emp_code) values ('" + uuid + "', '" + deviceUUID + "', '" + type + "', N'" + remark + "', GETDATE(), '"+empCode+"')";
                st.execute(insertRecord);
                String updateDeviceInfo;
                if (type.equals("0"))
                    updateDeviceInfo = "Update pccc_info set expire_date = '" + expDate + "', newest_maintenance_info = '" + uuid + "', newest_maintenance_date = GETDATE(), update_date = GETDATE() where device_uuid = '" + deviceUUID + "'";
                else
                    updateDeviceInfo = "Update pccc_info set newest_maintenance_info = '" + uuid + "', newest_maintenance_date = GETDATE(), update_date = GETDATE() where device_uuid = '" + deviceUUID + "'";
                st.executeUpdate(updateDeviceInfo);

                activity.returnToHome();
                subMethods.showSuccessDialog(getString(R.string.successTitle), "Cập nhật thông tin bảo trì thành công\n维护信息更新成功", activity);
            } else {
                subMethods.showErrorDialog(getString(R.string.errorTitle), getString(R.string.sqlNotConnect), activity);
            }
        } catch (Exception ex) {
            subMethods.showInformationDialog(getString(R.string.errorTitle), "Lỗi hệ thống!\n系统错误！\n" + ex.getMessage(), activity);
        }
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