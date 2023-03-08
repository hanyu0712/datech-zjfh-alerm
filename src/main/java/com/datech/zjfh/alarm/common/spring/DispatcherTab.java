package com.datech.zjfh.alarm.common.spring;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;

public class DispatcherTab extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {

        return String.valueOf(CoreConstants.TAB);
    }

}
