package com.rocel.ksd;

import com.google.gson.Gson;
import com.rocel.ksd.template.MustacheTemplateEngine;
import kafka.admin.AdminUtils;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.requests.MetadataResponse;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.internals.GlobalStreamThread;
import org.apache.kafka.streams.processor.internals.ProcessorNode;
import org.apache.kafka.streams.processor.internals.ProcessorTopology;
import org.apache.kafka.streams.processor.internals.StreamThread;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.JavaConversions;
import scala.collection.Seq;
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
    public void start(KafkaStreams streams, int port, String zkHosts) {
        staticFileLocation("public");
        port(port);
        System.out.println("KSD started on port " + port);
        get("/", this::getHome, new MustacheTemplateEngine());

        path("api/", () -> {
            get("stores/:storename", (request, response) -> getStoreFromName(streams, request, response));
            get("topics/:topicname", (request, response) -> getTopicDataFromName(zkHosts, request, response));
            get("graph", (request, response) -> getGraph(streams, request, response));
        });
    }

    private ModelAndView getHome(Request request, Response response) {
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(model, "home.mustache");
    }

    private String getGraph(KafkaStreams streams, Request request, Response response) throws NoSuchFieldException {
        List<String> topicsList = new ArrayList<>();
        List<String> storesList = new ArrayList<>();
        String appName;
        StreamThread[] threads = this.getPrivateField(streams, "threads");
        GlobalStreamThread globalStreamThread = this.getPrivateField(streams, "globalStreamThread");
        if (threads != null && threads.length > 0) {
            appName = threads[0].applicationId;
        } else {
            appName = globalStreamThread.getName();
        }
        if (globalStreamThread != null) {
            ProcessorTopology topology = this.getPrivateField(globalStreamThread, "topology");

            for (ProcessorNode proc : topology.processors()) {
                if (proc.name().startsWith("KSTREAM")) {
                    List<String> topics = this.getPrivateField(proc, "topics");
                    topicsList.add(topics.get(0));
                } else if (proc.name().startsWith("KTABLE")) {
                    HashSet<String> topics = this.getPrivateField(proc, "stateStores");
                    storesList.add((String) topics.toArray()[0]);
                } else {
                    System.out.println(">>> Found unknown process: " + proc.name());
                }
            }
        }

        Map<Object, Object> graph = new HashMap<>();
        graph.put("topologyName", appName);
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

    private String getTopicDataFromName(String zkHosts, Request request, Response response) {
        String topicName = request.params(":topicName");
        ZkUtils zkUtils = ZkUtils.apply(zkHosts, 3000, 3000, false);

        Properties configProperties = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topicName);
        MetadataResponse.TopicMetadata topicMetadata = AdminUtils.fetchTopicMetadataFromZk(topicName, zkUtils);

        Gson gson = new Gson();
        configProperties.put("nbPartions", topicMetadata.partitionMetadata().size());
        configProperties.put("isInternal", topicMetadata.isInternal());
        return gson.toJson(configProperties);
    }

    public java.util.Map<String, java.util.Map<Integer, java.util.List<Integer>>> getPartitionAssignmentForTopics(ZkUtils zkUtils, final List<String> topics) {
        final scala.collection.Seq<String> seqTopics = scala.collection.JavaConversions.asScalaBuffer(topics).toList();
        scala.collection.mutable.Map<String, scala.collection.Map<Object, scala.collection.Seq<Object>>> tmpMap1 =
                zkUtils.getPartitionAssignmentForTopics(seqTopics);

        final java.util.Map<String, java.util.Map<Integer, java.util.List<Integer>>> result = new HashMap<>();
        java.util.Map<String, scala.collection.Map<Object, Seq<Object>>> tmpMap2 = JavaConversions.mapAsJavaMap(tmpMap1);
        tmpMap2.forEach((k1, v1) -> {
            String topic = (String) k1;
            java.util.Map<Object, Seq<Object>> objectSeqMap = JavaConversions.mapAsJavaMap(v1);
            java.util.Map<Integer, List<Integer>> tmpResultMap = new HashMap<>();
            objectSeqMap.forEach((k2, v2) -> {
                Integer tmpInt = (Integer) k2;
                List<Integer> tmpList = (List<Integer>) (Object) JavaConversions.seqAsJavaList(v2);
                tmpResultMap.put(tmpInt, tmpList);
            });
            result.put(topic, tmpResultMap);
        });

        return result;
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