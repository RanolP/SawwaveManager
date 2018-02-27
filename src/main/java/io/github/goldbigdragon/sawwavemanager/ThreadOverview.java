package io.github.goldbigdragon.sawwavemanager;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ThreadOverview extends Thread {
    public void run() {
        String url = null;
        try {
            url = "http://" + Main.webIpClient.getText() + "/cgi-bin/luci/?" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(Main.webIdClient.getText(), "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(new String(Main.webPwClient.getPassword()), "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();
        }

        while (true) {
            if (!Main.started) {
                Function.exit(null);
                return;
            }
            if (!Main.truck.isReadyOverview()) {
                String[] splitted = null;
                try {
                    WebClient webClient = new WebClient(BrowserVersion.CHROME);
                    webClient.setJavaScriptTimeout(2000L);
                    WebRequest webRequest = new WebRequest(new URL(url));
                    webRequest.setCharset(StandardCharsets.UTF_8);
                    HtmlPage page = webClient.getPage(webRequest);
                    webClient.waitForBackgroundJavaScript(1500);
                    splitted = page.asText().split("Associated Stations");
                    page.cleanUp();
                    webClient.close();
                    page.cleanUp();
                } catch (IOException e2) {
                    System.out.println("네트워크 변동 감지. OverView 대기모드 전환.");
                } catch (FailingHttpStatusCodeException e3) {
                    System.out.println("세션 오류 감지. Network 대기모드 전환.");
                }
				/*
				catch(SocketTimeoutException e3)
				{
					System.out.println("네트워크 연결 실패. OverView 대기모드 전환.");
				}
				*/
                if (splitted != null) {
                    String Type = splitted[0].split("Overview")[0].split(" - ")[0];

                    String ResultText = splitted[1].split("System")[0].trim();
                    String[] Values = ResultText.split("\n")[2].split("\t");

                    if (Values.length >= 9) {
                        String RSSI = Values[5];
                        String CCQ = Values[8];
                        ResultText = ResultText.split("\n")[6];
                        String Tx_Rate = ResultText.split("\t")[0];
                        String Rx_Rate = ResultText.split("\t")[1];

                        Main.truck.setType(Type);
                        Main.truck.setRssi(RSSI);
                        Main.truck.setCcq(CCQ);
                        Main.truck.setTxRate(Tx_Rate);
                        Main.truck.setRxRate(Rx_Rate);
                        Main.truck.setReadyOverview(true);

                    } else {
                        System.out.println("OverView 못 구함");
                    }
                } else {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
