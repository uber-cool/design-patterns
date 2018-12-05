package com.ubercool.operation.manager.units;

import com.ubercool.operation.manager.model.OperationContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddInputsUnit extends AbstractPrepareOperation {

  @Override
  protected boolean execute(OperationContext context) {
    log.info("[STEP] Adding input to context");
    context.setInput("input_data");
    return true;
  }
}
