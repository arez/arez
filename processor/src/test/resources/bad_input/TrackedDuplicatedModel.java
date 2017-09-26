import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@Container
public class TrackedDuplicatedModel
{
  @Tracked
  public void render()
  {
  }

  @Tracked( name = "render" )
  public void render2()
  {
  }

  @OnDepsUpdated
  public void onRenderDepsUpdated()
  {
  }
}
