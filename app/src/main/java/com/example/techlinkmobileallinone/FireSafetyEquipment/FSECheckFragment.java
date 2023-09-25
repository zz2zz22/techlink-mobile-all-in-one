package com.example.techlinkmobileallinone.FireSafetyEquipment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.example.techlinkmobileallinone.controller.UUIDGenerator;

import java.sql.Connection;
import java.sql.Statement;

public class FSECheckFragment extends Fragment {
    Connection connect;
    String ConnectionResult = "";
    EditText checkNoteInput;
    CardView checkPassCard, checkFailCard;
    TextView checkDeviceInfoName, checkDeviceInfoLocation, checkDeviceInfoManager;
    private String deviceUUID, deviceName, deviceTypeID, deviceLocation, deviceManager, remark, empCode, empName;
    SubMethods subMethods = new SubMethods();

    public FSECheckFragment() {
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
        View view = inflater.inflate(R.layout.fragment_f_s_e_check, container, false);
        Bundle bundleArgs = getArguments();
        deviceUUID = bundleArgs.getString("deviceUUID");
        deviceName = bundleArgs.getString("deviceName");
        deviceTypeID = bundleArgs.getString("deviceTypeID");
        deviceLocation = bundleArgs.getString("deviceLocation");
        deviceManager = bundleArgs.getString("deviceManager");
        Bundle bundle = activity.getUserData();
        empCode = bundle.getString("empCode");
        empName = bundle.getString("empName");

        checkDeviceInfoName = (TextView) view.findViewById(R.id.checkDeviceInfoName);
        checkDeviceInfoLocation = (TextView) view.findViewById(R.id.checkDeviceInfoLocation);
        checkDeviceInfoManager = (TextView) view.findViewById(R.id.checkDeviceInfoManager);
        checkNoteInput = (EditText) view.findViewById(R.id.checkNoteInput);
        checkPassCard = (CardView) view.findViewById(R.id.checkPassCard);
        checkFailCard = (CardView) view.findViewById(R.id.checkFailCard);

        checkDeviceInfoName.setText("Tên thiết bị 设备名称:\n" + deviceName);
        checkDeviceInfoLocation.setText("Vị trí 定址:\n" + deviceLocation);
        checkDeviceInfoManager.setText("Quản lý 经理:\n" + deviceManager);

        checkPassCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
                alert.setTitle(getString(R.string.informationTitle));
                alert.setMessage("Thiết bị đạt yêu cầu ?\n设备满足要求吗？");
                alert.setPositiveButton("Đồng ý 同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        executeSaveNewCheckInfo("PASS");
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

        checkFailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
                alert.setTitle(getString(R.string.informationTitle));
                alert.setMessage("Thiết bị KHÔNG đạt yêu cầu?\n设备不符合要求？");
                alert.setPositiveButton("Đồng ý 同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        executeSaveNewCheckInfo("FAIL");
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

    public void executeSaveNewCheckInfo(String type) {
        FireSafetyEquipmentMainActivity activity = (FireSafetyEquipmentMainActivity)getActivity();
        try {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            connect = databaseConnector.sqlDeviceMaintenanceCon();
            if (connect != null) {
                String uuid = UUIDGenerator.getAscId();
                Statement st = connect.createStatement();
                remark = checkNoteInput.getText().toString().trim();
                String insertCheckRecord = "Insert into pccc_check_info (check_uuid, device_uuid, check_overall_status, check_description, check_employee, check_date) values ('" + uuid + "', '" + deviceUUID + "', '"+type+"', N'" + remark + "', N'" + empCode + " - " + empName + "', GETDATE())";
                st.execute(insertCheckRecord);

                String updateDeviceInfo = "Update pccc_info set newest_check_info = '" + uuid + "', newest_check_date = GETDATE(), update_date = GETDATE() where device_uuid = '" + deviceUUID + "'";
                st.executeUpdate(updateDeviceInfo);

                activity.returnToHome();
                subMethods.showSuccessDialog(getString(R.string.successTitle), "Hoàn tất kiểm tra thiết bị\n完成设备检查", activity);
            } else {
                subMethods.showErrorDialog(getString(R.string.errorTitle), getString(R.string.sqlNotConnect), activity);
            }
        } catch (Exception ex) {
            subMethods.showInformationDialog(getString(R.string.errorTitle), "Lỗi hệ thống!\n系统错误！\n" + ex.getMessage(), activity);
        }
    }
}