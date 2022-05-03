/*
Copyright 2022~Forever xasync.com under one or more contributor authorized.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.xasync.island.log.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Objects;

/**
 * IpOfMachineConverter
 *
 * @author xasync.com
 */
public class IpOfMachineConverter extends ClassicConverter {

    /**
     * The short name about registering itself into logback
     */
    public final static String SHORT_NAME = "ip_island";


    /**
     * The long name about registering itself into logback
     */
    public final static String LONG_NAME = "ipMachine_island";

    /**
     * The static global variable which will be assigned the ip of current local machine after starting.
     */
    private static String ipOfMachine = "127.0.0.1";

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return ipOfMachine;
    }

    @Override
    public void start() {
        try {
            boolean isFind = false;
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ipMachineAddress = null;
            InetAddress candidateAddress = null;
            while (interfaces.hasMoreElements() && !isFind) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> inetAddrs = networkInterface.getInetAddresses();
                while (inetAddrs.hasMoreElements()) {
                    InetAddress inetAddress = inetAddrs.nextElement();
                    if (inetAddress.isLoopbackAddress()) {
                        continue;
                    }
                    if (inetAddress.isSiteLocalAddress()) {
                        ipMachineAddress = inetAddress;
                        isFind = true;
                        break;
                    } else {
                        if (Objects.isNull(candidateAddress)) {
                            candidateAddress = inetAddress;
                        }
                    }
                }
            }
            //assign value for the ip of machine
            if (Objects.nonNull(ipMachineAddress)) {
                ipOfMachine = ipMachineAddress.getHostAddress();
            } else if (Objects.nonNull(candidateAddress)) {
                ipOfMachine = candidateAddress.getHostAddress();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
