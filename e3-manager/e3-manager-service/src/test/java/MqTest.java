import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/12/4 10:09
 */
public class MqTest {

    @Test
    public void sendMessage() throws Exception {
        // 手动初始化spring容器，tomcat所做的事情
        ApplicationContext applicationContext = new
                ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        // 从容器中手动获取需要的bean  自动注入@Autowire所做的事
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        Destination destination = (Destination) applicationContext.getBean("queueDestination");
        // 用jmsTemplate发送消息 参数 ：destination message
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("mq发送消息");
            }
        });
    }
}
