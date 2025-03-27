package com.amica.network;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnection;
import jakarta.jms.TopicConnectionFactory;
import jakarta.jms.TopicSession;
import jakarta.jms.TopicSubscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * JMS subscriber that accumulates messages published to a target topic,
 * and can report on the receipt or non-receipt of a message for a 
 * specific party ID and action type.
 *
 * @author Will Provost
 */
@Component
@Profile("test")
public class Subscriber {

	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Value("${VPN.TOPIC}")
	private String topic;
	
	private TopicConnection conn;
	private TopicSession session;
	
	@Getter
	private List<Message> received = new ArrayList<>();
	
	/**
	 * Use the injected JMS connection factory to start a topic session
	 * and to assign {@link #onMessage onMessage} as a subscriber.
	 */
	@PostConstruct
	public void open() {
		try {
			conn = ((TopicConnectionFactory) connectionFactory)
					.createTopicConnection();
			session = conn.createTopicSession(false,  Session.AUTO_ACKNOWLEDGE);
			Topic jmsTopic = session.createTopic(topic);
			TopicSubscriber subscriber = session.createSubscriber(jmsTopic);
			subscriber.setMessageListener(this::onMessage);
			conn.start();
		} catch (Exception ex) {
			fail(ex);
		}
	}
	
	/**
	 * Close JMS session and connection.
	 */
	@PreDestroy
	public void close() {
		try {
			session.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Clear the accumulated messages.
	 */
	public synchronized void reset() {
		received.clear();
	}
	public void onMessage(Message message) {
		synchronized(this) {
			received.add(message);
		}
	}
}
