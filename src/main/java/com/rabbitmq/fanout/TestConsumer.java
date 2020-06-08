package com.rabbitmq.fanout;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.*;
import com.rabbitmq.util.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


//fanout是广播类型
public class TestConsumer {

    //交换机名称
    public final static String EXCHANGE_NAME = "fanout_exchange";


    public static void main(String[] args) throws IOException, TimeoutException {

        //消费者名称
        final String name = "consumer-" + RandomUtil.randomString(5);

        //判断该端口是否已经被使用
        RabbitMQUtil.checkServer();

        //创建链接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置RabbitMQ地址
        connectionFactory.setHost("localhost");
        //创建一个连接
        Connection connection = connectionFactory.newConnection();

        //创建一个通道
        Channel channel = connection.createChannel();

        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        //获取一个临时的队列
        String queue = channel.queueDeclare().getQueue();
        //队列与交换机绑定
        channel.queueBind(queue, EXCHANGE_NAME, " ");
        System.out.println(name + "等待接受消息");

        //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(name + " 接收到消息 '" + message + "'");
                // System.out.println(name+" ");
            }
        };
        //自动回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(queue, true, consumer);
    }

}
