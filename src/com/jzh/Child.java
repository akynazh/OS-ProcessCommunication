package com.jzh;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @version 1.0
 * @description 子进程
 * @Author Jiang Zhihang
 * @Date 2022/5/5 19:29
 */
public class Child {
    public static void main(String[] args) {
        Child child = new Child();
        new MessageWindow("Child", 1);
        Timer timer1 = new Timer();
        Timer timer2 = new Timer();
        // 检测是否接收消息
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        if (MessageWindow.newMsgFromOther == null) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
                            MessageWindow.newMsgFromOther = br.readLine();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 500);
        // 检测是否发送消息
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        if (MessageWindow.mySendMsg != null && !MessageWindow.mySendMsg.equals("")) {
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
                            bw.write(MessageWindow.mySendMsg);
                            bw.flush();
                            MessageWindow.mySendMsg = null;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 500);
    }
}