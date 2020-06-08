package com.rabbitmq.topic;

import com.rabbitmq.client.*;
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
//ConsumerNews 是想接收 新闻 消息

public class ConsumerNews {

    private static final String EXCHANGE_NAME = "topics_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {

        final String name = "consumer-news";

        RabbitMQUtil.checkServer();

        //
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queue = channel.queueDeclare().getQueue();

        channel.queueBind(queue, EXCHANGE_NAME, "*.news");

        System.out.println(name + "在等待接受消息");

        DefaultConsumer consumer = new DefaultConsumer(channel) {


            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                String message = new String(body, "UTF-8");

                System.out.println(name + "接受到消息：" + message + " ");


            }
        };

        //自动回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(queue, true, consumer);


    }


}
