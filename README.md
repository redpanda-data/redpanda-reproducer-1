# Redpanda AdminClient/listConsumerGroups Reproducer

Redpanda Version: v21.7.4 (current latest)

Bug:

 AdminClient/listConsumerGroup throws a TimeoutException
 
 ```
 Error listing groups on localhost:9092 (id: 0 rack: null): Call(callName=listConsumerGroups, deadlineMs=1626870066072, tries=570, nextAllowedTryMs=1626870066173) timed out at 1626870066073 after 570 attempt(s)
 ```
 
 When:
 
 1. The following broker configuration is set:

 ```
 
