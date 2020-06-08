package com.rabbitmq.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.util.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class TestProducer {

    private static final String QUEUE_NAME="direct_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //判断该端口是否已经占用
        RabbitMQUtil.checkServer();

        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置分割成地址
        connectionFactory.setHost("localhost");

        //创建连接
        Connection connection = connectionFactory.newConnection();

        //创建连接通道
        Channel channel = connection.createChannel();

       for (int i=0;i<100;i++){
           String message="MQ-direct-消息"+i;

           channel.basicPublish("",QUEUE_NAME,null, message.getBytes("UTF-8"));
           System.out.println("发送消息："+message);

        }
       channel.close();
       connection.close();

    }



}
