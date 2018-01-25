import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class TypeParametersOnModel<T extends Integer>
{
  @Action
  public void doStuff()
  {
  }
}
