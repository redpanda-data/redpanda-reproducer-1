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

HOME=./ docker compose -f docker-compose-basic.yml down

