import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Function extends Thread
{
	public static long count = 1;
	public static String StartTime = null;
	
	public void ResearchStart()
	{
		//�˻� ���߿� ����ڰ� �ɼ��� �������� ���ϵ��� ��Ȱ��ȭ ��Ŵ.
		Main.mySqlID.setEnabled(false);
		Main.mySqlPassword.setEnabled(false);
		Main.mySqlAddress.setEnabled(false);
		Main.mySqlPort.setEnabled(false);
		Main.PC_IP_Client.setEnabled(false);
		Main.PC_IP_Server.setEnabled(false);
		Main.Web_IP_Client.setEnabled(false);
		Main.Web_IP_Server.setEnabled(false);
		Main.Web_ID_Client.setEnabled(false);
		Main.Web_ID_Server.setEnabled(false);
		Main.Web_PW_Client.setEnabled(false);
		Main.Web_PW_Server.setEnabled(false);
		Main.Distance.setEnabled(false);

		//�˻� ���߿� ����ڰ� �˻��� �ߴ� ��ų �� �ֵ��� [�˻�]�� [����]�� ����
		Main.StartButton.setText("                                         ����                                         ");
		Main.Information.setText("������������������������Ʈ ���� ���� Ȯ�� ��...����������������������");

		//�˻� ����
		Main.started = true;
		
		Main.Information.setText("���������������������ڷ� ���� ��...����������������������");

		StartTime = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date()).toString();
		//�����带 ����Ͽ� �˻��մϴ�.
		Thread thread1 = new Thread_OverView();
		Thread thread2 = new Thread_Network();
		thread1.start();
		thread2.start();
	}
	
	public boolean isPingWell()
	{
		try
		{
			return InetAddress.getByName(Main.PC_IP_Server.getText()).isReachable(2000);
		}
		catch (IOException e)
		{
			return false;
		}
	}
	
	
	public void exit(String Message)
	{
		
		//[�˻� ��] �󺧿� �˻��� �Ϸ�Ǿ��ٰ� �˸�
		if(Message == null)
			Main.Information.setText("�������������������������� ����, ���� ��ư�� Ŭ�� �� �ּ���.����������������������");
		else
			Main.Information.setText(Message);
			
		//�˻� �Ϸ� ����, �ٽ� ����ڰ� ������ �� �� �ֵ��� �ɼ� Ȱ��ȭ
		Main.mySqlID.setEnabled(true);
		Main.mySqlPassword.setEnabled(true);
		Main.mySqlAddress.setEnabled(true);
		Main.mySqlPort.setEnabled(true);
		Main.PC_IP_Client.setEnabled(true);
		Main.PC_IP_Server.setEnabled(true);
		Main.Web_IP_Client.setEnabled(true);
		Main.Web_IP_Server.setEnabled(true);
		Main.Web_ID_Client.setEnabled(true);
		Main.Web_ID_Server.setEnabled(true);
		Main.Web_PW_Client.setEnabled(true);
		Main.Web_PW_Server.setEnabled(true);
		Main.Distance.setEnabled(true);
		//�˻� �Ϸ� ����, [����]��ư�� �ٽ� [�˻�]���� ����
		Main.StartButton.setText("                                         ����                                         ");
	}
}
