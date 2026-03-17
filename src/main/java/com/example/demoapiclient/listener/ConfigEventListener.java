package com.example.demoapiclient.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.bus.event.RefreshRemoteApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ConfigEventListener {

	private static final Logger log = LoggerFactory.getLogger(ConfigEventListener.class);

	@EventListener
	public void onRefreshEvent(RefreshRemoteApplicationEvent event) {
		log.info("Hello World! Received config refresh event from: {}", event.getOriginService());
	}

}
