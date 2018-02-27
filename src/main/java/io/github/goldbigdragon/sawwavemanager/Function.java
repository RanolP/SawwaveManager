package io.github.goldbigdragon.sawwavemanager;

import java.io.IOException;
import java.net.InetAddress;

public class Function extends Thread {
    static long count = 1;

    static void startResearch() {
        //검색 도중에 사용자가 옵션을 변경하지 못하도록 비활성화 시킴.
        Main.mySqlId.setEnabled(false);
        Main.mySqlPassword.setEnabled(false);
        Main.mySqlAddress.setEnabled(false);
        Main.mySqlPort.setEnabled(false);
        Main.pcIpClient.setEnabled(false);
        Main.pcIpServer.setEnabled(false);
        Main.webIpClient.setEnabled(false);
        Main.webIpServer.setEnabled(false);
        Main.webIdClient.setEnabled(false);
        Main.webIdServer.setEnabled(false);
        Main.webPwClient.setEnabled(false);
        Main.webPwServer.setEnabled(false);
        Main.distance.setEnabled(false);

        //검색 도중에 사용자가 검색을 중단 시킬 수 있도록 [검색]을 [중지]로 변경
        Main.startButton.setText("                                         중지                                         ");
        Main.information.setText("　　　　　　　　　　사이트 접속 여부 확인 중...　　　　　　　　　　　");

        //검색 시작
        Main.started = true;

        Main.information.setText("　　　　　　　　　　자료 수집 중...　　　　　　　　　　　");

        //쓰레드를 사용하여 검색합니다.
        Thread thread1 = new Thread_OverView();
        Thread thread2 = new Thread_Network();
        thread1.start();
        thread2.start();
    }

    public static boolean isPingWell() {
        try {
            return InetAddress.getByName(Main.pcIpServer.getText()).isReachable(2000);
        } catch (IOException e) {
            return false;
        }
    }


    static void exit(String message) {
        //[검색 중] 라벨에 검색이 완료되었다고 알림
        if (message == null) {
            Main.information.setText("　　　　　　　　　　　설정 이후, 시작 버튼을 클릭 해 주세요.　　　　　　　　　　　");
        } else {
            Main.information.setText(message);
        }

        //검색 완료 이후, 다시 사용자가 설정을 할 수 있도록 옵션 활성화
        Main.mySqlId.setEnabled(true);
        Main.mySqlPassword.setEnabled(true);
        Main.mySqlAddress.setEnabled(true);
        Main.mySqlPort.setEnabled(true);
        Main.pcIpClient.setEnabled(true);
        Main.pcIpServer.setEnabled(true);
        Main.webIpClient.setEnabled(true);
        Main.webIpServer.setEnabled(true);
        Main.webIdClient.setEnabled(true);
        Main.webIdServer.setEnabled(true);
        Main.webPwClient.setEnabled(true);
        Main.webPwServer.setEnabled(true);
        Main.distance.setEnabled(true);
        //검색 완료 이후, [중지]버튼을 다시 [검색]으로 변경
        Main.startButton.setText("                                         시작                                         ");
    }
}
