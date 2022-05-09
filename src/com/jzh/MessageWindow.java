package com.jzh;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @version 1.0
 * @description 消息窗口
 * @Author Jiang Zhihang
 * @Date 2022/5/5 21:53
 */
public class MessageWindow extends JFrame {
    // 该进程发送的消息
    public static String mySendMsg;
    // 其他进程的消息
    public static String newMsgFromOther;
    // 父进程创建子进程的标识
    public static boolean createTag;

    /**
     * @param name: 进程名称
     * @param type: 0: 父进程 1: 子进程
     * @author Jiang Zhihang
     * @date 2022/5/6 12:51
     */
    public MessageWindow(String name, int type) {
        // 写入发送消息的文本框
        JTextArea sendMsg = new JTextArea(1, 10);
        // 展示消息的文本框
        JTextArea showMsg = new JTextArea();
        showMsg.setLineWrap(true);// 激活自动换行功能
        showMsg.setWrapStyleWord(true);// 激活断行不断字功能
        showMsg.setBackground(Color.gray);
        showMsg.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        if (type == 1) {
            showMsg.setForeground(Color.green);
        } else {
            showMsg.setForeground(Color.blue);
        }

        // 发送消息的按钮
        JButton sendButton = new JButton("send");
        sendButton.addActionListener(e -> {
            synchronized (MessageWindow.class) {
                String msg = "[" + name + " ~]# " + sendMsg.getText() + "\n";
                MessageWindow.mySendMsg = msg;
                showMsg.append(msg);
                sendMsg.setText("");
            }
        });
        // 创建子进程的按钮
        JButton createButton = new JButton("create child");
        createButton.addActionListener(e -> {
            synchronized (MessageWindow.class) {
                MessageWindow.createTag = true;
                createButton.setVisible(false);
            }
        });

        // 操作框面板
        JPanel jPanel = new JPanel();
        // 给面板添加组件
        jPanel.setLayout(new BorderLayout());
        jPanel.add(sendMsg, BorderLayout.CENTER);
        jPanel.add(sendButton, BorderLayout.EAST);

        // 消息面板
        JScrollPane jScrollPane = new JScrollPane(showMsg);

        // 给窗口添加面板
        if (type == 0) {
            add(createButton, BorderLayout.NORTH);
        }
        add(jScrollPane, BorderLayout.CENTER);
        add(jPanel, BorderLayout.SOUTH);

        // 设置窗口属性
        setSize(500, 400);
        if (type == 0)
            setLocation(300, 300);
        else if (type == 1)
            setLocation(900, 300);
        setVisible(true);
        setTitle(name);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // 监听其他进程的消息
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (MessageWindow.class) {
                    if (MessageWindow.newMsgFromOther != null && !MessageWindow.newMsgFromOther.equals("")) {
                        showMsg.append(MessageWindow.newMsgFromOther + "\r\n");
                        MessageWindow.newMsgFromOther = null;
                    }
                }
            }
        }, 0, 10);
    }
}
