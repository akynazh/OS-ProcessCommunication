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
        MessageWindow window = new MessageWindow("Child", 1);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
        Timer timer1 = new Timer();
        Timer timer2 = new Timer();
        // 检测是否接收消息
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                Common.receiveMsg(br);
            }
        }, 0, 500);
        // 检测是否发送消息
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                Common.sendMsg(bw);
            }
        }, 0, 500);
        // 结束任务，关闭资源，退出进程
        Common.endAll(window, bw, br, timer1, timer2);
    }
}