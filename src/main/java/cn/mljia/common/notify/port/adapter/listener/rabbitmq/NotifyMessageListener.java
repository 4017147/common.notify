package cn.mljia.common.notify.port.adapter.listener.rabbitmq;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.HibernateTransactionManager;

import cn.mljia.common.notify.application.NotifyMessageApplication;
import cn.mljia.common.notify.application.NotifyParam;
import cn.mljia.common.notify.application.command.CreateNotifyRecordCommand;
import cn.mljia.ddd.common.application.configuration.RabbitmqConfiguration;
import cn.mljia.ddd.common.event.ConsumedEventStore;
import cn.mljia.ddd.common.notification.NotificationReader;
import cn.mljia.ddd.common.port.adapter.messaging.rabbitmq.ExchangeListener;

public class NotifyMessageListener extends ExchangeListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotifyMessageListener.class);

	@Resource
	NotifyMessageApplication notifyMessageApplication;

	@Resource
	NotifyParam notifyParam;

	public NotifyMessageListener(RabbitmqConfiguration rabbitmqConfiguration,
			HibernateTransactionManager hibernateTransactionManager, ConsumedEventStore consumedEventStore) {
		super(rabbitmqConfiguration, hibernateTransactionManager, consumedEventStore);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String exchangeName() {
		// TODO Auto-generated method stub
		return "cn.mljia.shop.domain.event.OpenShopEvent";
	}

	@Override
	public void filteredDispatch(String aType, String aTextMessage) throws Exception {
		// TODO Auto-generated method stub
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("filteredDispatch type ===>>>" + aType + ",textMessage===========>>>" + aTextMessage);
		}
		NotificationReader reader = new NotificationReader(aTextMessage);
		long notificationId = reader.notificationId();
		String notifyId = UUID.randomUUID().toString();
		Integer maxNotifyTimes = notifyParam.getMaxNotifyTimes();
		Map<Integer, Integer> notifyParams = notifyParam.getNotifyParams();
		String url = "http://www.baidu.com";
		String notifyBody = "";
		notifyMessageApplication.createNotifyRecord(
				new CreateNotifyRecordCommand(notifyId, notifyParams, maxNotifyTimes, url, notifyBody));
	}

	@Override
	public String[] listensTo() {
		// TODO Auto-generated method stub
		return new String[] { "cn.mljia.shop.domain.event.OpenShopEvent" };
	}

}
