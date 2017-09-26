import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@Container
public class OnDepsUpdatedNotStaticModel
{
  @Tracked
  public void render()
  {
  }

  @OnDepsUpdated
  static void onRenderDepsUpdated()
  {
  }
}
