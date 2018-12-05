package operation;

import com.ubercool.operation.manager.OperationExecuter;
import com.ubercool.operation.manager.config.AppConfig;
import com.ubercool.operation.manager.model.OperationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class CreateAndExecuteOperationTest {

  @Autowired
  private OperationExecuter operationExecuter;

  @Test
  public void prepareAndExecuteOperation(){
    OperationContext operationContext = new OperationContext();
    operationExecuter.createAndExecuteOperation(operationContext);
    assert operationContext.getInput().equals("input_data");
    assert operationContext.getOperationId().equals("operation_id_from_susbsystem");
  }
}
