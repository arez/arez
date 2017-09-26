import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@Container
public class OnDepsUpdatedMustNotHaveParametersModel
{
  @Tracked
  public void render()
  {
  }

  @OnDepsUpdated
  void onRenderDepsUpdated( int x )
  {
  }
}
