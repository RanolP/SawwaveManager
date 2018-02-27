package io.github.goldbigdragon.sawwavemanager;

import javax.swing.*;
import java.awt.*;

public class PrintReportFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    static JTextField tableName = new JTextField("                                             ");
    private JButton freshData = new JButton("최신 자료");
    private JButton printAllValue = new JButton("상세값 출력");
    private JButton printAverageValue = new JButton("각 평균값 출력");
    private JButton printMaximumValue = new JButton("각 최대값 출력");
    private JButton printMinimumValue = new JButton("각 최소값 출력");
    private JButton printCcqMaximumValue = new JButton("CCQ 최대값 출력");
    private JButton printCcqMinimumValue = new JButton("CCQ 최소값 출력");

    static JFrame printReportFrame;

    PrintReportFrame() {
        printReportFrame = this;
        setTitle("보고서 출력");
        setSize(550, 180);
        setLocation(0, 120);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init();
        addListeners();
        setVisible(true);
    }

    private void init() {
        setLayout(new FlowLayout());

        add(new JLabel("출력할 테이블 명 : "));
        add(tableName);
        add(freshData);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(printAllValue);
        buttonPanel.add(printAverageValue);
        buttonPanel.add(printMaximumValue);
        buttonPanel.add(printMinimumValue);

        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.add(printCcqMaximumValue);
        buttonPanel2.add(printCcqMinimumValue);

        this.add(buttonPanel);
        this.add(buttonPanel2);
    }

    private void addListeners() {
        freshData.addActionListener(actionEvent -> MySql.catchFreshData());
        printAllValue.addActionListener(actionEvent -> MySql.exportData(0, tableName.getText()));
        printAverageValue.addActionListener(actionEvent -> MySql.exportData(1, tableName.getText()));
        printMaximumValue.addActionListener(actionEvent -> MySql.exportData(2, tableName.getText()));
        printMinimumValue.addActionListener(actionEvent -> MySql.exportData(3, tableName.getText()));
        printCcqMaximumValue.addActionListener(actionEvent -> MySql.exportData(4, tableName.getText()));
        printCcqMinimumValue.addActionListener(actionEvent -> MySql.exportData(5, tableName.getText()));
    }
}
