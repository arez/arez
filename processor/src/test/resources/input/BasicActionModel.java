import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;

/**
 * Test has a single action. No observables.
 */
@Container
public class BasicActionModel
{
  @Action
  public void performAction()
  {
  }
}
