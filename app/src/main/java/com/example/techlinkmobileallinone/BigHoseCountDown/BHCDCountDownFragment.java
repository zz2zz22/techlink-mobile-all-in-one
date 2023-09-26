package com.example.techlinkmobileallinone.BigHoseCountDown;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.example.techlinkmobileallinone.controller.UUIDGenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

public class BHCDCountDownFragment extends Fragment {
    Connection connect;
    String ConnectionResult = "";
    DatabaseConnector databaseConnector = new DatabaseConnector();
    String empCode, empName, stationUUID, stationName, deptUUID, deptName, productResultUUID, productResultNo;
    TextView cdTimerText;
    CardView cdTimerTextCard, cdPassCard, cdFailCard, cdSmokingCard, cdToiletCard, cdSupplyCard, cdChangeCard;
    CountDownTimer countDownTimer, smokingPauseTimer, toiletPauseTimer, supplyPauseTimer, overtimeTimer;

    String totalSmokingPauseTime = "0", totalToiletPauseTime = "0", totalSupplyPauseTime = "0", totalOvertimeTime = "0";
    private static long START_TIME_IN_MILLIS = 0;
    boolean TimerRunning, isOverTime;
    long TimeLeftInMills = START_TIME_IN_MILLIS;
    private String logUUID = null;
    SubMethods subMethods = new SubMethods();

