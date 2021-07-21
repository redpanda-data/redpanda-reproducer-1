(defproject kpow/redpanda-reproducer-1 "1.0.0"

  :description "Redpanda AdminClient Bug Reproducer"

  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/tools.logging "1.1.0"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [org.apache.kafka/kafka-streams "2.8.0" :exclusions [com.fasterxml.jackson.core/jackson-core]]]

  :source-paths ["src"]

  :main kpow.redpanda)
