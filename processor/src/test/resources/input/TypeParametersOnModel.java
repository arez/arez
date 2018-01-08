import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public class TypeParametersOnModel<T extends Integer>
{
  @Action
  public void doStuff()
  {
  }
}
