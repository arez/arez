import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.Computed;
import arez.annotations.Observable;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;
import javax.inject.Singleton;

@Singleton
@ArezComponent
public abstract class ImplicitSingletonModel
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
  protected void myAutorun()
  {
  }
}
