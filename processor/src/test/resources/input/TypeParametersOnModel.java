import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;

@Container
public class TypeParametersOnModel<T extends Integer>
{
  @Action
  public void doStuff()
  {
  }
}
