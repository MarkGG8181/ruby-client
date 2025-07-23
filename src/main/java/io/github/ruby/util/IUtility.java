package io.github.ruby.util;

import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface IUtility {
    Logger LOGGER = LogManager.getLogger("Ruby");
    EventBus BUS = new EventBus("Ruby");
}