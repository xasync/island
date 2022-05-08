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

## 1. IfKiller
Almost all programmers in the world are extremely disgusted with large pieces of if-else nested code in their hearts, 
because the problems it brings are far more than the decline of code readability and high maintenance cost. 
It is the most troublesome thing to change the complexity of business logic and frequent bugs. Of course, 
tight development time and frequent iterations are a major factor. The most essential reason is that the complexity 
increases with the development of business. DDD can be used to solve the problem of business complexity. 
Ignore it here and start with the tight development time. Assuming that all programmers are willing to analyze 
the requirements and realize the requirements in a more reasonable way, then the issue is that programmers don't know 
that there is a more reasonable implementation and make a compromise between the implementation cost is too large.
To solve the above problems, I provide IfKiller solutions, hoping to help you.

IfKiller simplifies the zero to one implementation strategy mode every time in the style of streaming code, 
completes business development in an efficient way, and opens the peak of your life as a business development!

**How to use:**

Step 1: complete the initialization of SpringContexts in your spring application
```java
@Component
public class SpringContextsStarter implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContexts.init(applicationContext);
    }
}
```

Step 2: analyze your requirements and define some abilities and some token activated the extension of ability, 
and finally code it in the form of java interface

```java
/**
 * Defines the ability about animal's bark
*/
public interface AnimalBarkAbility {
    String start();
}

/**
 * Designs and declares the token which can activate ability's extension
*/
public enum AnimalBarkTypeEnum{
	DOG(BarkTypeEnum.DOG_CODE, "animal-dog"),
    CAT(BarkTypeEnum.CAT_CODE, "animal-cat")
    ;
    public final static String DOG_CODE = "dog";
    public final static String CAT_CODE = "cat";
    
    @Getter
    private final String code;
    @Getter
    private final String desc;
    
    AnimalBarkTypeEnum(String code, Sring desc){
    	this.code=code;
    	this.desc=desc;
    }
}
```

Step 3: coding the extension implementation of ability

```java
/**
 * Cat
*/
@IfKiller(meets = BarkTypeEnum.CAT_CODE)
public class CatAnimalBarkAbility implements AnimalBarkAbility {
    @Override
    public String start() {
        return "cat";
    }
}

/**
 * Dog
*/
@IfKiller(meets = BarkTypeEnum.DOG_CODE, isDefault = true)
public class DogAnimalBarkAbility implements AnimalBarkAbility {
    @Override
    public String start() {
        return "dog";
    }
}
```

Step 3: applying
```java
public class Demo {
    public static void main(String[] args) {
        //you can use '#' to join dimensions, likes: cat#3#male(animal + age + gender)
        String token = "cat";
        String voice = IfKillers.about(AnimalBarkAbility.class)
                .meet(token)
                .start();
        System.out.println(voice);
    }
}
```

## 2. Log Domain
In the Java ecosystem, we usually use slf4j + logback to input log information, but some very necessary inputs are 
not supported by logback. Therefore, some enhancements and extensions are made for logback.
### 2.1 Converters for logback
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

## 3. Spring Domain

## 3.1 SpringContexts
Provides tools and methods to get the current ApplicationContext in spring project or related operations, but you must
finish to initialize before you use.

```java
@Component
public class SpringContextsStarter implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContexts.init(applicationContext);
    }
}
```