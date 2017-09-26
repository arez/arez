import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@Container
public class OnDepsUpdatedDuplicatedModel
{
  @Tracked
  public void render()
  {
  }

  @OnDepsUpdated
  public void onRenderDepsUpdated()
  {
  }

  @OnDepsUpdated( name = "render" )
  public void onRenderDepsUpdated2()
  {
  }
}
