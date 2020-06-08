package com.rabbitmq.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.util.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//fanout是广播类型
public class TestProducer {

    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        //检测默认端口是否能启动
        RabbitMQUtil.checkServer();

        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置RabbitMQ相关信息
        connectionFactory.setHost("localhost");
        //创建连接
        Connection connection = connectionFactory.newConnection();

        //创建一个通道
        Channel channel = connection.createChannel();
        //交换机声明（交换机名称：交换机类型模式）
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        for (int i=0;i<100;i++){
            String message=" MQ 消息："+i;

            //发送消息到队列中
            //
            channel.basicPublish(EXCHANGE_NAME," ",null,message.getBytes("UTF-8"));
            System.out.println("发送消息到队列中："+message);


        }
        channel.close();
        connection.close();


    }


}
