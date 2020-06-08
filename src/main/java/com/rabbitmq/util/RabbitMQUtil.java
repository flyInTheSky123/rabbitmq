package com.rabbitmq.util;

import cn.hutool.core.util.NetUtil;

import javax.swing.*;

public class RabbitMQUtil {
    public static void main(String[] args) {

        checkServer();
    }

    public static void checkServer(){
        if (NetUtil.isUsableLocalPort(15672)){
            JOptionPane.showMessageDialog(null,"RabbitMQ服务器没有启动");
            System.exit(1);
        }
    }
}
