package com.ubercool.operation.manager;

import com.ubercool.operation.manager.model.OperationContext;
import com.ubercool.operation.manager.units.AbstractPrepareOperation;
import com.ubercool.operation.manager.units.AddInputsUnit;
import com.ubercool.operation.manager.units.GetAdditionalData;
import com.ubercool.operation.manager.units.SendOrderForExecution;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationExecuter {

  private AbstractPrepareOperation firstStep;

  @Autowired
  public OperationExecuter(AddInputsUnit addInputsUnit,
      GetAdditionalData getAdditionalData,
      SendOrderForExecution sendOrderForExecution) {
    List<AbstractPrepareOperation> executionSteps = new ArrayList<>();

    // The steps will be executed in the sequence they are present in list
    executionSteps.add(addInputsUnit);
    executionSteps.add(getAdditionalData);
    executionSteps.add(sendOrderForExecution);

    createChainOfSteps(executionSteps);

    // First step from the chain
    firstStep = executionSteps.get(0);

  }

  private void createChainOfSteps(List<AbstractPrepareOperation> executionSteps) {
    // Create chain here
    int i = 1;
    for (AbstractPrepareOperation prepareOperationAction: executionSteps) {
      if (i < executionSteps.size())
      prepareOperationAction.setNext(executionSteps.get(i++));
    }
  }

  public void createAndExecuteOperation(OperationContext context){
    firstStep.handle(context);
  }

}
