package com.ubercool.operation.manager.units;

import com.ubercool.operation.manager.model.OperationContext;

public abstract class AbstractPrepareOperation {
  private AbstractPrepareOperation next;

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

    return getNext() == null || getNext().handle(context);
  }

  public AbstractPrepareOperation getNext() {
    return next;
  }

  public void setNext(AbstractPrepareOperation next) {
    this.next = next;
  }
}
