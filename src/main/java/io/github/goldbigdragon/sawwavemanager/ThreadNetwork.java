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

public class ThreadNetwork extends Thread {
    public void run() {
        String url = null;
        try {
            url = "http://192.168.1.253:8080/cgi-bin/luci/admin/network/wireless/wifi0.network1?" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode("admin", "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode("sawwave12#$", "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();
        }

        while (true) {
            if (!Main.started) {
                new Function().exit(null);
                return;
            }
            if (!Main.truck.isReadyNetwork()) {
                String ResultText = null;
                try {
                    WebClient webClient = new WebClient(BrowserVersion.CHROME);
                    webClient.setJavaScriptTimeout(2000L);
                    WebRequest webRequest = new WebRequest(new URL(url));
                    webRequest.setCharset(StandardCharsets.UTF_8);
                    HtmlPage page = webClient.getPage(webRequest);
                    webClient.waitForBackgroundJavaScript(1500);

                    ResultText = page.asText().split("Width")[1];
                    page.cleanUp();
                    webClient.close();
                } catch (IOException e2) {
                    System.out.println("네트워크 변동 감지. Network 대기모드 전환.");
                } catch (FailingHttpStatusCodeException e3) {
                    System.out.println("세션 오류 감지. Network 대기모드 전환.");
                }
                if (ResultText != null) {
                    String[] Result = ResultText.split("Antenna")[0].trim().split("\n");
                    if (Result.length >= 5) {
                        String Bandwidth = ResultText.split("Antenna")[0].trim().split("\n")[0].trim();
                        String Channel = ResultText.split("Antenna")[0].trim().split("\n")[4].trim();
                        String Tx_Power = ResultText.split("Antenna")[1].trim().split("\n")[5].trim();

                        Main.truck.setBandwidth(Bandwidth);
                        Main.truck.setChannel(Channel);
                        Main.truck.setTxPower(Tx_Power);
                        Main.truck.setReadyNetwork(true);

                    } else
                        System.out.println("Network 못 구함");
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
