package com.practice.spring.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.management.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.logging.Level;

@Component
public class MetricReporter implements ApplicationRunner, BeanFactoryAware, EnvironmentAware {
    private static final Logger log = LoggerFactory.getLogger(MetricReporter.class);

    private Environment environment;

    private BeanFactory beanFactory;


    private MBeanServer mBeanServer;

    public MetricReporter(MBeanServer mBeanServer) {
        this.mBeanServer = mBeanServer;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String[] domains = this.mBeanServer.getDomains();
        System.out.println("domains = " + Arrays.toString(domains));

//        String port = "8103";
        String port = environment.getProperty("server.port");
        ObjectName tomcatConnector = new ObjectName("Tomcat:type=ThreadPool,name=\"http-nio-" + port + "\"");

        ObjectName receivedMessages = new ObjectName("com.practice.spring.jmx:name=application@jmx-spring-practice.receivedMessages");


        Duration updateInterval = Duration.ofSeconds(1);
        Flux.interval(updateInterval)
                .doOnSubscribe(s -> log.debug("Scheduled status update every {}", updateInterval))
                .log(log.getName(), Level.FINEST)
                .subscribeOn(Schedulers.newSingle("tomcat-connectionCount-monitor"))
                .concatMap(aLong -> {
                    Object objConnectionCount = null, objCount = null;
                    try {
                        objConnectionCount = this.mBeanServer.getAttribute(tomcatConnector, "connectionCount");
                        objCount = mBeanServer.getAttribute(receivedMessages, "Count");
                    } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException e) {
                        e.printStackTrace();
                    }

                    int connectionCount = Integer.parseInt("" + objConnectionCount);
                    System.out.println("connectionCount = " + connectionCount);


                    int receivedMessagesCount = Integer.parseInt("" + objCount);
                    System.out.println("receivedMessagesCount = " + receivedMessagesCount);
                    return Mono.empty();
                })
                .onErrorContinue((ex, value) -> log.warn("Unexpected error while updating statuses",
                        ex
                ))
                .subscribe();


    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

