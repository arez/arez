import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.OnDepsChanged;
import org.realityforge.arez.annotations.Track;

@ArezComponent( nameIncludesId = false )
public class SingletonModel
{
  @Observable
  public long getTime()
  {
    return 0;
  }

  @Observable
  public void setTime( final long time )
  {
  }

  @Action
  public void doStuff( final long time )
  {
  }

  @Computed
  public int someValue()
  {
    return 0;
  }

  @Track
  public void render()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }

  @Autorun
  public void myAutorun()
  {
  }
}
