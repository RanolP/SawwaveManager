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

public class MySQL {
    static private String ID = "root";
    static private String Password = null;
    static private String Address = "localhost";
    static private int Port = 3306;
    static private String Table = null;

    public void MySqlSetting(String mySqlUserId, String mySqlUserPassword, String mySqlAdress, int mySqlPort) {
        this.ID = mySqlUserId;
        this.Password = mySqlUserPassword;
        this.Address = mySqlAdress;
        this.Port = mySqlPort;
        return;
    }

    public boolean MySqlInstallTest() {
        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean CreateDatabases() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        Calendar cal = Calendar.getInstance();
        this.Table = dateFormat.format(cal.getTime());
        try {
            String driverName = "com.mysql.jdbc.Driver"; // 드라이버 이름 지정
            String dbURL = "jdbc:mysql://" + Address + ":" + Port + "/"; // URL 지정
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, ID, Password);

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
            if (isExit == false) {
                SQL = "CREATE DATABASE `sawwave_manager`;";
                stmt.executeUpdate(SQL);
            }
            SQL = "use `sawwave_manager`;";
            stmt.execute(SQL);
            SQL = "show tables";
            result = stmt.executeQuery(SQL);
            isExit = false;
            while (result.next()) {
                if (result.getString("Tables_in_sawwave_manager").compareTo(Table) == 0) {
                    isExit = true;
                    break;
                }
            }
            if (isExit == false) {
                SQL =
                        "CREATE TABLE `" + Table + "` (" +
                                "`num` int(11) NOT NULL AUTO_INCREMENT," +
                                "`catchTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                                "`Product` varchar(20) DEFAULT NULL," +
                                "`Distance` varchar(5) DEFAULT NULL," +
                                "`Tx-Power` varchar(40) DEFAULT NULL," +
                                "`Bandwidth` varchar(20) DEFAULT NULL," +
                                "`Channel` varchar(20) DEFAULT NULL," +
                                "`RSSI` varchar(10) DEFAULT NULL," +
                                "`Tx-Rate` varchar(20) DEFAULT NULL," +
                                "`Rx-Rate` varchar(20) DEFAULT NULL," +
                                "`Tx-ccq` varchar(10) DEFAULT NULL," +
                                "PRIMARY KEY (`num`)" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=euckr;";
                stmt.executeUpdate(SQL);
            }
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void ExcuteQuery(String SQL) {
        try {
            String driverName = "com.mysql.jdbc.Driver"; // 드라이버 이름 지정
            String dbURL = "jdbc:mysql://" + Address + ":" + Port + "/sawwave_manager"; // URL 지정
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, ID, Password);
            PreparedStatement preparedStmt = con.prepareStatement(SQL);
            preparedStmt.execute();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void dataInsert() {
        try {
            String driverName = "com.mysql.jdbc.Driver"; // 드라이버 이름 지정
            String dbURL = "jdbc:mysql://" + Address + ":" + Port + "/sawwave_manager"; // URL 지정

            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, ID, Password);

            String query = "insert into `sawwave_manager`.`" + Table + "` (`catchTime`, `Product`, `Distance`, `Tx-Power`, `Bandwidth`, `Channel`, `RSSI`, `Tx-Rate`, `Rx-Rate`, `Tx-ccq`) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStmt = con.prepareStatement(query);

            for (int count = 0; count < Main.BufferStore.size(); count++) {
                String[] Splitted = Main.BufferStore.get(count).split("★");
                boolean nullValueExit = false;
                for (int count2 = 0; count2 < 10; count2++) {
                    if (Splitted[count2].trim().length() < 1) {
                        nullValueExit = true;
                        break;
                    }
                }
                if (nullValueExit == false) {
                    preparedStmt.setString(1, Splitted[0]);
                    preparedStmt.setString(2, Splitted[1]);
                    preparedStmt.setString(3, Splitted[2]);
                    preparedStmt.setString(4, Splitted[3]);
                    preparedStmt.setString(5, Splitted[4]);
                    preparedStmt.setString(6, Splitted[5]);
                    preparedStmt.setString(7, Splitted[6]);
                    preparedStmt.setString(8, Splitted[7]);
                    preparedStmt.setString(9, Splitted[8]);
                    preparedStmt.setString(10, Splitted[9]);
                    preparedStmt.execute();
                }
            }
            con.close();
            Main.BufferStore.clear();
        } catch (Exception e) {
            System.out.println("Mysql Server Not Connection.");
            e.printStackTrace();
        }
    }

    //CVS파일 추출용 메소드
    public void exportData(int type, String TableName) {
        ID = Main.mySqlID.getText();
        Password = Main.mySqlPassword.getText();
        Address = Main.mySqlAddress.getText();
        Port = Integer.parseInt(Main.mySqlPort.getText());

        // 현재 인코딩을 확인한다.
        //String enc = new java.io.OutputStreamWriter(System.out).getEncoding();
        //System.out.println( "현재 인코딩 : "  + enc);
        try {
            String driverName = "com.mysql.jdbc.Driver"; // 드라이버 이름 지정
            String dbURL = "jdbc:mysql://" + Address + ":" + Port + "/"; // URL 지정
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, ID, Password);

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
            if (isExit == false) {
                JOptionPane.showMessageDialog(PrintReportFrame.PrintReportFrame, "해당 테이블은 존재하지 않습니다!", "데이터 없음", JOptionPane.ERROR_MESSAGE);
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
            if (isExit == false) {
                JOptionPane.showMessageDialog(PrintReportFrame.PrintReportFrame, "해당 테이블은 존재하지 않습니다!", "데이터 없음", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //출력 시작

            File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "/Sawwave_Manager_Log");
            if (file.exists() == false)
                file.mkdir();
            String fileName = System.getProperty("user.dir") + System.getProperty("file.separator") + "/Sawwave_Manager_Log/[sawwave_manager] [" + TableName + "].csv";
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "MS949"));
            String Value = "\r\n";
            writer.write(Value);

            ArrayList<String> Product = new ArrayList<String>();
            SQL = "select `Product` from `sawwave_manager`.`" + TableName + "` group by `Product`;";
            result = stmt.executeQuery(SQL);
            while (result.next())
                Product.add(result.getString("Product"));

            if (type == 0) //전체 출력
                Value = " ,Product,Distance,Tx-Power,Bandwidth,Channel,Time,RSSI ,Tx Rate,Rx Rate,Tx ccq,Down,Up,Jitter_iferf(UDP),Jitter_JDSU,RFC-2544\r\n";
            else
                Value = " ,Product,Distance,Tx-Power,Bandwidth,Channel,RSSI ,Tx Rate,Rx Rate,Tx ccq,Down,Up,Jitter_iferf(UDP),Jitter_JDSU,RFC-2544\r\n";
            writer.write(Value);
            for (int count = 0; count < Product.size(); count++) {
                Value = " ," + Product.get(count);
                writer.write(Value);
                ArrayList<String> Distance = new ArrayList<String>();
                SQL = "select `Distance` from `sawwave_manager`.`" + TableName + "` where `Product` = \"" + Product.get(count) + "\" group by `Distance`;";
                result = stmt.executeQuery(SQL);
                while (result.next())
                    Distance.add(result.getString("Distance"));

                for (int count2 = 0; count2 < Distance.size(); count2++) {
                    Value = "," + Distance.get(count2);
                    writer.write(Value);
                    ArrayList<String> Tx_Power = new ArrayList<String>();
                    SQL = "select `Tx-Power` from `sawwave_manager`.`" + TableName + "` where `Product` = \"" + Product.get(count) + "\" and `Distance` = \"" + Distance.get(count2) + "\" group by `Tx-Power`;";
                    result = stmt.executeQuery(SQL);
                    while (result.next())
                        Tx_Power.add(result.getString("Tx-Power"));

                    for (int count3 = 0; count3 < Tx_Power.size(); count3++) {
                        if (count3 == 0)
                            Value = "," + Tx_Power.get(count3);
                        else
                            Value = ",,," + Tx_Power.get(count3);
                        writer.write(Value);
                        ArrayList<String> Bandwidth = new ArrayList<String>();
                        SQL = "select `Bandwidth` from `sawwave_manager`.`" + TableName + "` where `Product` = \"" + Product.get(count) + "\" and `Distance` = \"" + Distance.get(count2) + "\" and `Tx-Power` = \"" + Tx_Power.get(count3) + "\" group by `Bandwidth`;";
                        result = stmt.executeQuery(SQL);
                        while (result.next())
                            Bandwidth.add(result.getString("Bandwidth"));

                        for (int count4 = 0; count4 < Bandwidth.size(); count4++) {
                            if (count4 == 0)
                                Value = "," + Bandwidth.get(count4);
                            else
                                Value = ",,,," + Bandwidth.get(count4);
                            writer.write(Value);
                            ArrayList<String> Channel = new ArrayList<String>();
                            SQL = "select `Channel` from `sawwave_manager`.`" + TableName + "` where `Product` = \"" + Product.get(count) + "\" and `Distance` = \"" + Distance.get(count2) + "\" and `Tx-Power` = \"" + Tx_Power.get(count3) + "\" and `Bandwidth` = \"" + Bandwidth.get(count4) + "\" group by `Channel`;";
                            result = stmt.executeQuery(SQL);
                            while (result.next())
                                Channel.add(result.getString("Channel"));

                            for (int count5 = 0; count5 < Channel.size(); count5++) {
                                if (count5 == 0)
                                    Value = "," + Channel.get(count5);
                                else
                                    Value = ",,,,," + Channel.get(count5);
                                writer.write(Value);
                                SQL = "select * from `sawwave_manager`.`" + TableName + "` where `Product` = \"" + Product.get(count) + "\" and `Distance` = \"" + Distance.get(count2) + "\" and `Tx-Power` = \"" + Tx_Power.get(count3) + "\" and `Bandwidth` = \"" + Bandwidth.get(count4) + "\" and `Channel` = \"" + Channel.get(count5) + "\"order by `catchTime`;";
                                result = stmt.executeQuery(SQL);
                                if (type == 0)//전체 출력
                                {
                                    result.next();
                                    Value = "," + result.getString("catchTime") + "," + result.getString("RSSI") + " ," + result.getString("Tx-Rate").replaceAll(",", "，") + "," + result.getString("Rx-Rate").replaceAll(",", "，") + "," + result.getString("Tx-ccq");
                                    writer.write(Value);
                                    while (result.next()) {
                                        Value = ",,,,,," + result.getString("catchTime") + "," + result.getString("RSSI") + " ," + result.getString("Tx-Rate").replaceAll(",", "，") + "," + result.getString("Rx-Rate").replaceAll(",", "，") + "," + result.getString("Tx-ccq");
                                        writer.write(Value);
                                    }
                                } else {
                                    ArrayList<Integer> RSSI_List = new ArrayList<Integer>();
                                    ArrayList<Integer> Tx_Mbit_List = new ArrayList<Integer>();
                                    ArrayList<Integer> Tx_MCS_List = new ArrayList<Integer>();
                                    ArrayList<Integer> Rx_Mbit_List = new ArrayList<Integer>();
                                    ArrayList<Integer> Rx_MCS_List = new ArrayList<Integer>();
                                    ArrayList<Integer> Tx_CCQ_List = new ArrayList<Integer>();
                                    while (result.next()) {
                                        RSSI_List.add(Integer.parseInt(result.getString("RSSI").split(" ")[0]));
                                        Tx_Mbit_List.add(Integer.parseInt(result.getString("Tx-Rate").split(" ")[0]));
                                        Tx_MCS_List.add(Integer.parseInt(result.getString("Tx-Rate").split(" ")[3]));
                                        Rx_Mbit_List.add(Integer.parseInt(result.getString("Rx-Rate").split(" ")[0]));
                                        Rx_MCS_List.add(Integer.parseInt(result.getString("Rx-Rate").split(" ")[3]));
                                        Tx_CCQ_List.add(Integer.parseInt(result.getString("Tx-ccq").split(" %")[0]));
                                    }
                                    int RSSI_Value = 0;
                                    int Tx_Mbit_Value = 0;
                                    int Tx_MCS_Value = 0;
                                    int Rx_Mbit_Value = 0;
                                    int Rx_MCS_Value = 0;
                                    int Tx_CCQ_Value = 0;
                                    if (type == 1)//평균 출력
                                    {
                                        for (int count6 = 0; count6 < RSSI_List.size(); count6++) {
                                            RSSI_Value += RSSI_List.get(count6);
                                            Tx_Mbit_Value += Tx_Mbit_List.get(count6);
                                            Tx_MCS_Value += Tx_MCS_List.get(count6);
                                            Rx_Mbit_Value += Rx_Mbit_List.get(count6);
                                            Rx_MCS_Value += Rx_MCS_List.get(count6);
                                            Tx_CCQ_Value += Tx_CCQ_List.get(count6);
                                        }
                                        RSSI_Value /= RSSI_List.size();
                                        Tx_Mbit_Value /= RSSI_List.size();
                                        Tx_MCS_Value /= RSSI_List.size();
                                        Rx_Mbit_Value /= RSSI_List.size();
                                        Rx_MCS_Value /= RSSI_List.size();
                                        Tx_CCQ_Value /= RSSI_List.size();
                                    } else if (type == 2)//최대 출력
                                    {
                                        RSSI_Value = -1000;
                                        Tx_Mbit_Value = -1000;
                                        Tx_MCS_Value = -1000;
                                        Rx_Mbit_Value = -1000;
                                        Rx_MCS_Value = -1000;
                                        ;
                                        Tx_CCQ_Value = -1000;
                                        for (int count6 = 0; count6 < RSSI_List.size(); count6++) {
                                            if (RSSI_Value < RSSI_List.get(count6))
                                                RSSI_Value = RSSI_List.get(count6);
                                            if (Tx_Mbit_Value < Tx_Mbit_List.get(count6))
                                                Tx_Mbit_Value = Tx_Mbit_List.get(count6);
                                            if (Tx_MCS_Value < Tx_MCS_List.get(count6))
                                                Tx_MCS_Value = Tx_MCS_List.get(count6);
                                            if (Rx_Mbit_Value < Rx_Mbit_List.get(count6))
                                                Rx_Mbit_Value = Rx_Mbit_List.get(count6);
                                            if (Rx_MCS_Value < Rx_MCS_List.get(count6))
                                                Rx_MCS_Value = Rx_MCS_List.get(count6);
                                            if (Tx_CCQ_Value < Tx_CCQ_List.get(count6))
                                                Tx_CCQ_Value = Tx_CCQ_List.get(count6);
                                        }
                                    } else if (type == 3)//최소 출력
                                    {
                                        RSSI_Value = 1000;
                                        Tx_Mbit_Value = 1000;
                                        Tx_MCS_Value = 1000;
                                        Rx_Mbit_Value = 1000;
                                        Rx_MCS_Value = 1000;
                                        ;
                                        Tx_CCQ_Value = 1000;
                                        for (int count6 = 0; count6 < RSSI_List.size(); count6++) {
                                            if (RSSI_Value > RSSI_List.get(count6))
                                                RSSI_Value = RSSI_List.get(count6);
                                            if (Tx_Mbit_Value > Tx_Mbit_List.get(count6))
                                                Tx_Mbit_Value = Tx_Mbit_List.get(count6);
                                            if (Tx_MCS_Value > Tx_MCS_List.get(count6))
                                                Tx_MCS_Value = Tx_MCS_List.get(count6);
                                            if (Rx_Mbit_Value > Rx_Mbit_List.get(count6))
                                                Rx_Mbit_Value = Rx_Mbit_List.get(count6);
                                            if (Rx_MCS_Value > Rx_MCS_List.get(count6))
                                                Rx_MCS_Value = Rx_MCS_List.get(count6);
                                            if (Tx_CCQ_Value > Tx_CCQ_List.get(count6))
                                                Tx_CCQ_Value = Tx_CCQ_List.get(count6);
                                        }
                                    } else if (type == 4)//CCQ 최대 출력
                                    {
                                        Tx_CCQ_Value = -1000;
                                        for (int count6 = 0; count6 < RSSI_List.size(); count6++) {
                                            if (Tx_CCQ_Value < Tx_CCQ_List.get(count6)) {
                                                Tx_CCQ_Value = Tx_CCQ_List.get(count6);
                                                RSSI_Value = RSSI_List.get(count6);
                                                Tx_Mbit_Value = Tx_Mbit_List.get(count6);
                                                Tx_MCS_Value = Tx_MCS_List.get(count6);
                                                Rx_Mbit_Value = Rx_Mbit_List.get(count6);
                                                Rx_MCS_Value = Rx_MCS_List.get(count6);
                                            }
                                        }
                                    } else if (type == 5)//CCQ 최소 출력
                                    {
                                        RSSI_Value = 1000;
                                        Tx_Mbit_Value = 1000;
                                        Tx_MCS_Value = 1000;
                                        Rx_Mbit_Value = 1000;
                                        Rx_MCS_Value = 1000;
                                        ;
                                        Tx_CCQ_Value = 1000;
                                        for (int count6 = 0; count6 < RSSI_List.size(); count6++) {
                                            if (Tx_CCQ_Value > Tx_CCQ_List.get(count6)) {
                                                Tx_CCQ_Value = Tx_CCQ_List.get(count6);
                                                RSSI_Value = RSSI_List.get(count6);
                                                Tx_Mbit_Value = Tx_Mbit_List.get(count6);
                                                Tx_MCS_Value = Tx_MCS_List.get(count6);
                                                Rx_Mbit_Value = Rx_Mbit_List.get(count6);
                                                Rx_MCS_Value = Rx_MCS_List.get(count6);
                                            }
                                        }
                                    }
                                    Value = "," + RSSI_Value + " dBm," + Tx_Mbit_Value + " Mbit/s， MCS " + Tx_MCS_Value + "," + Rx_Mbit_Value + " Mbit/s， MCS " + Rx_MCS_Value + "," + Tx_CCQ_Value + "%\r\n";
                                    writer.write(Value);
                                }
                            }
                        }
                    }
                }
            }

            writer.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(PrintReportFrame.PrintReportFrame, "MySQL이 설치되지 않았거나\nMySQL 접근 ID, Password 혹은\n연결 주소 및 포트 등의\n정보가 정확하지 않습니다!", "MySQL 동작 에러", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public void catchFreshData() {
        ID = Main.mySqlID.getText();
        Password = Main.mySqlPassword.getText();
        Address = Main.mySqlAddress.getText();
        Port = Integer.parseInt(Main.mySqlPort.getText());

        try {
            String driverName = "com.mysql.jdbc.Driver"; // 드라이버 이름 지정
            String dbURL = "jdbc:mysql://" + Address + ":" + Port + "/"; // URL 지정
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, ID, Password);

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
            if (isExit == false) {
                JOptionPane.showMessageDialog(PrintReportFrame.PrintReportFrame, "현재 어떠한 데이터도 존재하지 않습니다!", "데이터 없음", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SQL = "use `sawwave_manager`;";
            stmt.execute(SQL);
            SQL = "show tables";
            result = stmt.executeQuery(SQL);
            isExit = false;
            String TableName = null;
            while (result.next())
                TableName = result.getString("Tables_in_sawwave_manager");
            if (TableName == null) {
                JOptionPane.showMessageDialog(PrintReportFrame.PrintReportFrame, "현재 어떠한 데이터도 존재하지 않습니다!", "데이터 없음", JOptionPane.ERROR_MESSAGE);
                return;
            }
            PrintReportFrame.TableName.setText(TableName);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(PrintReportFrame.PrintReportFrame, "MySQL이 설치되지 않았거나\nMySQL 접근 ID, Password 혹은\n연결 주소 및 포트 등의\n정보가 정확하지 않습니다!", "MySQL 동작 에러", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
}
