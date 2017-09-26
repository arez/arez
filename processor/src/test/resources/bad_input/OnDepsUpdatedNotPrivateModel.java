import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@Container
public class OnDepsUpdatedNotPrivateModel
{
  @Tracked
  public void render()
  {
  }

  @OnDepsUpdated
  private void onRenderDepsUpdated()
  {
  }
}
