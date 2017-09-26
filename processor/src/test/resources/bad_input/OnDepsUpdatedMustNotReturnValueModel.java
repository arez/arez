import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@Container
public class OnDepsUpdatedMustNotReturnValueModel
{
  @Tracked
  public void render()
  {
  }

  @OnDepsUpdated
  int onRenderDepsUpdated()
  {
    return 0;
  }
}
