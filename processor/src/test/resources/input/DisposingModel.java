import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PostDispose;
import org.realityforge.arez.annotations.PreDispose;

@Container
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
