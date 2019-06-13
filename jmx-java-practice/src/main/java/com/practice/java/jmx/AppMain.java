package com.practice.java.jmx;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * @author Luo Bao Ding
 * @since 2019/1/30
 */
public class AppMain {

    public void start() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        Game game = new Game("John");
        ObjectName objectName = new ObjectName("com.practice.java.jmx:type=basic,name=game");

        platformMBeanServer.registerMBean(game, objectName);

        System.out.println("started");


        try {
            while (!game.getCommand().equals("stop")) {
                System.out.println("running...");
                Thread.sleep(3000);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        new AppMain().start();
    }
}
