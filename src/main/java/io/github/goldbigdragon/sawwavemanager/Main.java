package io.github.goldbigdragon.sawwavemanager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Main extends JFrame {
    private static final long serialVersionUID = 0001L;

    public static JTextField pcIpClient = new JTextField("192.168.1.202");
    public static JTextField pcIpServer = new JTextField("192.168.1.101");
    public static JTextField webIpClient = new JTextField("192.168.1.253:8080");
    public static JTextField webIpServer = new JTextField("192.168.1.254:8080");
    public static JCheckBox sound = new JCheckBox();

    public static JTextField webIdClient = new JTextField("admin");
    public static JPasswordField webPwClient = new JPasswordField("sawwave12#$");
    public static JTextField webIdServer = new JTextField("admin");
    public static JPasswordField webPwServer = new JPasswordField("sawwave12#$");

    public static JTextField mySqlId = new JTextField("　　　　　　　　　　"); // MySQL 유저 이름 입력 필드
    public static JPasswordField mySqlPassword = new JPasswordField("               　　　　　"); // MySQL 유저 암호 입력 필드
    public static JTextField mySqlAddress = new JTextField("　　　　　　　　　　"); // MySQL 주소 입력 필드
    public static JTextField mySqlPort = new JTextField("　　　　　　　　　　"); // MySQL 포트 입력 필드

    public static JTextField distance = new JTextField("1.00");
    public static JButton startButton; // Start 버튼
    public static JButton printReport; // printReport 버튼

    public static final String[] logList = null;

    public static JTable table; //현재 상황을 나타내어 줄 것

    public static boolean started; //시작 변수

    public static JFrame SWM;
    public static JLabel information = new JLabel("　　　　　　　　　　　설정 이후, 시작 버튼을 클릭 해 주세요.　　　　　　　　　　　"); // 현재 상태를 나타내는 바.

    public static ObjectTruck truck = null;

    public static ArrayList<String> BufferStore = new ArrayList<>();

    //SawWave Manager GUI판을 만들어 주는 메소드
    public Main() {
        SWM = this;
        setTitle("SawWaveManager");
        setSize(550, 850);
        setBackground(new Color(255, 185, 0));
        setVisible(true);

        //GUI 화면에 컴포넌트 배치
        setComponent();
        //각종 액션 이벤트를 추가시키는 메소드
        setAction();
    }

    public void setComponent() {
        getContentPane().setLayout(new BorderLayout());
        JPanel SettingPane = new JPanel();

        JPanel SqlPanel = new JPanel();
        GridBagConstraints constraints = null;
        GridBagLayout layout = new GridBagLayout();
        SqlPanel.setBorder(BorderFactory.createTitledBorder("MySQL"));
        SqlPanel.setLayout(layout);
        SqlPanel.add(new JLabel("SQL ID : "));
        SqlPanel.add(mySqlId);
        SqlPanel.add(new JLabel("　SQL PW : "));
        SqlPanel.add(mySqlPassword);
        putBlankLabel(constraints, layout, SqlPanel);
        SqlPanel.add(new JLabel("SQL Address : "));
        SqlPanel.add(mySqlAddress);
        SqlPanel.add(new JLabel("　SQL Port :"));
        SqlPanel.add(mySqlPort);

        JPanel DistancePane = new JPanel();
        DistancePane.setBorder(BorderFactory.createTitledBorder("거리"));
        DistancePane.setLayout(layout);
        DistancePane.add(new JLabel("거리 : "));
        DistancePane.add(distance);
        DistancePane.add(new JLabel("Km"));

        JPanel ServerSide = new JPanel();
        ServerSide.setBorder(BorderFactory.createTitledBorder("Server 측"));
        ServerSide.setLayout(new GridBagLayout());
        ServerSide.add(new JLabel("　PC-IP : "));
        ServerSide.add(pcIpServer);
        ServerSide.add(new JLabel("　WEB-IP : "));
        ServerSide.add(webIpServer);
        ServerSide.add(new JLabel("　ID : "));
        ServerSide.add(webIdServer);
        ServerSide.add(new JLabel("　PW : "));
        ServerSide.add(webPwServer);

        JPanel ClientSide = new JPanel();
        ClientSide.setBorder(BorderFactory.createTitledBorder("Clinet 측"));
        ClientSide.setLayout(new GridBagLayout());
        ClientSide.add(new JLabel("　PC-IP : "));
        ClientSide.add(pcIpClient);
        ClientSide.add(new JLabel("　WEB-IP : "));
        ClientSide.add(webIpClient);
        ClientSide.add(new JLabel("　ID : "));
        ClientSide.add(webIdClient);
        ClientSide.add(new JLabel("　PW : "));
        ClientSide.add(webPwClient);

        SettingPane.add(SqlPanel);
        SettingPane.add(DistancePane);
        SettingPane.add(ServerSide);
        SettingPane.add(ClientSide);

        JPanel StartPane = new JPanel();
        startButton = new JButton("                                         시작                                         ");
        StartPane.add(startButton);
        printReport = new JButton("보고서 출력");
        StartPane.add(printReport);
        SettingPane.add(StartPane);

        JPanel MessageArea = new JPanel();
        MessageArea.setBorder(BorderFactory.createTitledBorder("상황 메시지"));
        MessageArea.add(information);
        SettingPane.add(MessageArea);

        getContentPane().add(SettingPane, BorderLayout.CENTER);


        table = new JTable(new DefaultTableModel(new Object[][]{},
                new String[]{"회차", "Signal", "Tx Rate", "Rx Rate", "TX-CCQ"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        JPanel matchesPanel = new JPanel();
        matchesPanel.setBorder(BorderFactory.createTitledBorder("측정 결과"));
        matchesPanel.setLayout(new BorderLayout());
        JPanel soundPanel = new JPanel();
        soundPanel.add(new JLabel("음성 안내 : "));
        soundPanel.add(sound);
        matchesPanel.add(soundPanel, BorderLayout.NORTH);
        matchesPanel.add(new JScrollPane(table), BorderLayout.SOUTH);

        getContentPane().add(matchesPanel, BorderLayout.SOUTH);
    }

    public void setAction() {
        //X버튼을 누르면 사라지도록 하는 설정
        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
        startButton.addActionListener(e -> {
            if (!started) {
                MySql.MySqlSetting(Main.mySqlId.getText(), Main.mySqlPassword.getText(), Main.mySqlAddress.getText(), Integer.parseInt(Main.mySqlPort.getText()));
                //MySQL 설치 유무 체크
                if (!MySql.MySqlInstallTest() || !MySql.createDatabases()) {
                    JOptionPane.showMessageDialog(Main.SWM, "MySQL이 설치되지 않았거나\nMySQL 접근 ID, Password 혹은\n연결 주소 및 포트 등의\n정보가 정확하지 않습니다!", "MySQL 동작 에러", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                truck = new ObjectTruck();
                Function.startResearch();
            } else {
                started = false;
                MySql.dataInsert();
            }
        });

        printReport.addActionListener(actionEvent -> new PrintReportFrame());
    }

    //한 칸 띄워 쓰기 위한 빈 라벨 생성
    public void putBlankLabel(GridBagConstraints constraints, GridBagLayout layout, JPanel searchPanel) {
        JLabel blankLabel = new JLabel();
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(blankLabel, constraints);
        searchPanel.add(blankLabel);
    }

    //붙여야 할 라벨이 많아서 필자가 만든 라벨 생성 메소드
    public void putLabel(String LabelName, GridBagConstraints constraints, GridBagLayout layout, JPanel searchPanel, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight, int sort) {
        JLabel startLabel = new JLabel(LabelName);
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        //위치 설정. (위,왼쪽,아래,오른쪽에 대한 픽셀 띄우기 설정.)
        constraints.anchor = GridBagConstraints.EAST;
        //위치 설정 이후 우측 정렬 설정
        layout.setConstraints(startLabel, constraints);
        //레이아웃에 라벨(startLabel)을 위에서 설정한 위치(constraints)에 붙인다.
        searchPanel.add(startLabel);
        //패널에 라벨(startLabel을 붙인다.
    }

    //붙여야 할 컴포넌트가 많아서 필자가 만든 컴포넌트 생성 메소드
    public void putComponent(GridBagConstraints constraints, Component component, GridBagLayout layout, JPanel searchPanel, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight, int sort) {
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        //위치 설정. (위,왼쪽,아래,오른쪽에 대한 픽셀 띄우기 설정.)
        constraints.anchor = GridBagConstraints.EAST;
        //위치 설정 이후 우측 정렬
        layout.setConstraints(component, constraints);
        //레이아웃에 라벨(startLabel)을 위에서 설정한 위치(constraints)에 붙인다.
        searchPanel.add(component);
        //패널에 라벨(startLabel을 붙인다.
    }

    public static void main(String[] args) {
        Main crawler = new Main();
        crawler.setVisible(true);
        mySqlId.setText("root");
        mySqlPassword.setText("");
        mySqlAddress.setText("localhost");
        mySqlPort.setText("3306");
    }
}
