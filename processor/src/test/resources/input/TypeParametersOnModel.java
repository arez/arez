import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@ArezComponent
public class TypeParametersOnModel<T extends Integer>
{
  @Action
  public void doStuff()
  {
  }
}
