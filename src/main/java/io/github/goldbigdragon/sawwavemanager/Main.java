import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame
//JavaFrame����� ��� �޴´� �߱⿡, �������� GUI�� ������ �� �ְ� �ȴ�!
{
	private static final long serialVersionUID = 0001L;
	
	public static JTextField PC_IP_Client = new JTextField("192.168.1.202");
	public static JTextField PC_IP_Server = new JTextField("192.168.1.101");
	public static JTextField Web_IP_Client = new JTextField("192.168.1.253:8080");
	public static JTextField Web_IP_Server = new JTextField("192.168.1.254:8080");
	public static JCheckBox Sound = new JCheckBox();

	public static JTextField Web_ID_Client = new JTextField("admin");
	public static JPasswordField Web_PW_Client = new JPasswordField("sawwave12#$");
	public static JTextField Web_ID_Server = new JTextField("admin");
	public static JPasswordField Web_PW_Server = new JPasswordField("sawwave12#$");
	
	public static JTextField mySqlID = new JTextField("��������������������"); // MySQL ���� �̸� �Է� �ʵ�
	public static JPasswordField mySqlPassword = new JPasswordField("               ����������"); // MySQL ���� ��ȣ �Է� �ʵ�
	public static JTextField mySqlAddress = new JTextField("��������������������"); // MySQL �ּ� �Է� �ʵ�
	public static JTextField mySqlPort = new JTextField("��������������������"); // MySQL ��Ʈ �Է� �ʵ�

	public static JTextField Distance = new JTextField("1.00");
	public static JButton StartButton; // Start ��ư
	public static JButton PrintReport; // PrintReport ��ư

	public static final String[] LogList = null;
	
	public static JTable table; //���� ��Ȳ�� ��Ÿ���� �� ��
	
	public static boolean started; //���� ����
	
	public static JFrame SWM;
	public static JLabel Information = new JLabel("�������������������������� ����, ���� ��ư�� Ŭ�� �� �ּ���.����������������������"); // ���� ���¸� ��Ÿ���� ��.

	public static Object_Truck Truck = null;
	
	public static ArrayList<String> BufferStore = new ArrayList<String>();
	
	//SawWave Manager GUI���� ����� �ִ� �޼ҵ�
	public Main()
	{
		SWM = this;
		setTitle("SawWaveManager");
		setSize(550, 850);
		setBackground(new Color(255, 185, 0));
		setVisible(true);
		
		//GUI ȭ�鿡 ������Ʈ ��ġ
		setComponent();
		//���� �׼� �̺�Ʈ�� �߰���Ű�� �޼ҵ�
		setAction();
	}
	
	public void setComponent()
	{
		getContentPane().setLayout(new BorderLayout());
		JPanel SettingPane = new JPanel();
		
		JPanel SqlPanel = new JPanel();
		GridBagConstraints constraints = null;
		GridBagLayout layout = new GridBagLayout();
		SqlPanel.setBorder(BorderFactory.createTitledBorder("MySQL"));
		SqlPanel.setLayout(layout);
		SqlPanel.add(new JLabel("SQL ID : "));
		SqlPanel.add(mySqlID);
		SqlPanel.add(new JLabel("��SQL PW : "));
		SqlPanel.add(mySqlPassword);
		putBlankLabel(constraints, layout, SqlPanel);
		SqlPanel.add(new JLabel("SQL Address : "));
		SqlPanel.add(mySqlAddress);
		SqlPanel.add(new JLabel("��SQL Port :"));
		SqlPanel.add(mySqlPort);

		JPanel DistancePane = new JPanel();
		DistancePane.setBorder(BorderFactory.createTitledBorder("�Ÿ�"));
		DistancePane.setLayout(layout);
		DistancePane.add(new JLabel("�Ÿ� : "));
		DistancePane.add(Distance);
		DistancePane.add(new JLabel("Km"));
		
		JPanel ServerSide = new JPanel();
		ServerSide.setBorder(BorderFactory.createTitledBorder("Server ��"));
		ServerSide.setLayout(new GridBagLayout());
		ServerSide.add(new JLabel("��PC-IP : "));
		ServerSide.add(PC_IP_Server);
		ServerSide.add(new JLabel("��WEB-IP : "));
		ServerSide.add(Web_IP_Server);
		ServerSide.add(new JLabel("��ID : "));
		ServerSide.add(Web_ID_Server);
		ServerSide.add(new JLabel("��PW : "));
		ServerSide.add(Web_PW_Server);

		JPanel ClientSide = new JPanel();
		ClientSide.setBorder(BorderFactory.createTitledBorder("Clinet ��"));
		ClientSide.setLayout(new GridBagLayout());
		ClientSide.add(new JLabel("��PC-IP : "));
		ClientSide.add(PC_IP_Client);
		ClientSide.add(new JLabel("��WEB-IP : "));
		ClientSide.add(Web_IP_Client);
		ClientSide.add(new JLabel("��ID : "));
		ClientSide.add(Web_ID_Client);
		ClientSide.add(new JLabel("��PW : "));
		ClientSide.add(Web_PW_Client);

		SettingPane.add(SqlPanel);
		SettingPane.add(DistancePane);
		SettingPane.add(ServerSide);
		SettingPane.add(ClientSide);

		JPanel StartPane = new JPanel();
		StartButton = new JButton("                                         ����                                         ");
		StartPane.add(StartButton);
		PrintReport = new JButton("���� ���");
		StartPane.add(PrintReport);
		SettingPane.add(StartPane);
		
		JPanel MessageArea = new JPanel();
		MessageArea.setBorder(BorderFactory.createTitledBorder("��Ȳ �޽���"));
		MessageArea.add(Information);
		SettingPane.add(MessageArea);
		
		getContentPane().add(SettingPane, BorderLayout.CENTER);
		
		
		table = new JTable(new DefaultTableModel(new Object[][] {},
				new String[] { "ȸ��", "Signal", "Tx Rate", "Rx Rate", "TX-CCQ" }) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		JPanel matchesPanel = new JPanel();
		matchesPanel.setBorder(BorderFactory.createTitledBorder("���� ���"));
		matchesPanel.setLayout(new BorderLayout());
		JPanel soundPanel = new JPanel();
		soundPanel.add(new JLabel("���� �ȳ� : "));
		soundPanel.add(Sound);
		matchesPanel.add(soundPanel, BorderLayout.NORTH);
		matchesPanel.add(new JScrollPane(table), BorderLayout.SOUTH);
		
		getContentPane().add(matchesPanel, BorderLayout.SOUTH);
	}
	
	public void setAction()
	{
		//X��ư�� ������ ��������� �ϴ� ����
		addWindowListener(
		new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		StartButton.addActionListener(new ActionListener() {
			public
			void actionPerformed(ActionEvent e)
			{
				if(started==false)
				{
					new MySQL().MySqlSetting(Main.mySqlID.getText(), Main.mySqlPassword.getText(), Main.mySqlAddress.getText(), Integer.parseInt(Main.mySqlPort.getText()));
					//MySQL ��ġ ���� üũ
					if(new MySQL().MySqlInstallTest()==false || new MySQL().CreateDatabases()==false)
					{
						JOptionPane.showMessageDialog(Main.SWM, "MySQL�� ��ġ���� �ʾҰų�\nMySQL ���� ID, Password Ȥ��\n���� �ּ� �� ��Ʈ ����\n������ ��Ȯ���� �ʽ��ϴ�!", "MySQL ���� ����", JOptionPane.ERROR_MESSAGE);
						return;
					}
					Truck = new Object_Truck();
					new Function().ResearchStart();
				}
				else
				{
					started = false;
					new MySQL().dataInsert();
				}
			}
		});

		 PrintReport.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                new PrintReportFrame();
	            }
	        });
	}

	//�� ĭ ��� ���� ���� �� �� ����
	public void putBlankLabel(GridBagConstraints constraints, GridBagLayout layout, JPanel searchPanel)
	{
		JLabel blankLabel = new JLabel();
		constraints = new GridBagConstraints();
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(blankLabel, constraints);
		searchPanel.add(blankLabel);
	}
	
	//�ٿ��� �� ���� ���Ƽ� ���ڰ� ���� �� ���� �޼ҵ�
	public void putLabel(String LabelName, GridBagConstraints constraints, GridBagLayout layout, JPanel searchPanel, int insetsTop,  int insetsLeft, int insetsBottom, int insetsRight, int sort)
	{
		JLabel startLabel = new JLabel(LabelName);
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
		//��ġ ����. (��,����,�Ʒ�,�����ʿ� ���� �ȼ� ���� ����.)
		constraints.anchor = GridBagConstraints.EAST;
		//��ġ ���� ���� ���� ���� ����
		layout.setConstraints(startLabel, constraints);
		//���̾ƿ��� ��(startLabel)�� ������ ������ ��ġ(constraints)�� ���δ�.
		searchPanel.add(startLabel);
		//�гο� ��(startLabel�� ���δ�.
	}

	//�ٿ��� �� ������Ʈ�� ���Ƽ� ���ڰ� ���� ������Ʈ ���� �޼ҵ�
	public void putComponent(GridBagConstraints constraints, Component component, GridBagLayout layout, JPanel searchPanel, int insetsTop,  int insetsLeft, int insetsBottom, int insetsRight, int sort)
	{
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
		//��ġ ����. (��,����,�Ʒ�,�����ʿ� ���� �ȼ� ���� ����.)
		constraints.anchor = GridBagConstraints.EAST;
		//��ġ ���� ���� ���� ����
		layout.setConstraints(component, constraints);
		//���̾ƿ��� ��(startLabel)�� ������ ������ ��ġ(constraints)�� ���δ�.
		searchPanel.add(component);
		//�гο� ��(startLabel�� ���δ�.
	}
	
	public static void main(String[] args)
	{
		Main crawler = new Main();
		crawler.show();
		mySqlID.setText("root");
		mySqlPassword.setText("");
		mySqlAddress.setText("localhost");
		mySqlPort.setText("3306");
	}
}
