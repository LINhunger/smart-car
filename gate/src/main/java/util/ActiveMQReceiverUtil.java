package util;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


/**
 * Created by 小排骨 on 2017/9/23.
 */
public class ActiveMQReceiverUtil {

    public static void main(String args[]) {
        queueReceiver("1");
    }

    /**
     * 接收消息
     *
     * @param quequName	队列名
     * @return
     */
    public static String queueReceiver(String quequName){
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://localhost:61616");//tcp地址
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // 消费者，消息接收者
        MessageConsumer consumer;
        String receiveMsg = "";
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(quequName);
            consumer = session.createConsumer(destination);
            while (true) {
                //设置接收者接收消息的时间，为了便于测试，这里为100s
                TextMessage message = (TextMessage) consumer.receive(100000*5);
                if (null != message) {
                    receiveMsg = message.getText();
                    System.out.println("收到消息:" + message.getText());

                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (Throwable ignore) {
            }
    }
        return receiveMsg;
    }

    /**
     * 重载方法，拉取消息队列发来的指令
     * @param quequName
     * @param ctx
     * @return
     */
    public static String queueReceiver(String quequName, ChannelHandlerContext ctx){
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://localhost:61616");//tcp地址
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // 消费者，消息接收者
        MessageConsumer consumer;
        String receiveMsg = "";
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(quequName);
            consumer = session.createConsumer(destination);
            //清空消息队列
            while(consumer.receive(100) != null);
            while (true) {
                //设置接收者接收消息的时间，为了便于测试，这里为100s
                TextMessage message = (TextMessage) consumer.receive(100000*5);
                if (null != message) {
                    receiveMsg = message.getText();
                    if (receiveMsg != null) {
                        ChannelFuture future =  ctx.writeAndFlush(Unpooled.copiedBuffer(receiveMsg, CharsetUtil.UTF_8));
                        future.get();
                        System.out.println("mq has send message >> :" + message.getText());
                    }

                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (Throwable ignore) {
            }
        }
        return receiveMsg;
    }
}