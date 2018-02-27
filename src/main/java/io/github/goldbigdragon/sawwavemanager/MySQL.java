package io.github.goldbigdragon.sawwavemanager;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class MySQL {
    static private String id = "root";
    static private String password = null;
    static private String address = "localhost";
    static private int port = 3306;
    static private String table = null;

    static void MySqlSetting(String mySqlUserId, String mySqlUserPassword, String mySqlAdress, int mySqlPort) {
        id = mySqlUserId;
        password = mySqlUserPassword;
        address = mySqlAdress;
        port = mySqlPort;
    }

    static boolean MySqlInstallTest() {
        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean createDatabases() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        Calendar cal = Calendar.getInstance();
        table = dateFormat.format(cal.getTime());
        try {
            String driverName = "com.mysql.jdbc.Driver"; // 드라이버 이름 지정
            String databaseUrl = "jdbc:mysql://" + address + ":" + port + "/"; // URL 지정
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(databaseUrl, id, password);

            Statement statement = con.createStatement();
            String SQL = "show databases;";
            ResultSet result = statement.executeQuery(SQL);
            boolean isExit = false;
            while (result.next()) {
                if (result.getString("Database").compareTo("sawwave_manager") == 0) {
                    isExit = true;
                    break;
                }
            }
            if (!isExit) {
                SQL = "CREATE DATABASE `sawwave_manager`;";
                statement.executeUpdate(SQL);
            }
            SQL = "use `sawwave_manager`;";
            statement.execute(SQL);
            SQL = "show tables";
            result = statement.executeQuery(SQL);
            isExit = false;
            while (result.next()) {
                if (result.getString("Tables_in_sawwave_manager").compareTo(table) == 0) {
                    isExit = true;
                    break;
                }
            }
            if (!isExit) {
                SQL =
                        "CREATE TABLE `" + table + "` (" +
                                "`num` int(11) NOT NULL AUTO_INCREMENT," +
                                "`catchTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                                "`Product` varchar(20) DEFAULT NULL," +
                                "`distance` varchar(5) DEFAULT NULL," +
                                "`Tx-Power` varchar(40) DEFAULT NULL," +
                                "`Bandwidth` varchar(20) DEFAULT NULL," +
                                "`Channel` varchar(20) DEFAULT NULL," +
                                "`RSSI` varchar(10) DEFAULT NULL," +
                                "`Tx-Rate` varchar(20) DEFAULT NULL," +
                                "`Rx-Rate` varchar(20) DEFAULT NULL," +
                                "`Tx-ccq` varchar(10) DEFAULT NULL," +
                                "PRIMARY KEY (`num`)" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=euckr;";
                statement.executeUpdate(SQL);
            }
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void executeQuery(String sql) {
        try {
            String driverName = "com.mysql.jdbc.Driver"; // 드라이버 이름 지정
            String dbURL = "jdbc:mysql://" + address + ":" + port + "/sawwave_manager"; // URL 지정
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, id, password);
            PreparedStatement preparedStmt = con.prepareStatement(sql);
            preparedStmt.execute();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void dataInsert() {
        try {
            String driverName = "com.mysql.jdbc.Driver"; // 드라이버 이름 지정
            String dbURL = "jdbc:mysql://" + address + ":" + port + "/sawwave_manager"; // URL 지정

            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, id, password);

            String query = "insert into `sawwave_manager`.`" + table + "` (`catchTime`, `Product`, `distance`, `Tx-Power`, `Bandwidth`, `Channel`, `RSSI`, `Tx-Rate`, `Rx-Rate`, `Tx-ccq`) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStmt = con.prepareStatement(query);

            for (int count = 0; count < Main.BufferStore.size(); count++) {
                String[] splitted = Main.BufferStore.get(count).split("★");
                boolean nullValueExit = false;
                for (int count2 = 0; count2 < 10; count2++) {
                    if (splitted[count2].trim().length() < 1) {
                        nullValueExit = true;
                        break;
                    }
                }
                if (!nullValueExit) {
                    preparedStmt.setString(1, splitted[0]);
                    preparedStmt.setString(2, splitted[1]);
                    preparedStmt.setString(3, splitted[2]);
                    preparedStmt.setString(4, splitted[3]);
                    preparedStmt.setString(5, splitted[4]);
                    preparedStmt.setString(6, splitted[5]);
                    preparedStmt.setString(7, splitted[6]);
                    preparedStmt.setString(8, splitted[7]);
                    preparedStmt.setString(9, splitted[8]);
                    preparedStmt.setString(10, splitted[9]);
                    preparedStmt.execute();
                }
            }
            con.close();
            Main.BufferStore.clear();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Mysql Server Not Connection.");
            e.printStackTrace();
        }
    }

    //CVS파일 추출용 메소드
    static void exportData(int type, String TableName) {
        id = Main.mySqlId.getText();
        password = Main.mySqlPassword.getText();
        address = Main.mySqlAddress.getText();
        port = Integer.parseInt(Main.mySqlPort.getText());

        // 현재 인코딩을 확인한다.
        //String enc = new java.io.OutputStreamWriter(System.out).getEncoding();
        //System.out.println( "현재 인코딩 : "  + enc);
        try {
            String driverName = "com.mysql.jdbc.Driver"; // 드라이버 이름 지정
            String dbURL = "jdbc:mysql://" + address + ":" + port + "/"; // URL 지정
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, id, password);

            Statement stmt = con.createStatement();
            String SQL = "show databases;";
            ResultSet result = stmt.executeQuery(SQL);
            boolean isExit = false;
            while (result.next()) {
                if (result.getString("Database").compareTo("sawwave_manager") == 0) {
                    isExit = true;
                    break;
                }
            }
            if (!isExit) {
                JOptionPane.showMessageDialog(PrintReportFrame.printReportFrame, "해당 테이블은 존재하지 않습니다!", "데이터 없음", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SQL = "use `sawwave_manager`;";
            stmt.execute(SQL);
            SQL = "show tables";
            result = stmt.executeQuery(SQL);
            isExit = false;
            while (result.next()) {
                if (result.getString("Tables_in_sawwave_manager").compareTo(TableName) == 0) {
                    isExit = true;
                    break;
                }
            }
            if (!isExit) {
                JOptionPane.showMessageDialog(PrintReportFrame.printReportFrame, "해당 테이블은 존재하지 않습니다!", "데이터 없음", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //출력 시작

            File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "/Sawwave_Manager_Log");
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.mkdir();
            }
            String fileName = System.getProperty("user.dir") + System.getProperty("file.separator") + "/Sawwave_Manager_Log/[sawwave_manager] [" + TableName + "].csv";
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "MS949"));
            String value;
            writer.write("\r\n");

            ArrayList<String> Product = new ArrayList<>();
            SQL = "select `Product` from `sawwave_manager`.`" + TableName + "` group by `Product`;";
            result = stmt.executeQuery(SQL);
            while (result.next()) {
                Product.add(result.getString("Product"));
            }
            //전체 출력
            if (type == 0) {
                value = " ,Product,distance,Tx-Power,Bandwidth,Channel,Time,RSSI ,Tx Rate,Rx Rate,Tx ccq,Down,Up,Jitter_iferf(UDP),Jitter_JDSU,RFC-2544\r\n";
            } else {
                value = " ,Product,distance,Tx-Power,Bandwidth,Channel,RSSI ,Tx Rate,Rx Rate,Tx ccq,Down,Up,Jitter_iferf(UDP),Jitter_JDSU,RFC-2544\r\n";
            }
            writer.write(value);
            for (String aProduct : Product) {
                value = " ," + aProduct;
                writer.write(value);
                ArrayList<String> Distance = new ArrayList<>();
                SQL = "select `distance` from `sawwave_manager`.`" + TableName + "` where `Product` = \"" + aProduct + "\" group by `distance`;";
                result = stmt.executeQuery(SQL);
                while (result.next())
                    Distance.add(result.getString("distance"));

                for (int count2 = 0; count2 < Distance.size(); count2++) {
                    value = "," + Distance.get(count2);
                    writer.write(value);
                    ArrayList<String> Tx_Power = new ArrayList<>();
                    SQL = "select `Tx-Power` from `sawwave_manager`.`" + TableName + "` where `Product` = \"" + aProduct + "\" and `distance` = \"" + Distance.get(count2) + "\" group by `Tx-Power`;";
                    result = stmt.executeQuery(SQL);
                    while (result.next())
                        Tx_Power.add(result.getString("Tx-Power"));

                    for (int count3 = 0; count3 < Tx_Power.size(); count3++) {
                        if (count3 == 0)
                            value = "," + Tx_Power.get(count3);
                        else
                            value = ",,," + Tx_Power.get(count3);
                        writer.write(value);
                        ArrayList<String> Bandwidth = new ArrayList<>();
                        SQL = "select `Bandwidth` from `sawwave_manager`.`" + TableName + "` where `Product` = \"" + aProduct + "\" and `distance` = \"" + Distance.get(count2) + "\" and `Tx-Power` = \"" + Tx_Power.get(count3) + "\" group by `Bandwidth`;";
                        result = stmt.executeQuery(SQL);
                        while (result.next())
                            Bandwidth.add(result.getString("Bandwidth"));

                        for (int count4 = 0; count4 < Bandwidth.size(); count4++) {
                            if (count4 == 0)
                                value = "," + Bandwidth.get(count4);
                            else
                                value = ",,,," + Bandwidth.get(count4);
                            writer.write(value);
                            ArrayList<String> Channel = new ArrayList<>();
                            SQL = "select `Channel` from `sawwave_manager`.`" + TableName + "` where `Product` = \"" + aProduct + "\" and `distance` = \"" + Distance.get(count2) + "\" and `Tx-Power` = \"" + Tx_Power.get(count3) + "\" and `Bandwidth` = \"" + Bandwidth.get(count4) + "\" group by `Channel`;";
                            result = stmt.executeQuery(SQL);
                            while (result.next())
                                Channel.add(result.getString("Channel"));

                            for (int count5 = 0; count5 < Channel.size(); count5++) {
                                if (count5 == 0)
                                    value = "," + Channel.get(count5);
                                else
                                    value = ",,,,," + Channel.get(count5);
                                writer.write(value);
                                SQL = "select * from `sawwave_manager`.`" + TableName + "` where `Product` = \"" + aProduct + "\" and `distance` = \"" + Distance.get(count2) + "\" and `Tx-Power` = \"" + Tx_Power.get(count3) + "\" and `Bandwidth` = \"" + Bandwidth.get(count4) + "\" and `Channel` = \"" + Channel.get(count5) + "\"order by `catchTime`;";
                                result = stmt.executeQuery(SQL);
                                if (type == 0)//전체 출력
                                {
                                    result.next();
                                    value = "," + result.getString("catchTime") + "," + result.getString("RSSI") + " ," + result.getString("Tx-Rate").replaceAll(",", "，") + "," + result.getString("Rx-Rate").replaceAll(",", "，") + "," + result.getString("Tx-ccq");
                                    writer.write(value);
                                    while (result.next()) {
                                        value = ",,,,,," + result.getString("catchTime") + "," + result.getString("RSSI") + " ," + result.getString("Tx-Rate").replaceAll(",", "，") + "," + result.getString("Rx-Rate").replaceAll(",", "，") + "," + result.getString("Tx-ccq");
                                        writer.write(value);
                                    }
                                } else {
                                    List<Integer> rssiList = new ArrayList<>();
                                    List<Integer> txMbitList = new ArrayList<>();
                                    List<Integer> txMcsList = new ArrayList<>();
                                    List<Integer> rxMbitList = new ArrayList<>();
                                    List<Integer> rxMcsList = new ArrayList<>();
                                    List<Integer> txCcqList = new ArrayList<>();
                                    while (result.next()) {
                                        rssiList.add(Integer.parseInt(result.getString("RSSI").split(" ")[0]));
                                        txMbitList.add(Integer.parseInt(result.getString("Tx-Rate").split(" ")[0]));
                                        txMcsList.add(Integer.parseInt(result.getString("Tx-Rate").split(" ")[3]));
                                        rxMbitList.add(Integer.parseInt(result.getString("Rx-Rate").split(" ")[0]));
                                        rxMcsList.add(Integer.parseInt(result.getString("Rx-Rate").split(" ")[3]));
                                        txCcqList.add(Integer.parseInt(result.getString("Tx-ccq").split(" %")[0]));
                                    }
                                    int rssi = 0;
                                    int txMbit = 0;
                                    int txMcs = 0;
                                    int rxMbit = 0;
                                    int rxMcs = 0;
                                    int txCcq = 0;
                                    if (type == 1)//평균 출력
                                    {
                                        for (int count6 = 0; count6 < rssiList.size(); count6++) {
                                            rssi += rssiList.get(count6);
                                            txMbit += txMbitList.get(count6);
                                            txMcs += txMcsList.get(count6);
                                            rxMbit += rxMbitList.get(count6);
                                            rxMcs += rxMcsList.get(count6);
                                            txCcq += txCcqList.get(count6);
                                        }
                                        rssi /= rssiList.size();
                                        txMbit /= rssiList.size();
                                        txMcs /= rssiList.size();
                                        rxMbit /= rssiList.size();
                                        rxMcs /= rssiList.size();
                                        txCcq /= rssiList.size();
                                    } else if (type == 2)//최대 출력
                                    {
                                        rssi = -1000;
                                        txMbit = -1000;
                                        txMcs = -1000;
                                        rxMbit = -1000;
                                        rxMcs = -1000;
                                        txCcq = -1000;
                                        for (int count6 = 0; count6 < rssiList.size(); count6++) {
                                            if (rssi < rssiList.get(count6)) {
                                                rssi = rssiList.get(count6);
                                            }
                                            if (txMbit < txMbitList.get(count6)) {
                                                txMbit = txMbitList.get(count6);
                                            }
                                            if (txMcs < txMcsList.get(count6)) {
                                                txMcs = txMcsList.get(count6);
                                            }
                                            if (rxMbit < rxMbitList.get(count6)) {
                                                rxMbit = rxMbitList.get(count6);
                                            }
                                            if (rxMcs < rxMcsList.get(count6)) {
                                                rxMcs = rxMcsList.get(count6);
                                            }
                                            if (txCcq < txCcqList.get(count6)) {
                                                txCcq = txCcqList.get(count6);
                                            }
                                        }
                                    } else if (type == 3)//최소 출력
                                    {
                                        rssi = 1000;
                                        txMbit = 1000;
                                        txMcs = 1000;
                                        rxMbit = 1000;
                                        rxMcs = 1000;
                                        txCcq = 1000;
                                        for (int count6 = 0; count6 < rssiList.size(); count6++) {
                                            if (rssi > rssiList.get(count6)) {
                                                rssi = rssiList.get(count6);
                                            }
                                            if (txMbit > txMbitList.get(count6)) {
                                                txMbit = txMbitList.get(count6);
                                            }
                                            if (txMcs > txMcsList.get(count6)) {
                                                txMcs = txMcsList.get(count6);
                                            }
                                            if (rxMbit > rxMbitList.get(count6)) {
                                                rxMbit = rxMbitList.get(count6);
                                            }
                                            if (rxMcs > rxMcsList.get(count6)) {
                                                rxMcs = rxMcsList.get(count6);
                                            }
                                            if (txCcq > txCcqList.get(count6)) {
                                                txCcq = txCcqList.get(count6);
                                            }
                                        }
                                    } else if (type == 4)//CCQ 최대 출력
                                    {
                                        txCcq = -1000;
                                        for (int count6 = 0; count6 < rssiList.size(); count6++) {
                                            if (txCcq < txCcqList.get(count6)) {
                                                txCcq = txCcqList.get(count6);
                                                rssi = rssiList.get(count6);
                                                txMbit = txMbitList.get(count6);
                                                txMcs = txMcsList.get(count6);
                                                rxMbit = rxMbitList.get(count6);
                                                rxMcs = rxMcsList.get(count6);
                                            }
                                        }
                                    } else if (type == 5)//CCQ 최소 출력
                                    {
                                        rssi = 1000;
                                        txMbit = 1000;
                                        txMcs = 1000;
                                        rxMbit = 1000;
                                        rxMcs = 1000;
                                        txCcq = 1000;
                                        for (int count6 = 0; count6 < rssiList.size(); count6++) {
                                            if (txCcq > txCcqList.get(count6)) {
                                                txCcq = txCcqList.get(count6);
                                                rssi = rssiList.get(count6);
                                                txMbit = txMbitList.get(count6);
                                                txMcs = txMcsList.get(count6);
                                                rxMbit = rxMbitList.get(count6);
                                                rxMcs = rxMcsList.get(count6);
                                            }
                                        }
                                    }
                                    value = "," + rssi + " dBm," + txMbit + " Mbit/s， MCS " + txMcs + "," + rxMbit + " Mbit/s， MCS " + rxMcs + "," + txCcq + "%\r\n";
                                    writer.write(value);
                                }
                            }
                        }
                    }
                }
            }

            writer.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(PrintReportFrame.printReportFrame, "MySQL이 설치되지 않았거나\nMySQL 접근 id, password 혹은\n연결 주소 및 포트 등의\n정보가 정확하지 않습니다!", "MySQL 동작 에러", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void catchFreshData() {
        id = Main.mySqlId.getText();
        password = new String(Main.mySqlPassword.getPassword());
        address = Main.mySqlAddress.getText();
        port = Integer.parseInt(Main.mySqlPort.getText());

        try {
            String driverName = "com.mysql.jdbc.Driver"; // 드라이버 이름 지정
            String dbURL = "jdbc:mysql://" + address + ":" + port + "/"; // URL 지정
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, id, password);

            Statement statement = con.createStatement();
            String SQL = "show databases;";
            ResultSet result = statement.executeQuery(SQL);
            boolean isExit = false;
            while (result.next()) {
                if (result.getString("Database").compareTo("sawwave_manager") == 0) {
                    isExit = true;
                    break;
                }
            }
            if (!isExit) {
                JOptionPane.showMessageDialog(PrintReportFrame.printReportFrame, "현재 어떠한 데이터도 존재하지 않습니다!", "데이터 없음", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SQL = "use `sawwave_manager`;";
            statement.execute(SQL);
            SQL = "show tables";
            result = statement.executeQuery(SQL);
            String tableName = null;
            while (result.next()) {
                tableName = result.getString("Tables_in_sawwave_manager");
            }
            if (tableName == null) {
                JOptionPane.showMessageDialog(PrintReportFrame.printReportFrame, "현재 어떠한 데이터도 존재하지 않습니다!", "데이터 없음", JOptionPane.ERROR_MESSAGE);
                return;
            }
            PrintReportFrame.tableName.setText(tableName);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(PrintReportFrame.printReportFrame, "MySQL이 설치되지 않았거나\nMySQL 접근 id, password 혹은\n연결 주소 및 포트 등의\n정보가 정확하지 않습니다!", "MySQL 동작 에러", JOptionPane.ERROR_MESSAGE);
        }
    }
}
