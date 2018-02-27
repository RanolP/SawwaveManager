import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class PrintReportFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JSplitPane JSP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, null, null);
	public static JTextField TableName = new JTextField("                                             ");
	public JButton FreshData = new JButton("�ֽ� �ڷ�");
	public JButton AllValuePrint = new JButton("�󼼰� ���");
	public JButton AverageValuePrint = new JButton("�� ��հ� ���");
	public JButton MaxValuePrint = new JButton("�� �ִ밪 ���");
	public JButton MinValuePrint = new JButton("�� �ּҰ� ���");
	public JButton MaxValue_CCQ_Print = new JButton("CCQ �ִ밪 ���");
	public JButton MinValue_CCQ_Print = new JButton("CCQ �ּҰ� ���");
	

	public static JFrame PrintReportFrame;
	
	public void putBlankLabel(GridBagConstraints constraints, GridBagLayout layout, JPanel searchPanel)
	{
		JLabel blankLabel = new JLabel();
		constraints = new GridBagConstraints();
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(blankLabel, constraints);
		searchPanel.add(blankLabel);
		
	}
	
	public PrintReportFrame()
	{
		PrintReportFrame = this;
        setTitle("���� ���");
        setSize(550, 180);
        setLocation(0, 120);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init();
        setting();
		setVisible(true);
    }
	
	public void init()
	{
		FlowLayout fl = new FlowLayout();
		this.setLayout(fl);

		this.add(new JLabel("����� ���̺� �� : "));
		this.add(TableName);
		this.add(FreshData);
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.add(AllValuePrint);
		ButtonPanel.add(AverageValuePrint);
		ButtonPanel.add(MaxValuePrint);
		ButtonPanel.add(MinValuePrint);
		JPanel ButtonPanel2 = new JPanel();
		ButtonPanel2.add(MaxValue_CCQ_Print);
		ButtonPanel2.add(MinValue_CCQ_Print);
		
		
		this.add(ButtonPanel);
		this.add(ButtonPanel2);
	}
	
	public void setting()
	{
		FreshData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new MySQL().catchFreshData();
            }
        });
		AllValuePrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new MySQL().exportData(0, TableName.getText());
            }
        });
		AverageValuePrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new MySQL().exportData(1, TableName.getText());
            }
        });
		MaxValuePrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new MySQL().exportData(2, TableName.getText());
            }
        });
		MinValuePrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new MySQL().exportData(3, TableName.getText());
            }
        });
		MaxValue_CCQ_Print.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new MySQL().exportData(4, TableName.getText());
            }
        });
		MinValue_CCQ_Print.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new MySQL().exportData(5, TableName.getText());
            }
        });
	}
}
