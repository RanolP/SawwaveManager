package io.github.goldbigdragon.sawwavemanager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {
    private static final long serialVersionUID = 0001L;

    static JTextField pcIpClient = new JTextField("192.168.1.202");
    static JTextField pcIpServer = new JTextField("192.168.1.101");
    static JTextField webIpClient = new JTextField("192.168.1.253:8080");
    static JTextField webIpServer = new JTextField("192.168.1.254:8080");
    static JCheckBox sound = new JCheckBox();

    static JTextField webIdClient = new JTextField("admin");
    static JPasswordField webPwClient = new JPasswordField("sawwave12#$");
    static JTextField webIdServer = new JTextField("admin");
    static JPasswordField webPwServer = new JPasswordField("sawwave12#$");

    static JTextField mySqlId = new JTextField("　　　　　　　　　　"); // MySQL 유저 이름 입력 필드
    static JPasswordField mySqlPassword = new JPasswordField("               　　　　　"); // MySQL 유저 암호 입력 필드
    static JTextField mySqlAddress = new JTextField("　　　　　　　　　　"); // MySQL 주소 입력 필드
    static JTextField mySqlPort = new JTextField("　　　　　　　　　　"); // MySQL 포트 입력 필드

    static JTextField distance = new JTextField("1.00");
    static JButton startButton; // Start 버튼
    private static JButton printReport; // printReport 버튼

    static JTable table; //현재 상황을 나타내어 줄 것

    static boolean started; //시작 변수

    private static JFrame frame;
    static JLabel information = new JLabel("　　　　　　　　　　　설정 이후, 시작 버튼을 클릭 해 주세요.　　　　　　　　　　　"); // 현재 상태를 나타내는 바.

    static ObjectTruck truck = null;

    static List<String> bufferStore = new ArrayList<>();

    //SawWave Manager GUI판을 만들어 주는 메소드
    private Main() {
        frame = this;
        setTitle("SawWaveManager");
        setSize(550, 850);
        setBackground(new Color(255, 185, 0));
        setVisible(true);

        //GUI 화면에 컴포넌트 배치
        setComponent();
        //각종 액션 이벤트를 추가시키는 메소드
        setAction();
    }

    private void setComponent() {
        getContentPane().setLayout(new BorderLayout());
        JPanel settingPane = new JPanel();

        JPanel sqlPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        sqlPanel.setBorder(BorderFactory.createTitledBorder("MySQL"));
        sqlPanel.setLayout(layout);
        sqlPanel.add(new JLabel("SQL ID : "));
        sqlPanel.add(mySqlId);
        sqlPanel.add(new JLabel("　SQL PW : "));
        sqlPanel.add(mySqlPassword);
        putBlankLabel(layout, sqlPanel);
        sqlPanel.add(new JLabel("SQL Address : "));
        sqlPanel.add(mySqlAddress);
        sqlPanel.add(new JLabel("　SQL Port :"));
        sqlPanel.add(mySqlPort);

        JPanel distancePane = new JPanel();
        distancePane.setBorder(BorderFactory.createTitledBorder("거리"));
        distancePane.setLayout(layout);
        distancePane.add(new JLabel("거리 : "));
        distancePane.add(distance);
        distancePane.add(new JLabel("Km"));

        JPanel serverSide = new JPanel();
        serverSide.setBorder(BorderFactory.createTitledBorder("Server 측"));
        serverSide.setLayout(new GridBagLayout());
        serverSide.add(new JLabel("　PC-IP : "));
        serverSide.add(pcIpServer);
        serverSide.add(new JLabel("　WEB-IP : "));
        serverSide.add(webIpServer);
        serverSide.add(new JLabel("　ID : "));
        serverSide.add(webIdServer);
        serverSide.add(new JLabel("　PW : "));
        serverSide.add(webPwServer);

        JPanel clientSide = new JPanel();
        clientSide.setBorder(BorderFactory.createTitledBorder("Clinet 측"));
        clientSide.setLayout(new GridBagLayout());
        clientSide.add(new JLabel("　PC-IP : "));
        clientSide.add(pcIpClient);
        clientSide.add(new JLabel("　WEB-IP : "));
        clientSide.add(webIpClient);
        clientSide.add(new JLabel("　ID : "));
        clientSide.add(webIdClient);
        clientSide.add(new JLabel("　PW : "));
        clientSide.add(webPwClient);

        settingPane.add(sqlPanel);
        settingPane.add(distancePane);
        settingPane.add(serverSide);
        settingPane.add(clientSide);

        JPanel startPane = new JPanel();
        startButton = new JButton("                                         시작                                         ");
        startPane.add(startButton);
        printReport = new JButton("보고서 출력");
        startPane.add(printReport);
        settingPane.add(startPane);

        JPanel MessageArea = new JPanel();
        MessageArea.setBorder(BorderFactory.createTitledBorder("상황 메시지"));
        MessageArea.add(information);
        settingPane.add(MessageArea);

        getContentPane().add(settingPane, BorderLayout.CENTER);


        table = new JTable(new DefaultTableModel(new Object[0][0], new String[]{"회차", "Signal", "Tx Rate", "Rx Rate", "TX-CCQ"}) {
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

    private void setAction() {
        //X버튼을 누르면 사라지도록 하는 설정
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        startButton.addActionListener(e -> {
            if (!started) {
                MySql.MySqlSetting(Main.mySqlId.getText(), Main.mySqlPassword.getText(), Main.mySqlAddress.getText(), Integer.parseInt(Main.mySqlPort.getText()));
                //MySQL 설치 유무 체크
                if (!MySql.MySqlInstallTest() || !MySql.createDatabases()) {
                    JOptionPane.showMessageDialog(Main.frame, "MySQL이 설치되지 않았거나\nMySQL 접근 ID, Password 혹은\n연결 주소 및 포트 등의\n정보가 정확하지 않습니다!", "MySQL 동작 에러", JOptionPane.ERROR_MESSAGE);
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
    private void putBlankLabel(GridBagLayout layout, JPanel searchPanel) {
        JLabel blankLabel = new JLabel();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(blankLabel, constraints);
        searchPanel.add(blankLabel);
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