    public BHCDCountDownFragment() {
        // Required empty public constructor
    }
    @Override
    public void onPause() {
        super.onPause();
        if (countDownTimer != null)
            countDownTimer.cancel();
        if (smokingPauseTimer != null)
            smokingPauseTimer.cancel();
        if (toiletPauseTimer != null)
            toiletPauseTimer.cancel();
        if (supplyPauseTimer != null)
            supplyPauseTimer.cancel();
        if (overtimeTimer != null)
            overtimeTimer.cancel();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BigHoseCountDownActivity activity = (BigHoseCountDownActivity) getActivity();

        Bundle results = getArguments();
        //empCode, empName, stationUUID, stationName, deptUUID, deptName;
        empCode = results.getString("empCode");
        empName = results.getString("empName");
        stationUUID = results.getString("stationUUID");
        stationName = results.getString("stationName");
        deptUUID = results.getString("deptUUID");
        deptName = results.getString("deptName");
        productResultUUID = results.getString("productResultUUID");
        productResultNo = results.getString("productResultNo");

        View view = inflater.inflate(R.layout.fragment_b_h_c_d_count_down, container, false);
        cdTimerText = (TextView) view.findViewById(R.id.cdTimerText);

        cdTimerTextCard = (CardView) view.findViewById(R.id.cdTimerTextCard);
        cdPassCard = (CardView) view.findViewById(R.id.cdPassCard);
        cdFailCard = (CardView) view.findViewById(R.id.cdFailCard);
        cdSmokingCard = (CardView) view.findViewById(R.id.cdSmokingCard);
        cdToiletCard = (CardView) view.findViewById(R.id.cdToiletCard);
        cdSupplyCard = (CardView) view.findViewById(R.id.cdSupplyCard);
        cdChangeCard = (CardView) view.findViewById(R.id.cdChangeCard);

        GetLogID();
        GetProductCountDown();
        isOverTime = false;
        smokingPauseTimer = new CountDownTimer(10800000, 1000) {
            public void onTick(long millisUntilFinished) {
                totalSmokingPauseTime = String.valueOf((10800000 - millisUntilFinished) / 1000);
            }

            public void onFinish() {
                //Logic khi hết 3 giờ
            }

        };
        toiletPauseTimer = new CountDownTimer(10800000, 1000) {
            public void onTick(long millisUntilFinished) {
                totalToiletPauseTime = String.valueOf((10800000 - millisUntilFinished) / 1000);
            }

            public void onFinish() {
                //Logic khi hết 3 giờ
            }
        };
        supplyPauseTimer = new CountDownTimer(10800000, 1000) {
            public void onTick(long millisUntilFinished) {
                totalSupplyPauseTime = String.valueOf((10800000 - millisUntilFinished) / 1000);
            }

            public void onFinish() {
                //Logic khi hết 3 giờ
            }
        };
        overtimeTimer = new CountDownTimer(10800000, 1000) {
            public void onTick(long millisUntilFinished) {
                totalOvertimeTime = String.valueOf((10800000 - millisUntilFinished) / 1000);
            }

            public void onFinish() {
                //Logic khi hết 3 giờ
            }
        };
        //cdPassCard, cdFailCard, cdSmokingCard, cdToiletCard, cdSupplyCard, cdChangeCard;
        cdPassCard.setVisibility(View.INVISIBLE);
        cdFailCard.setVisibility(View.INVISIBLE);
        cdSmokingCard.setVisibility(View.INVISIBLE);
        cdToiletCard.setVisibility(View.INVISIBLE);
        cdSupplyCard.setVisibility(View.INVISIBLE);
        cdChangeCard.setVisibility(View.INVISIBLE);


        cdTimerTextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
                alert.setTitle(getString(R.string.informationTitle));
                alert.setMessage("Bắt đầu phiên làm việc?\n开始会话？");
                alert.setPositiveButton("Đồng ý 同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cdPassCard.setVisibility(View.VISIBLE);
                        cdFailCard.setVisibility(View.VISIBLE);
                        cdSmokingCard.setVisibility(View.VISIBLE);
                        cdToiletCard.setVisibility(View.VISIBLE);
                        cdSupplyCard.setVisibility(View.VISIBLE);
                        cdChangeCard.setVisibility(View.VISIBLE);
                        UpdateWorkingStatus();
                        startCountDownTimer();
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
        cdPassCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimerRunning) {
                    UpdateRealTimeQuantity();
                    resetCountDownTimer();
                }else{
                    if(isOverTime)
                    {
                        UpdateOvertimeInfo();
                        UpdateRealTimeQuantity();
                        if (overtimeTimer != null)
                            overtimeTimer.cancel();
                        resetCountDownTimer();
                    }
                }
            }
        });
        cdFailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TimerRunning) {
                    androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
                    alert.setTitle(getString(R.string.informationTitle));
                    alert.setMessage("Báo phế?\n废物报告？");
                    alert.setPositiveButton("Đồng ý 同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UpdateNGQuantity();
                            resetCountDownTimer();
                        }
                    });
                    alert.setNegativeButton("Hủy bỏ 撤消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alert.show();
                }else{
                    if(isOverTime)
                    {
                        UpdateOvertimeInfo();
                        UpdateNGQuantity();
                        if (overtimeTimer != null)
                            overtimeTimer.cancel();
                        resetCountDownTimer();
                    }
                }

            }
        });
        cdSmokingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimerRunning) {
                    androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
                    alert.setTitle(getString(R.string.informationTitle));
                    alert.setMessage("Tạm ngưng để hút thuốc?\n停下来抽烟？");
                    alert.setPositiveButton("Đồng ý 同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cdSmokingCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.aquamarine));
                            cdPassCard.setVisibility(View.INVISIBLE);
                            cdFailCard.setVisibility(View.INVISIBLE);
                            cdToiletCard.setVisibility(View.INVISIBLE);
                            cdSupplyCard.setVisibility(View.INVISIBLE);
                            cdChangeCard.setVisibility(View.INVISIBLE);
                            pauseCountDownTimer();
                            //Tính thời gian đi hút thuốc
                            smokingPauseTimer.start();
                        }
                    });
                    alert.setNegativeButton("Hủy bỏ 撤消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alert.show();
                } else {
                    cdSmokingCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    cdPassCard.setVisibility(View.VISIBLE);
                    cdFailCard.setVisibility(View.VISIBLE);
                    cdToiletCard.setVisibility(View.VISIBLE);
                    cdSupplyCard.setVisibility(View.VISIBLE);
                    cdChangeCard.setVisibility(View.VISIBLE);
                    UpdateSmokingPauseInfo();
                    startCountDownTimer();
                    //Thêm thời gian vào tổng
                    smokingPauseTimer.cancel();
                }
            }
        });
        cdToiletCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimerRunning) {
                    androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
                    alert.setTitle(getString(R.string.informationTitle));
                    alert.setMessage("Tạm ngưng để đi vệ sinh?\n停下来去洗手间？");
                    alert.setPositiveButton("Đồng ý 同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cdToiletCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.aquamarine));
                            cdPassCard.setVisibility(View.INVISIBLE);
                            cdFailCard.setVisibility(View.INVISIBLE);
                            cdSmokingCard.setVisibility(View.INVISIBLE);
                            cdSupplyCard.setVisibility(View.INVISIBLE);
                            cdChangeCard.setVisibility(View.INVISIBLE);
                            pauseCountDownTimer();
                            //Tính thời gian đi vệ sinh
                            toiletPauseTimer.start();
                        }
                    });
                    alert.setNegativeButton("Hủy bỏ 撤消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alert.show();

                } else {
                    cdToiletCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    cdPassCard.setVisibility(View.VISIBLE);
                    cdFailCard.setVisibility(View.VISIBLE);
                    cdSmokingCard.setVisibility(View.VISIBLE);
                    cdSupplyCard.setVisibility(View.VISIBLE);
                    cdChangeCard.setVisibility(View.VISIBLE);
                    UpdateToiletPauseInfo();
                    startCountDownTimer();
                    //Thêm thời gian vào tổng
                    toiletPauseTimer.cancel();
                }
            }
        });

        cdSupplyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimerRunning) {
                    androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
                    alert.setTitle(getString(R.string.informationTitle));
                    alert.setMessage("Tạm ngưng để lãnh liệu?\n暂停接收原材料？");
                    alert.setPositiveButton("Đồng ý 同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cdSupplyCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.aquamarine));
                            cdPassCard.setVisibility(View.INVISIBLE);
                            cdFailCard.setVisibility(View.INVISIBLE);
                            cdToiletCard.setVisibility(View.INVISIBLE);
                            cdSmokingCard.setVisibility(View.INVISIBLE);
                            cdChangeCard.setVisibility(View.INVISIBLE);
                            pauseCountDownTimer();
                            //Tính thời gian đi lãnh liệu
                            supplyPauseTimer.start();
                        }
                    });
                    alert.setNegativeButton("Hủy bỏ 撤消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alert.show();
                } else {
                    cdSupplyCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    cdPassCard.setVisibility(View.VISIBLE);
                    cdFailCard.setVisibility(View.VISIBLE);
                    cdSmokingCard.setVisibility(View.VISIBLE);
                    cdToiletCard.setVisibility(View.VISIBLE);
                    cdChangeCard.setVisibility(View.VISIBLE);
                    UpdateSupplyPauseInfo();
                    startCountDownTimer();
                    //Thêm thời gian vào tổng
                    supplyPauseTimer.cancel();
                }
            }
        });
        cdChangeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
                alert.setTitle(getString(R.string.informationTitle));
                alert.setMessage("Đổi mã thành phẩm?\n更改成品代码？");
                alert.setPositiveButton("Đồng ý 同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       activity.returnToHome();
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
        updateCountText();
        return view;
    }
    private void UpdateOvertimeInfo() {
        BigHoseCountDownActivity activity = (BigHoseCountDownActivity) getActivity();
        connect = databaseConnector.sqlProgramsDatabaseCon();
        isOverTime = false;
        if (connect != null) {
            try {
                Statement st = connect.createStatement();
                String updateResult = "update big_hose_daily_employee_countdown_log set overtime = overtime + 1, total_overtime = total_overtime + " + totalOvertimeTime + " where uuid = '" + logUUID + "'";
                st.execute(updateResult);
                connect.close();
            } catch (Exception ex) {
                subMethods.showErrorDialog(getString(R.string.errorTitle), "Lỗi thêm dữ liệu\n添加数据时出错 \n\n" + ex.getMessage(), activity);
                activity.returnToHome();
            }
        } else {
            subMethods.showErrorDialog(getString(R.string.errorTitle), "Không thể kết nối với máy chủ!\n无法连接到服务器！", activity);
            activity.returnToHome();
        }
    }
    private void UpdateSupplyPauseInfo() {
        BigHoseCountDownActivity activity = (BigHoseCountDownActivity) getActivity();
        connect = databaseConnector.sqlProgramsDatabaseCon();
        if (connect != null) {
            try {
                Statement st = connect.createStatement();
                String updateResult = "update big_hose_daily_employee_countdown_log set supply_stop = supply_stop + 1, total_supply_time = total_supply_time + " + totalSupplyPauseTime + " where uuid = '" + logUUID + "'";
                st.execute(updateResult);
                connect.close();
            } catch (Exception ex) {
                subMethods.showErrorDialog(getString(R.string.errorTitle), "Lỗi thêm dữ liệu\n添加数据时出错 \n\n" + ex.getMessage(), activity);
                activity.returnToHome();
            }
        } else {
            subMethods.showErrorDialog(getString(R.string.errorTitle), "Không thể kết nối với máy chủ!\n无法连接到服务器！", activity);
            activity.returnToHome();
        }
    }

    private void UpdateToiletPauseInfo() {
        BigHoseCountDownActivity activity = (BigHoseCountDownActivity) getActivity();
        connect = databaseConnector.sqlProgramsDatabaseCon();
        if (connect != null) {
            try {
                Statement st = connect.createStatement();
                String updateResult = "update big_hose_daily_employee_countdown_log set toilet_stop = toilet_stop + 1, total_toilet_time = total_toilet_time + " + totalToiletPauseTime + " where uuid = '" + logUUID + "'";
                st.execute(updateResult);
                connect.close();
            } catch (Exception ex) {
                subMethods.showErrorDialog(getString(R.string.errorTitle), "Lỗi thêm dữ liệu\n添加数据时出错 \n\n" + ex.getMessage(), activity);
                activity.returnToHome();
            }
        } else {
            subMethods.showErrorDialog(getString(R.string.errorTitle), "Không thể kết nối với máy chủ!\n无法连接到服务器！", activity);
            activity.returnToHome();
        }
    }
    private void UpdateSmokingPauseInfo() {
        BigHoseCountDownActivity activity = (BigHoseCountDownActivity) getActivity();
        connect = databaseConnector.sqlProgramsDatabaseCon();
        if (connect != null) {
            try {
                Statement st = connect.createStatement();
                String updateResult = "update big_hose_daily_employee_countdown_log set smoking_stop = smoking_stop + 1, total_smoking_time = total_smoking_time + " + totalSmokingPauseTime + " where uuid = '" + logUUID + "'";
                st.execute(updateResult);
                connect.close();
            } catch (Exception ex) {
                subMethods.showErrorDialog(getString(R.string.errorTitle), "Lỗi thêm dữ liệu\n添加数据时出错 \n\n" + ex.getMessage(), activity);
                activity.returnToHome();
            }
        } else {
            subMethods.showErrorDialog(getString(R.string.errorTitle), "Không thể kết nối với máy chủ!\n无法连接到服务器！", activity);
            activity.returnToHome();
        }
    }
    private void UpdateNGQuantity() {
        BigHoseCountDownActivity activity = (BigHoseCountDownActivity) getActivity();
        connect = databaseConnector.sqlProgramsDatabaseCon();
        if (connect != null) {
            try {
                Statement st = connect.createStatement();
                String updateResult = "update big_hose_countdown_result set ng_qty = ng_qty + 1 where uuid = '" + productResultUUID + "'";
                st.execute(updateResult);
                String updateLog = "update big_hose_daily_employee_countdown_log set ng_qty = ng_qty + 1 where uuid = '" + logUUID + "'";
                st.execute(updateLog);
                connect.close();
                Toast.makeText(activity, "Báo phế thành công!\n报废成功！", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                subMethods.showErrorDialog(getString(R.string.errorTitle), "Lỗi thêm dữ liệu\n添加数据时出错 \n\n" + ex.getMessage(), activity);
                activity.returnToHome();
            }
        } else {
            subMethods.showErrorDialog(getString(R.string.errorTitle), "Không thể kết nối với máy chủ!\n无法连接到服务器！", activity);
            activity.returnToHome();
        }
    }
    private void UpdateWorkingStatus() {
        BigHoseCountDownActivity activity = (BigHoseCountDownActivity) getActivity();
        connect = databaseConnector.sqlProgramsDatabaseCon();
        if (connect != null) {
            try {
                Statement st = connect.createStatement();
                String updateResult = "update big_hose_countdown_result set isWorking = 1 where uuid = '" + productResultUUID + "'";
                st.execute(updateResult);
                if (logUUID == null) {
                    logUUID = UUIDGenerator.getAscId();
                    String insertLog = "exec Insert_big_hose_daily_employee_countdown_log '" + logUUID + "', '" + stationUUID + "', '" + productResultUUID + "', '" + productResultNo + "', '" + empName + "', '" + empCode + "', 1";
                    st.execute(insertLog);
                }
                Toast.makeText(activity, "Khởi tạo thành công!\n初始化成功！", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                subMethods.showErrorDialog(getString(R.string.errorTitle), "Lỗi thêm dữ liệu\n添加数据时出错 \n\n" + ex.getMessage(), activity);
                activity.returnToHome();
            }
        } else {
            subMethods.showErrorDialog(getString(R.string.errorTitle), "Không thể kết nối với máy chủ!\n无法连接到服务器！", activity);
            activity.returnToHome();
        }
    }

    private void UpdateRealTimeQuantity() {
        BigHoseCountDownActivity activity = (BigHoseCountDownActivity) getActivity();
        connect = databaseConnector.sqlProgramsDatabaseCon();
        if (connect != null) {
            try {
                Statement st = connect.createStatement();
                String updateResult = "update big_hose_countdown_result set realtime_qty = realtime_qty + 1 where uuid = '" + productResultUUID + "'";
                st.execute(updateResult);
                String updateLog = "update big_hose_daily_employee_countdown_log set realtime_qty = realtime_qty + 1 where uuid = '" + logUUID + "'";
                st.execute(updateLog);
                connect.close();
                Toast.makeText(activity, "Thêm thành công\n更多成功", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                subMethods.showErrorDialog(getString(R.string.errorTitle), "Lỗi thêm dữ liệu\n添加数据时出错 \n\n" + ex.getMessage(), activity);
                activity.returnToHome();
            }
        } else {
            subMethods.showErrorDialog(getString(R.string.errorTitle), "Không thể kết nối với máy chủ!\n无法连接到服务器！", activity);
            activity.returnToHome();
        }
    }

    private void GetLogID() {
        BigHoseCountDownActivity activity = (BigHoseCountDownActivity) getActivity();
        try {
            connect = databaseConnector.sqlProgramsDatabaseCon();
            if (connect != null) {
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery("select uuid from big_hose_daily_employee_countdown_log where station_uuid = '" + stationUUID + "' and cd_result_uuid = '" + productResultUUID + "' and employee_code = '" + empCode + "'");
                while (rs.next()) {
                    if (!rs.getString("uuid").isEmpty()) {
                        logUUID = rs.getString("uuid");
                    }
                }
            } else {
                subMethods.showErrorDialog(getString(R.string.errorTitle), "Không thể kết nối với máy chủ!\n无法连接到服务器！", activity);
                activity.returnToHome();
            }
        } catch (Exception ex) {
            subMethods.showErrorDialog(getString(R.string.errorTitle), "Lỗi không xác định:\n未知错误：\n\n" + ex.getMessage(), activity);
            activity.returnToHome();
        }
    }

    private void GetProductCountDown() {
        BigHoseCountDownActivity activity = (BigHoseCountDownActivity) getActivity();
        try {
            connect = databaseConnector.sqlProgramsDatabaseCon();
            if (connect != null) {
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery("select count_down from big_hose_product_countdown where product_no = '" + productResultNo + "' and station_uuid = '" + stationUUID + "'");
                while (rs.next()) {
                    if (!rs.getString("count_down").isEmpty()) {
                        START_TIME_IN_MILLIS = (long) Float.parseFloat(rs.getString("count_down")) * 1000;
                    }
                }
            } else {
                subMethods.showErrorDialog(getString(R.string.errorTitle), "Không thể kết nối với máy chủ!\n无法连接到服务器！", activity);
                activity.returnToHome();
            }
        } catch (Exception ex) {
            subMethods.showErrorDialog(getString(R.string.errorTitle), "Lỗi không xác định:\n未知错误：\n\n" + ex.getMessage(), activity);
            activity.returnToHome();
        }
    }

    private void resetCountDownTimer() {
        countDownTimer.cancel();
        updateCountText();
        startCountDownTimer();
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeftInMills = millisUntilFinished;
                updateCountText();
            }

            @Override
            public void onFinish() {
                TimerRunning = false;
                isOverTime = true;
                overtimeTimer.start();
            }
        }.start();
        TimerRunning = true;
    }

    private void updateCountText() {
        int minutes = (int) (TimeLeftInMills / 1000) / 60;
        int seconds = (int) (TimeLeftInMills / 1000) % 60;

        String timeFormat = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        cdTimerText.setText(timeFormat);
    }
    private void pauseCountDownTimer() {
        countDownTimer.cancel();
        TimerRunning = false;
    }
}