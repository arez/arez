import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class TypeParametersOnModel<T extends Integer>
{
  @Action
  public void doStuff()
  {
  }
}
