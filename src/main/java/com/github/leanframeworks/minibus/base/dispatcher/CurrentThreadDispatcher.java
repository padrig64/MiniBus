/*
 * Copyright (c) 2017, LeanFrameworks
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.leanframeworks.minibus.base.dispatcher;

import com.github.leanframeworks.minibus.api.Event;
import com.github.leanframeworks.minibus.api.EventFilter;
import com.github.leanframeworks.minibus.api.EventHandler;
import com.github.leanframeworks.minibus.api.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Dispatcher strategy dispatching events on the current thread (whatever it may be when the {@link #dispatch(Event,
 * Map, Collection, Collection)} method is called).
 */
public class CurrentThreadDispatcher extends AbstractSerialDispatcher {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentThreadDispatcher.class);

    private final NestedDispatchStrategy nestedDispatchStrategy;

    public CurrentThreadDispatcher(NestedDispatchStrategy nestedDispatchStrategy) {
        this.nestedDispatchStrategy = nestedDispatchStrategy;
    }

    public NestedDispatchStrategy getNestedDispatchStrategy() {
        return nestedDispatchStrategy;
    }

    @Override
    public void dispatch(Event<Object> event, Map<EventHandler<Object>, EventFilter<Object>> eventHandlers,
                         Collection<EventHandler<Object>> undeliveredEventHandlers,
                         Collection<ExceptionHandler> exceptionHandlers) {
        if (getNestedDispatchCount() > 0) {
            // Already dispatching, so process event depending on defined strategy
            NestedDispatchStrategy nestedDispatchStrategy = getNestedDispatchStrategy();
            switch (nestedDispatchStrategy) {
                case PROCESS_IMMEDIATELY:
                    processEvent(event, eventHandlers, undeliveredEventHandlers, exceptionHandlers);
                    processQueue();
                    break;
                case QUEUE:
                    queueEvent(event, eventHandlers, undeliveredEventHandlers, exceptionHandlers);
                    break;
                default:
                    LOGGER.error("Unsupported nested dispatch strategy: {}", nestedDispatchStrategy);
            }
        } else {
            // Not dispatching, so process event now
            processEvent(event, eventHandlers, undeliveredEventHandlers, exceptionHandlers);
            processQueue();
        }
    }

    @Override
    public void dispose() {
        // Nothing to be done
    }

    public enum NestedDispatchStrategy {
        PROCESS_IMMEDIATELY,
        QUEUE
    }
}
