package control.system.service;

import com.mongodb.client.MongoClient;
import control.system.domain.ControlSystemData;

import java.util.List;

public interface MongoService {

    void writeBulkData(List<ControlSystemData> controlSystemDataList);
}
