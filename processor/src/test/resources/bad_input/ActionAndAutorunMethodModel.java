import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.Container;

@Container
public class ActionAndAutorunMethodModel
{
  @Autorun
  @Action
  public void doStuff()
  {
  }
}
