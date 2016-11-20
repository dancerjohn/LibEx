package org.libex.camel;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
public enum MessageLocation implements Function<Exchange, Message> {
    IN {
        @Override
        public Message getMessage(
                final Exchange input)
        {
            return input == null ? null : input.getIn();
        }
    },
    OUT {
        @Override
        public Message getMessage(
                final Exchange input)
        {
            return input == null ? null : input.getOut();
        }
    };

    public abstract Message getMessage(
            final Exchange input);

    @Override
    public Message apply(
            final Exchange input)
    {
        return getMessage(input);
    }
}
