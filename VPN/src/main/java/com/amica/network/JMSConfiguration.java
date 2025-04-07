package com.amica.network;

import com.amica.esa.componentconfiguration.manager.ComponentConfigurationManager;
import com.amica.escm.configuration.api.Configuration;
import com.amica.escm.jmsspring.setup.ActiveMQConnectionFactoryBuilder;
import com.amica.escm.jmsspring.setup.JmsListenerContainerFactoryBuilder;
import com.amica.escm.jmsspring.setup.JmsTemplateBuilder;
import jakarta.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.support.converter.SimpleMessageConverter;

/**
 * Context configuration to support the MQ client component.
 *
 * @author Will Provost
 */
@org.springframework.context.annotation.Configuration
public class JMSConfiguration {

	public static final String CONFIGURATION_NAME = "MQ";
	public static String INTEGRATION_QUALIFIER = "VPN";
	public static String CLIENT_ID_PROP = INTEGRATION_QUALIFIER + ".CLIENT_ID";

	@Bean
	public Configuration configuration() {
		return ComponentConfigurationManager.getInstance()
				.getConfiguration(CONFIGURATION_NAME);
	}

	/**
	 * Creates a connection factory with the injected broker URL.
	 */
	@Bean
	public ConnectionFactory connectionFactory(Configuration configuration) {
		ActiveMQConnectionFactoryBuilder builder = 
				ActiveMQConnectionFactoryBuilder.instance();
    
		builder.configuration(configuration);
		builder.integrationQualifier(INTEGRATION_QUALIFIER);
		if (configuration.containsKey(CLIENT_ID_PROP)) {
			builder.clientId(configuration.getString(CLIENT_ID_PROP));
		}

		return builder.build();
	}

	/**
	 * Creates a JMS template with our connection-factory bean.
	 */
	@Bean
	public JmsTemplate jmsTemplate(Configuration configuration,
			ConnectionFactory connectionFactory) {
		return JmsTemplateBuilder.instance()
				.configuration(configuration)
				.integrationQualifier(INTEGRATION_QUALIFIER)
				.connectionFactory(connectionFactory)
				.build();
	}

	/**
	 * Creates a JMS template with our connection-factory bean.
	 */
	@Bean
	public JmsListenerContainerFactory<? extends MessageListenerContainer>
		jmsListenerContainerFactory(Configuration configuration,
			ConnectionFactory connectionFactory) {
		return JmsListenerContainerFactoryBuilder.instance()
				.configuration(configuration)
				.integrationQualifier(INTEGRATION_QUALIFIER)
				.connectionFactory(connectionFactory)
				.platformTransactionManager(new JmsTransactionManager(connectionFactory))
				.messageConverter(new SimpleMessageConverter())
				.build();
	}
}

