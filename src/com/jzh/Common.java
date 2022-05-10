package com.jzh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @version 1.0
 * @description 共同代码
 * @Author Jiang Zhihang
 * @Date 2022/5/9 22:51
 */
public class Common {
    /**
     * @description: 接收消息
     * @author Jiang Zhihang
     * @date 2022/5/9 22:59
     */
    public static void receiveMsg(BufferedReader br) {
        try {
            if (MessageWindow.newMsgFromOther == null) {
                String s = br.readLine();
                synchronized (MessageWindow.class) {
                    MessageWindow.newMsgFromOther = s;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @description: 发送消息
     * @author Jiang Zhihang
     * @date 2022/5/9 22:59
     */
    public static void sendMsg(BufferedWriter bw) {
        try {
            if (MessageWindow.mySendMsg != null && !MessageWindow.mySendMsg.equals("")) {
                bw.write(MessageWindow.mySendMsg);
                bw.flush();
                synchronized (MessageWindow.class) {
                    MessageWindow.mySendMsg = null;
                }
            }
            // 必须放到不同线程中（其他TimerTask），否则可能因为子进程br未读完父进程的bw写而产生错误
//                            if (MessageWindow.newMsgFromOther == null) {
//                                MessageWindow.newMsgFromOther = br.readLine();
//                            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @description: 结束任务，关闭资源，退出进程
     * @author Jiang Zhihang
     * @date 2022/5/9 22:59
     */
    public static void endAll(MessageWindow window, BufferedWriter bw, BufferedReader br, Timer timer1, Timer timer2) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!window.isVisible()) {
                    try {
                        timer1.cancel();
                        timer2.cancel();
                        bw.close();
                        br.close();
                        System.out.println("closed");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("over");
                    System.exit(0);
                }
            }
        }, 0, 1000);
    }
}
