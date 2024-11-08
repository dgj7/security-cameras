package com.dg.securitycams.transcoder.config.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.function.TriConsumer;
import org.slf4j.Logger;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;

import java.util.Objects;
import java.util.function.Predicate;

@Slf4j
public class CallLogger extends InMemoryHttpExchangeRepository {
    private final Predicate<Logger> enabled;
    private final TriConsumer<Logger, String, Object[]> method;

    public CallLogger(final Predicate<Logger> pEnabled, final TriConsumer<Logger, String, Object[]> pLogger) {
        this.enabled = Objects.requireNonNull(pEnabled, "Predicate<Logger> is null");
        this.method = Objects.requireNonNull(pLogger, "TriConsumer<Logger, String, Object[]> is null");
    }

    @Override
    public void add(final HttpExchange he) {
        /* existing functionality preserved */
        super.add(he);

        /* log hits */
        if (enabled.test(log)) {
            method.accept(log, "[{}: {}] => [{}] ({}ms)", new Object[]{he.getRequest().getMethod(), he.getRequest().getUri(), he.getResponse().getStatus(), he.getTimeTaken().toMillis()});
        }
    }
}
