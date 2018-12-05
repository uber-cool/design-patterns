package com.ubercool.operation.manager.units.enrich;

import com.ubercool.operation.manager.model.OperationContext;

public abstract class AbstractEnhanceOperationUnit {

  protected boolean isApplicable(OperationContext context) {
    return true;
  }

  protected abstract boolean execute(OperationContext context);

  public boolean handle(OperationContext context) {
    if (isApplicable(context)) {
      if (!execute(context)) {
        return false;
      }
    }
    return true;
  }

}
