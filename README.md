### Redpanda AdminClient/listConsumerGroups Reproducer

Redpanda Version: v21.7.4 (current latest)

Background: encountered when running [kPow](https://kpow.io) with a Redpanda cluster with modified broker configuration.

 **Report:**

 AdminClient/listConsumerGroup throws a TimeoutException
 
 ```bash
 Error listing groups on localhost:9092 (id: 0 rack: null): Call(callName=listConsumerGroups, deadlineMs=1626870066072, tries=570, nextAllowedTryMs=1626870066173) timed out at 1626870066073 after 570 attempt(s)
 ```
 
 **When:**
 
 The following broker configuration is set:

 ```bash
   default_topic_partitions: 18
   default_topic_replications: 3
 ``` 
 
 **And:**
 
 At least one ConsumerGroup is connected to the cluster.

### Steps to Reproduce

Clone this repo.

#### Start a 3-Node Redpanda with Default Configuration

```
HOME=./ docker compose -f docker-compose-basic.yml up
```

#### Run the Reproducer

The reproducer:

1. Start an admin-client, consumer, and producer.
2. Creates a topic
3. Writes messages to the topic
4. Consumes message from the topic
5. Lists ConsumerGroups

The reproducer is written in Clojure, to run it install [Leiningen](https://leiningen.org/) and:

```
lein run
```

Output demonstrates successful run of all commands:

```
redpanda-reproducer-1 $ lein run                                                                                                                                 
OpenJDK 64-Bit Server VM warning: Options -Xverify:none and -noverify were deprecated in JDK 13 and will likely be removed in a future release.
22:18:34.066 INFO  [main] kpow.redpanda – kpow redpanda reproducer-1
22:18:34.613 INFO  [main] kpow.redpanda – groups: #object[java.util.ArrayList 0x6e3ecf5c []]
22:18:34.731 INFO  [main] kpow.redpanda – created kpow_topic
22:18:34.736 INFO  [main] kpow.redpanda – groups: #object[java.util.ArrayList 0x45f756e6 []]
22:18:36.568 INFO  [main] kpow.redpanda – produced a b
22:18:36.570 INFO  [main] kpow.redpanda – produced b c
22:18:36.573 INFO  [main] kpow.redpanda – produced c d
22:18:36.579 INFO  [main] kpow.redpanda – produced d e
22:18:36.760 INFO  [main] kpow.redpanda – produced e f
22:18:36.764 INFO  [main] kpow.redpanda – groups: #object[java.util.ArrayList 0x7219ac49 []]
22:18:38.485 INFO  [main] kpow.redpanda – consumed 1 messages
22:18:38.491 INFO  [main] kpow.redpanda – groups: #object[java.util.ArrayList 0x67774e29 [(groupId='test-kpow-group', isSimpleConsumerGroup=false, state=Optional.empty)]]
```

#### Stop the Redpanda Cluster

```
HOME=./ docker compose -f docker-compose-basic.yml down
```

### Demonstrate Timeout

#### Start a 3-Node Redpanda with Custom Configuration

```
HOME=./ docker compose -f docker-compose-both.yml up
```

#### Run the Reproducer

Run the reproducer

```
lein run
```

Output demonstrates timeout on AdminClient/listConsumerGroups:

```redpanda-reproducer-1 $ lein run                                                                                                                              
OpenJDK 64-Bit Server VM warning: Options -Xverify:none and -noverify were deprecated in JDK 13 and will likely be removed in a future release.
22:20:01.065 INFO  [main] kpow.redpanda – kpow redpanda reproducer-1
22:20:01.741 INFO  [main] kpow.redpanda – groups: #object[java.util.ArrayList 0x290e8cab []]
22:20:01.853 INFO  [main] kpow.redpanda – created kpow_topic
22:20:01.859 INFO  [main] kpow.redpanda – groups: #object[java.util.ArrayList 0x4d1ff6b1 []]
22:20:03.500 INFO  [main] kpow.redpanda – produced a b
22:20:03.504 INFO  [main] kpow.redpanda – produced b c
22:20:04.121 INFO  [main] kpow.redpanda – produced c d
22:20:04.123 INFO  [main] kpow.redpanda – produced d e
22:20:04.130 INFO  [main] kpow.redpanda – produced e f
22:20:04.135 INFO  [main] kpow.redpanda – groups: #object[java.util.ArrayList 0x44be69aa []]
22:20:06.072 INFO  [main] kpow.redpanda – consumed 3 messages
Syntax error (TimeoutException) compiling at (/private/var/folders/gz/7g238rvd6j1c_jrqqc87_7_m0000gn/T/form-init12984523496375365583.clj:1:126).
Error listing groups on localhost:9092 (id: 0 rack: null): Call(callName=listConsumerGroups, deadlineMs=1626870066072, tries=570, nextAllowedTryMs=1626870066173) timed out at 1626870066073 after 570 attempt(s)
```

#### Stop the Redpanda Cluster

```
HOME=./ docker compose -f docker-compose-both.yml down
```

