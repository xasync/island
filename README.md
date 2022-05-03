![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/xasync/island)
![GitHub](https://img.shields.io/github/license/xasync/island)
# island

Island is a Java tool library precipitated in the process of long-term business development, and provides out of 
the box functionality to enable developers to focus on business logic while accelerating business development. The goal
is ambitious, but my determination is also firm.

# Quickly Started

```xml
<dependency>
    <groupId>com.xasync</groupId>
    <artifactId>island</artifactId>
    <!-- Find the latest version in maven repository -->
    <version>${island.version}</version>
</dependency>
```
*Next to read for learning to use.*

# What can island provides?

Island's plan is not very clear at current, and I just do something in the log domain. Of course, I will be updated 
step by step later. The functions provided likes:

## 1. Log Domain
In the Java ecosystem, we usually use slf4j + logback to input log information, but some very necessary inputs are 
not supported by logback. Therefore, some enhancements and extensions are made for logback.
### 1.1 Converters for logback
If you want to use the converters provided, you only need to include 'island-logback-converter.xml' into 'logback.xml'. 
Then, you can browse the specific variables provided in 'island-logback-converter.xml'.

```xml
<configuration>
    <include resource="island-logback-converter.xml"/>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%date %ip_island %level %fc_island{15,1} %msg%n</pattern>
        </encoder>
    </appender>

    <root level="debug">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```
