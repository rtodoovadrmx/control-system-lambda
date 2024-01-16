package control.system;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import control.system.dagger.DaggerDependeniesComponent;
import control.system.dagger.DependeniesComponent;
import control.system.domain.ControlSystemDataProcessingResult;
import control.system.service.MappingService;
import control.system.service.MongoService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ControlSystemLambdaHandler implements RequestHandler<SQSEvent, SQSBatchResponse>  {

    private static final DependeniesComponent dependeniesComponent = DaggerDependeniesComponent.create();

    private MappingService mappingService;
    private MongoService mongoService;

    public ControlSystemLambdaHandler () {
        this.mappingService = dependeniesComponent.getMappingService();
        this.mongoService = dependeniesComponent.getMongoServce();
    }

    @Override
    public SQSBatchResponse handleRequest(SQSEvent data, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log("Message: " + data.toString());
        ControlSystemDataProcessingResult controlSystemDataProcessingResult = mappingService.parseRawMessageDataToRequestDataList(data.getRecords());
        logger.log("Extracted messages:" + controlSystemDataProcessingResult.getSuccessfullyParsed().toString());
        logger.log("Incorrect messages:" + controlSystemDataProcessingResult.getBatchItemFailures().toString());

        if (!controlSystemDataProcessingResult.getSuccessfullyParsed().isEmpty()) {
            logger.log("Uploading data to MongoDb...");
            mongoService.writeBulkData(controlSystemDataProcessingResult.getSuccessfullyParsed());
            logger.log("Control data successfully stored in Mongo");
        }
        
        return new SQSBatchResponse(controlSystemDataProcessingResult.getBatchItemFailures());
    }


}
