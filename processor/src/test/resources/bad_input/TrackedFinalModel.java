import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@Container
public class TrackedFinalModel
{
  @Tracked
  public final void render()
  {
  }

  @OnDepsUpdated
  public void onRenderDepsUpdated()
  {
  }
}
