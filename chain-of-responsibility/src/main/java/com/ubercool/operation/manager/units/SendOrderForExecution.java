package com.ubercool.operation.manager.units;

import com.ubercool.operation.manager.model.OperationContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendOrderForExecution extends AbstractPrepareOperation {

  @Override
  protected boolean execute(OperationContext context) {
    log.info("[STEP] Operation sent for processing");
    context.setOperationId("operation_id_from_susbsystem");
    return true;
  }
}
