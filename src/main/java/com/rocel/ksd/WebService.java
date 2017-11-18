package com.rocel.ksd;

import com.google.gson.Gson;
import com.rocel.ksd.template.MustacheTemplateEngine;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.internals.GlobalStreamThread;
import org.apache.kafka.streams.processor.internals.ProcessorNode;
import org.apache.kafka.streams.processor.internals.ProcessorTopology;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.lang.reflect.Field;
import java.util.*;

import static spark.Spark.*;

public class WebService implements IWebService {
    private static final Logger Log = LoggerFactory.getLogger(WebService.class);
    private Gson gson = new Gson();

    @Override
    public void start(KafkaStreams streams, String host, int port) {
        staticFileLocation("/public");
        port(port);

        get("/", this::getHome, new MustacheTemplateEngine());

        path("api/", () -> {
            get("stores/:storename", (request, response) -> getStoreFromName(streams, request, response));
            get("graph", (request, response) -> getGraph(streams, request, response));
        });
    }

    private ModelAndView getHome(Request request, Response response) {
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(model, "home.mustache");
    }

    private String getGraph(KafkaStreams streams, Request request, Response response) throws NoSuchFieldException {
        Map<Object, Object> graph = new HashMap<>();
        GlobalStreamThread globalStreamThread = this.getPrivateField(streams, "globalStreamThread");
        ProcessorTopology topology = this.getPrivateField(globalStreamThread, "topology");

        List<String> topicsList = new ArrayList<>();
        List<String> storesList = new ArrayList<>();
        for (ProcessorNode proc : topology.processors()) {
            if (proc.name().startsWith("KSTREAM")) {
                List<String> topics = this.getPrivateField(proc, "topics");
                topicsList.add(topics.get(0));
            } else if (proc.name().startsWith("KTABLE")) {
                HashSet<String> topics = this.getPrivateField(proc, "stateStores");
                storesList.add((String) topics.toArray()[0]);
            }
        }
        graph.put("topologyName", globalStreamThread.getName());
        graph.put("topics", topicsList);
        graph.put("stores", storesList);
        Gson gson = new Gson();
        response.status(200);
        response.type("application/json");
        return gson.toJson(graph);
    }

    private <T> T getPrivateField(Object from, String name) {
        T a = null;
        try {
            Field f = from.getClass().getDeclaredField(name);
            f.setAccessible(true);
            a = (T) f.get(from);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Log.error("Could not access the globalStreamThread private field");
        }
        return a;
    }

    private String getStoreFromName(KafkaStreams streams, Request request, Response response) {
        String storeName = request.params(":storename");
        Log.debug("Requesting store " + storeName);
        response.status(200);
        response.type("application/json");
        Map<Object, Object> data = new HashMap<>();
        ReadOnlyKeyValueStore<Object, Object> store = getStore(streams, storeName);
        if (store != null) {
            KeyValueIterator<Object, Object> all = store.all();
            while ((all.hasNext())) {
                KeyValue<Object, Object> next = all.next();
                data.put(next.key, next.value);
            }
        }
        return gson.toJson(data);
    }

    private ReadOnlyKeyValueStore<Object, Object> getStore(KafkaStreams streams, String storeName) {
        try {
            return streams.store(storeName, QueryableStoreTypes.keyValueStore());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void stop() {
        spark.Spark.stop();
    }

}