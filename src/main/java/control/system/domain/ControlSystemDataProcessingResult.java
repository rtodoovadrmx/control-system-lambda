package control.system.domain;

import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControlSystemDataProcessingResult {

    private List<ControlSystemData> successfullyParsed = new ArrayList<>();
    private List<SQSBatchResponse.BatchItemFailure> batchItemFailures = new ArrayList<>();
}
