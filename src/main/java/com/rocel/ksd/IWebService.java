package com.rocel.ksd;

import org.apache.kafka.streams.KafkaStreams;

public interface IWebService {
    void start(KafkaStreams streams, String host, int port);

    void stop();
}
