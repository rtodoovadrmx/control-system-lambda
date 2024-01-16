package control.system.service.impl;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import control.system.domain.ProcessedControlSystemData;
import control.system.domain.ControlSystemData;
import control.system.service.MongoService;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.openjdk.tools.javac.util.Constants;

import java.util.ArrayList;
import java.util.List;

import static control.system.service.Constants.*; //CONTROL_SYSTEM_PROCESSED_COLLECTION;

public class MongoServiceImpl implements MongoService {

    private final MongoClient mongoClient;

    public MongoServiceImpl(MongoClient mongoClient){
        this.mongoClient=mongoClient;
    }

    @Override
       public void writeBulkData(List<ControlSystemData> controlSystemDataList) {
            MongoDatabase database = mongoClient.getDatabase(CONTROL_SYSTEM_DATABASE);
            MongoCollection<ProcessedControlSystemData> collection = database.getCollection(CONTROL_SYSTEM_PROCESSED_COLLECTION, ProcessedControlSystemData.class);
            List<ProcessedControlSystemData> dataToStore = new ArrayList<>();
            for (ControlSystemData rd : controlSystemDataList) {
                ProcessedControlSystemData processedControlSystemData = ProcessedControlSystemData.builder()
                        .flight(rd.getFlight())
                        .gate(rd.getGate())
                        .status(STATUS_PROCESSED)
                        .build();
                dataToStore.add(processedControlSystemData);
            }
            InsertManyResult result = collection.insertMany(dataToStore);
     }
}