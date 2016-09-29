import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Thread_OverView extends Thread
{
	public void run ()
	{
		String url = null;
		try
		{
			url = "http://"+Main.Web_IP_Client.getText()+"/cgi-bin/luci/?"+URLEncoder.encode("username","UTF-8") + "=" + URLEncoder.encode(Main.Web_ID_Client.getText(), "UTF-8")+"&" +  URLEncoder.encode("password","UTF-8") + "=" + URLEncoder.encode(Main.Web_PW_Client.getText(), "UTF-8");
		}
		catch (UnsupportedEncodingException e1)
		{}
		
		while (true)
		{
			if(Main.started==false)
			{
				new Function().exit(null);
				return;
			}
			if(Main.Truck.isReady_OverView()==false)
			{
				String[] splitted = null;
				try
				{
					WebClient webClient = new WebClient(BrowserVersion.CHROME);
					webClient.setJavaScriptTimeout(2000L);
					WebRequest webRequest = new WebRequest(new URL(url));
					webRequest.setCharset("utf-8");
					HtmlPage page = webClient.getPage(webRequest);
					webClient.waitForBackgroundJavaScript(1500);
					splitted = page.asText().split("Associated Stations");
					page.cleanUp();
					webClient.close();
					page.cleanUp();
				}
				catch(IOException e2)
				{
					System.out.println("네트워크 변동 감지. OverView 대기모드 전환.");
				}
				catch(FailingHttpStatusCodeException e3)
				{
					System.out.println("세션 오류 감지. Network 대기모드 전환.");
				}
				/*
				catch(SocketTimeoutException e3)
				{
					System.out.println("네트워크 연결 실패. OverView 대기모드 전환.");
				}
				*/
				if(splitted!=null)
				{
					String Type = splitted[0].split("Overview")[0].split(" - ")[0];
					
					String ResultText = splitted[1].split("System")[0].trim();
					String[] Values = ResultText.split("\n")[2].split("\t");

					if(Values.length >= 9)
					{
						String RSSI = Values[5];
						String CCQ = Values[8];
						ResultText = ResultText.split("\n")[6];
						String Tx_Rate = ResultText.split("\t")[0];
						String Rx_Rate = ResultText.split("\t")[1];

						Main.Truck.setType(Type);
						Main.Truck.setRSSI(RSSI);
						Main.Truck.setCCQ(CCQ);
						Main.Truck.setTx_Rate(Tx_Rate);
						Main.Truck.setRx_Rate(Rx_Rate);
						Main.Truck.setReady_OverView(true);
						
					}
					else
						System.out.println("OverView 못 구함");
				}
				else
				{
					try {sleep(1000);} catch (InterruptedException e){e.printStackTrace();}
				}
					
			}
			else
			{
				try {sleep(1000);} catch (InterruptedException e){e.printStackTrace();}
			}
		}
	}
}
