import arez.annotations.ArezComponent;
import arez.annotations.Computed;
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

  @Computed
  public int someValue()
  {
    return 0;
  }
}
