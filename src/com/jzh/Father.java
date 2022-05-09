package com.jzh;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @version 1.0
 * @description 父进程
 * @Author Jiang Zhihang
 * @Date 2022/5/5 19:21
 */
public class Father {
    public static void main(String[] args) throws IOException {
        Father father = new Father();
        MessageWindow window = new MessageWindow("Father", 0);
        BufferedWriter bw;
        BufferedReader br;
        Timer timer1 = new Timer();
        Timer timer2 = new Timer();
        // 检测是否创建子进程
        while (true) {
            synchronized (MessageWindow.class) {
                if (MessageWindow.createTag) {
                    father.createChildProcess();
                    bw = new BufferedWriter(new OutputStreamWriter(father.getChild().getOutputStream(), StandardCharsets.UTF_8));
                    br = new BufferedReader(new InputStreamReader(father.getChild().getInputStream(), StandardCharsets.UTF_8));
                    break;
                }
                if (!window.isVisible()) {
                    System.exit(0);
                }
            }
        }
        // 检测是否发送消息
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                if (father.getChild() != null) {
                    Common.receiveMsg(bw);
                }
            }
        }, 0, 500);
        // 检测是否接收消息
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                if (father.getChild() != null) {
                    Common.sendMsg(br);
                }
            }
        }, 0, 500);
        // 结束任务，关闭资源，退出进程
        Common.endAll(window, bw, br, timer1, timer2);
    }


    // 创建的子进程
    private Process child;

    public Process getChild() {
        return child;
    }

    /**
     * @description: 创建子进程
     * @author Jiang Zhihang
     * @date 2022/5/5 21:52
     */
    public void createChildProcess() throws IOException {
        Runtime run = Runtime.getRuntime();
        // 获取java执行命令所在的位置
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        // 获取类加载的时候的加载的class文件的路径,返回结果是一个string,多个路径之间用";"分割.
        String cp = "\"" + System.getProperty("java.class.path");
        // 获取当前程序的class文件所在的位置
        cp += File.pathSeparator + ClassLoader.getSystemResource("").getPath() + "\"";
        // java -cp 命令   (这是运行class文件的命令,会将class文件载入jvm)
        String cmd = java + " -cp " + cp + " com.jzh.Child";
        // 开启一个子进程
        child = run.exec(cmd);
    }
}

