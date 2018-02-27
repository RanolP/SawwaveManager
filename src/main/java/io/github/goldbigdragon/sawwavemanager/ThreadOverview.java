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
        String url;
        try {
            url = "http://" + Main.webIpClient.getText() + "/cgi-bin/luci/?" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(Main.webIdClient.getText(), "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(new String(Main.webPwClient.getPassword()), "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();
            return;
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
                    String type = splitted[0].split("Overview")[0].split(" - ")[0];

                    String resultText = splitted[1].split("System")[0].trim();
                    String[] values = resultText.split("\n")[2].split("\t");

                    if (values.length >= 9) {
                        String rssi = values[5];
                        String ccq = values[8];
                        resultText = resultText.split("\n")[6];
                        String txRate = resultText.split("\t")[0];
                        String rxRate = resultText.split("\t")[1];

                        Main.truck.setType(type);
                        Main.truck.setRssi(rssi);
                        Main.truck.setCcq(ccq);
                        Main.truck.setTxRate(txRate);
                        Main.truck.setRxRate(rxRate);
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
