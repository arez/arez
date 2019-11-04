import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostDispose;
import arez.annotations.PreDispose;

@ArezComponent
public abstract class DisposingModel
{
  @PreDispose
  protected void preDispose()
  {
  }

  @PostDispose
  protected void postDispose()
  {
  }

  @Memoize
  public int someValue()
  {
    return 0;
  }
}
