package com.rocel.ksd;

import org.apache.kafka.streams.KafkaStreams;

public interface KSDBase {
    void start(KafkaStreams streams, int port, String zkKhost);

    void stop();
}
