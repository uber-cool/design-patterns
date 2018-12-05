package com.ubercool.operation.manager.units;

import com.ubercool.operation.manager.model.OperationContext;
import com.ubercool.operation.manager.units.enrich.AbstractEnhanceOperationUnit;
import com.ubercool.operation.manager.units.enrich.GetDataFromSystemA;
import com.ubercool.operation.manager.units.enrich.GetDataFromSystemB;
import com.ubercool.operation.manager.units.enrich.GetDataFromSystemC;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetAdditionalData extends AbstractPrepareOperation {

  List<AbstractEnhanceOperationUnit> executionSteps = new ArrayList<>();

  @Autowired
  public GetAdditionalData(GetDataFromSystemA getDataFromSystemA,
      GetDataFromSystemB getDataFromSystemB,
      GetDataFromSystemC getDataFromSystemC) {

    // The steps will be executed in the sequence they are present in list
    executionSteps.add(getDataFromSystemA);
    executionSteps.add(getDataFromSystemB);
    executionSteps.add(getDataFromSystemC);

  }

  @Override
  protected boolean execute(OperationContext context) {
    log.info("[STEP] Getting additional data for operation processing");
    log.info("This step have sub steps");

    //No need to have nextStep property just define the steps in the list and execute each step
    for (AbstractEnhanceOperationUnit enhanceOperationUnit: executionSteps) {
      if (!enhanceOperationUnit.handle(context)){
        return false;
      }
    }
    return true;
  }
}
