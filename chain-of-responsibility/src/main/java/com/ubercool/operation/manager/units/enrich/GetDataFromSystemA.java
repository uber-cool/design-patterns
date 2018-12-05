package com.ubercool.operation.manager.units.enrich;

import com.ubercool.operation.manager.model.OperationContext;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetDataFromSystemA extends AbstractEnhanceOperationUnit{

  @Override
  protected boolean execute(OperationContext context) {
    log.info("[SUB_STEP] Getting additional data from System A");
    String additionalData = "Data from A";
    if (context.getAdditionalData() == null) {
      context.setAdditionalData(new ArrayList<>());
    }
    context.getAdditionalData().add(additionalData);
    return true;
  }
}
