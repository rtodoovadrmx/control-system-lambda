package control.system.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import control.system.domain.ControlSystemData;
import control.system.domain.ControlSystemDataProcessingResult;

import java.util.List;

public interface MappingService {

    ControlSystemDataProcessingResult parseRawMessageDataToRequestDataList (List<SQSEvent.SQSMessage> sqsMessages);

    ControlSystemData parseRawMessageDataToRequestData (SQSEvent.SQSMessage sqsMessage) throws JsonProcessingException;
}
