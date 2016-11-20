package org.libex.test.jms.validation.capture;

import lombok.Getter;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

@Getter
public class JmsExchangeCapturer<T> implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(JmsExchangeCapturer.class);

    private final ImmutableMap<String, ? extends T> inputMap;
    private final Multimap<T, Exchange> exchangeList = ArrayListMultimap.create();

    public JmsExchangeCapturer(
            final Function<? super T, String> keyFunction,
            final Iterable<? extends T> destinations)
    {
        inputMap = Maps.uniqueIndex(destinations, keyFunction);
    }

    public void reset(){
        exchangeList.clear();
    }

    public ImmutableList<Exchange> getExchangeList(
            final T destination)
    {
        return ImmutableList.copyOf(exchangeList.get(destination));
    }
    
    public ImmutableMultimap<T, Exchange> getExchangeMap()
    {
        return ImmutableMultimap.copyOf(exchangeList);
    }

    @Override
    public void process(
            final Exchange exchange) throws Exception
    {
        String sourceRoute = (String) exchange.getIn().getHeader(JmsCaptureRouteBuilder.INPUT_TOPIC_HEADER);

        T input = inputMap.get(sourceRoute);
        exchangeList.put(input, exchange);

        LOG.debug("\n<---- Observed message sent to {}", input);
    }

    Iterable<String> getDestinations()
    {
        return inputMap.keySet();
    }
}
