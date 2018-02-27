package io.github.goldbigdragon.sawwavemanager;

import javax.swing.*;
import java.awt.*;

public class PrintReportFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public static JTextField tableName = new JTextField("                                             ");
    public JButton freshData = new JButton("최신 자료");
    public JButton printAllValue = new JButton("상세값 출력");
    public JButton printAverageValue = new JButton("각 평균값 출력");
    public JButton printMaximumValue = new JButton("각 최대값 출력");
    public JButton printMinimumValue = new JButton("각 최소값 출력");
    public JButton printCcqMaximumValue = new JButton("CCQ 최대값 출력");
    public JButton printCcqMinimumValue = new JButton("CCQ 최소값 출력");


    public static JFrame printReportFrame;

    public void putBlankLabel(GridBagConstraints constraints, GridBagLayout layout, JPanel searchPanel) {
        JLabel blankLabel = new JLabel();
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(blankLabel, constraints);
        searchPanel.add(blankLabel);

    }

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
        FlowLayout fl = new FlowLayout();
        this.setLayout(fl);

        this.add(new JLabel("출력할 테이블 명 : "));
        this.add(tableName);
        this.add(freshData);
        JPanel ButtonPanel = new JPanel();
        ButtonPanel.add(printAllValue);
        ButtonPanel.add(printAverageValue);
        ButtonPanel.add(printMaximumValue);
        ButtonPanel.add(printMinimumValue);
        JPanel ButtonPanel2 = new JPanel();
        ButtonPanel2.add(printCcqMaximumValue);
        ButtonPanel2.add(printCcqMinimumValue);


        this.add(ButtonPanel);
        this.add(ButtonPanel2);
    }

    private void addListeners() {
        freshData.addActionListener(actionEvent -> MySQL.catchFreshData());
        printAllValue.addActionListener(actionEvent -> MySQL.exportData(0, tableName.getText()));
        printAverageValue.addActionListener(actionEvent -> MySQL.exportData(1, tableName.getText()));
        printMaximumValue.addActionListener(actionEvent -> MySQL.exportData(2, tableName.getText()));
        printMinimumValue.addActionListener(actionEvent -> MySQL.exportData(3, tableName.getText()));
        printCcqMaximumValue.addActionListener(actionEvent -> MySQL.exportData(4, tableName.getText()));
        printCcqMinimumValue.addActionListener(actionEvent -> MySQL.exportData(5, tableName.getText()));
    }
}
