(ns kpow.redpanda
  (:require [clojure.tools.logging :as log])
  (:import (org.apache.kafka.clients.admin AdminClient NewTopic)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (java.util Optional)
           (java.time Duration)))

(def topic "kpow_topic")

(defn list-groups
  [client]
  (log/info "groups:" (-> client (.listConsumerGroups) (.all) (.get))))

(defn create-topic
  [client]
  (-> (.createTopics client [(NewTopic. topic (Optional/of (int 12)) (Optional/of (short 3)))])
      (.all)
      (.get))
  (log/info "created kpow_topic"))

(defn produce
  [producer key value]
  (-> (.send producer (ProducerRecord. topic key value))
      (.get))
  (log/info "produced" key value))

(defn consume
  [consumer]
  (.subscribe consumer [topic])
  (log/info "consumed" (.count (.poll consumer (Duration/ofMillis 2000))) "messages"))

(defn show-bug
  []
  (let [client   (AdminClient/create {"bootstrap.servers" "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094"})
        producer (KafkaProducer. {"bootstrap.servers" "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094"
                                  "key.serializer"    "org.apache.kafka.common.serialization.StringSerializer"
                                  "value.serializer"  "org.apache.kafka.common.serialization.StringSerializer"})
        consumer (KafkaConsumer. {"group.id"           "test-kpow-group"
                                  "bootstrap.servers"  "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094"
                                  "enable.auto.commit" "true"
                                  "auto.offset.reset"  "earliest"
                                  "key.deserializer"   "org.apache.kafka.common.serialization.StringDeserializer"
                                  "value.deserializer" "org.apache.kafka.common.serialization.StringDeserializer"})]
    (list-groups client)
    (create-topic client)
    (list-groups client)
    (produce producer "a" "b")
    (produce producer "b" "c")
    (produce producer "c" "d")
    (produce producer "d" "e")
    (produce producer "e" "f")
    (list-groups client)
    (consume consumer)

    ;; this list groups call times out when the following broker configuration is set
    ;;  default_topic_partitions: 18
    ;;  default_topic_replications: 3
    (list-groups client)))

(defn -main
  [& _]
  (log/info "kpow redpanda reproducer-1")
  (show-bug))
