### Redpanda AdminClient/listConsumerGroups Reproducer

Redpanda Version: v21.7.4 (current latest)

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

### Stop the Redpanda Cluster

```
HOME=./ docker compose -f docker-compose-basic.yml down
```
