package control.system.dagger;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import control.system.service.MappingService;
import control.system.service.MongoService;
import control.system.service.impl.MappingServiceImpl;
import control.system.service.impl.MongoServiceImpl;
import dagger.Module;
import dagger.Provides;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import javax.inject.Singleton;

import static control.system.service.Constants.MONGO_CONNECTION_STRING;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Module
public class ControlSystemModule {

    @Provides
    @Singleton
    MappingService provideMappingService(){
        return new MappingServiceImpl();
    }

    @Provides
    @Singleton
    MongoService provideMongoService(){
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(MONGO_CONNECTION_STRING))
                .serverApi(serverApi)
                .codecRegistry(codecRegistry)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        return new MongoServiceImpl(mongoClient);
    }
}
