package com.practice.spring.jmx.config;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @author Luo Bao Ding
 * @since 2019/1/30
 */
@Configuration
public class AppMetricConfig {

    @Bean
    public JmxMeterRegistry jmxMeterRegistry(JmxConfig config, Clock clock) {
        //derived from HierarchicalNameMapper.DEFAULT
        /* this custom effect example:
        registry.counter("received.messages")
        ObjectName receivedMessages = new ObjectName("com.practice.spring.jmx:name=application@jmx-spring-practice.receivedMessages");
*/
        HierarchicalNameMapper CUSTOM_NAME_MAPPER = (id, convention) -> id.getConventionTags(convention).stream()
                .sorted(Comparator.comparing(Tag::getKey))
                .map(tag -> tag.getKey() + "@" + tag.getValue())
                .map(nameSegment -> nameSegment.replace(" ", "_"))
                .collect(Collectors.joining("."))
                + "." + id.getConventionName(convention);
        return new JmxMeterRegistry(config, clock, CUSTOM_NAME_MAPPER);
    }

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricCommonTags() {
        return registry -> {
            registry.config().commonTags("application", "jmx-spring-practice");
        };
    }
}
