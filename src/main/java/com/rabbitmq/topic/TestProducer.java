package com.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.util.RabbitMQUtil;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
topic类型：
 比如：消息来源有：重庆新闻，重庆天气，北京新闻，北京天气
 当我想获取 天气  主题时：就会接收到 重庆天气，北京天气
 当我想获取 重庆  主题时：就会接收到 重庆天气，重庆新闻
 当我想获取 新闻  主题时：就会接收到 北京新闻，重庆新闻
 .....
 */

//topic 生产者
public class TestProducer {

    private static final String EXCHANGE_NAME = "topics_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        //检测该端口是否已经被占用
        RabbitMQUtil.checkServer();

        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");


        //创建连接
        Connection connection = connectionFactory.newConnection();

        //创建一个连接新的通道
        Channel channel = connection.createChannel();

        //交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

//        这里的 routing_keys - messages是对应的关系。

        String[] routing_keys = new String[]{"ChongQing.news", "ChongQing.weather",
                "BeiJing.news", "BeiJing.weather"};
        String[] messages = new String[]{" 重庆新闻", "重庆天气", "北京新闻", "北京天气"};

        for (int i = 0; i < routing_keys.length; i++) {
            String routingKey = routing_keys[i];
            String message = messages[i];

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.printf("发送消息到路由：%s ,消息是：%s%n", routingKey, message);

        }

        channel.close();
        connection.close();

    }


}
