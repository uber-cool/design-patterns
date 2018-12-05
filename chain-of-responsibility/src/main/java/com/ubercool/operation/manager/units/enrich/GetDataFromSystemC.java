package com.ubercool.operation.manager.units.enrich;

import com.ubercool.operation.manager.model.OperationContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetDataFromSystemC extends AbstractEnhanceOperationUnit {

  @Override
  protected boolean execute(OperationContext context) {
    return false;
  }

  @Override
  protected boolean isApplicable(OperationContext context) {
    log.info("[SUB_STEP] Getting additional data from System C will be skipped");
    return false;
  }
}
