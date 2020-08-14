package gms.cuit.service;

import gms.cuit.entity.Gms_Order;
import gms.cuit.entity.Gms_User;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ发送邮件服务
 * @author story
 * @date 2020/5/23
 */
@Service
public class RabbitSenderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    /**
     * 订单成功异步发送邮件通知消息
     */
    public void sendOrderSuccessEmailMsg(Gms_Order gms_order){

        try {
            if (gms_order!=null){
                //TODO:rabbitmq发送消息的逻辑
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(env.getProperty("order.booking.success.email.exchange"));
                rabbitTemplate.setRoutingKey(env.getProperty("order.booking.success.email.routing.key"));
                //TODO：将info充当消息发送至队列
                //convertAndSend()  自动 Java 对象包装成 Message 对象，Java 对象需要实现 Serializable 序列化接口
                rabbitTemplate.convertAndSend(gms_order, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, Gms_Order.class);
                        return message;
                    }
                });
            }
        }catch (Exception e){
            System.out.println("邮件通知-发送消息-发生异常");
            e.printStackTrace();
        }
    }
    public void sendRegisterSuccessEmailMsg(Gms_User gms_user){

        try {
            if (gms_user!=null){
                //TODO:rabbitmq发送消息的逻辑
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(env.getProperty("register.success.email.exchange"));
                rabbitTemplate.setRoutingKey(env.getProperty("register.success.email.routing.key"));
                //TODO：将info充当消息发送至队列
                //convertAndSend()  自动 Java 对象包装成 Message 对象，Java 对象需要实现 Serializable 序列化接口
                rabbitTemplate.convertAndSend(gms_user, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, Gms_User.class);
                        return message;
                    }
                });
            }
        }catch (Exception e){
            System.out.println("邮件通知-发送消息-发生异常");
            e.printStackTrace();
        }
    }

}
