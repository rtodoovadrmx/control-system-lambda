package control.system.service.impl;

import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import control.system.domain.ControlSystemData;
import control.system.domain.ControlSystemDataProcessingResult;
import control.system.service.MappingService;

import java.util.List;

public class MappingServiceImpl implements MappingService {


    @Override
    public ControlSystemDataProcessingResult parseRawMessageDataToRequestDataList (List<SQSEvent.SQSMessage> sqsMessages){
        ControlSystemDataProcessingResult controlSystemDataProcessingResult = new ControlSystemDataProcessingResult();
        for (SQSEvent.SQSMessage sqsMessage : sqsMessages) {
            try {
                controlSystemDataProcessingResult.getSuccessfullyParsed().add(parseRawMessageDataToRequestData(sqsMessage));
            } catch (JsonProcessingException e) {
                controlSystemDataProcessingResult.getBatchItemFailures().add(new SQSBatchResponse.BatchItemFailure(sqsMessage.getMessageId()));
            }
        }
        return controlSystemDataProcessingResult;
    }

    @Override
    public ControlSystemData parseRawMessageDataToRequestData (SQSEvent.SQSMessage sqsMessage) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(sqsMessage.getBody(), ControlSystemData.class);
    }

}

