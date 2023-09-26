package com.example.techlinkmobileallinone.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {
    Connection con;

    @SuppressLint("NewApi")
    public Connection sqlDeviceMaintenanceCon() {
        String uname, pass, ip, port, database;
        ip = "172.16.0.12";
        database = "DEVICES_MAINTENANCE";
        uname = "ERPUSER";
        pass = "12345";
        port = "1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + database + ";user=" + uname + ";password=" + pass + ";";
            DriverManager.setLoginTimeout(2);
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (Exception ex) {
            Log.e("0.12 DEVICES_MAINTENANCE SQL connection Error ", ex.getMessage());
        }
        return connection;
    }
    @SuppressLint("NewApi")
    public Connection sqlProgramsDatabaseCon() {
        String uname, pass, ip, port, database;
        ip = "172.16.0.12";
        database = "programs_database";
        uname = "ERPUSER";
        pass = "12345";
        port = "1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + database + ";user=" + uname + ";password=" + pass + ";";
            DriverManager.setLoginTimeout(2);
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (Exception ex) {
            Log.e("0.12 programs_database SQL connection Error ", ex.getMessage());
        }

        return connection;
    }
    @SuppressLint("NewApi")
    public Connection sqlHRCon() {
        String uname, pass, ip, port, database;
        ip = "172.16.0.9";
        database = "txcard";
        uname = "sa";
        pass = "ppnn13";
        port = "1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "instance=tx;databasename=" + database + ";user=" + uname + ";password=" + pass + ";";
            DriverManager.setLoginTimeout(2);
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (Exception ex) {
            Log.e("HR SQL connection Error ", ex.getMessage());
        }

        return connection;
    }
    @SuppressLint("NewApi")
    public Connection mySqlMESCon() {
        String uname, pass, ip, port, database;
        ip = "172.16.0.22";
        database = "mes_base_data";
        uname = "guest";
        pass = "guest@123";
        port = "3306";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            ConnectionURL = "jdbc:mysql://" + ip + ":" + port + "/" + database;
            DriverManager.setLoginTimeout(2);
            connection = DriverManager.getConnection(ConnectionURL, uname, pass);
        } catch (Exception ex) {
            Log.e("MES My SQL connection Error", ex.getMessage());
        }
        return connection;
    }
}
