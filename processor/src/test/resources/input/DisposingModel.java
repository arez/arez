import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.PostDispose;
import org.realityforge.arez.annotations.PreDispose;

@ArezComponent
public class DisposingModel
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
