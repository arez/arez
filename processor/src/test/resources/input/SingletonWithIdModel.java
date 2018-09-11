import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Executor;
import arez.annotations.Observable;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;
import javax.inject.Singleton;

@SuppressWarnings( "DefaultAnnotationParam" )
@Singleton
@ArezComponent( nameIncludesId = true )
public abstract class SingletonWithIdModel
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

  @Observed
  void render()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }

  @Observed( executor = Executor.APPLICATION )
  public void render2( int i )
  {
  }

  @OnDepsChanged
  protected void onRender2DepsChanged()
  {
  }

  @Observed
  protected void myAutorun()
  {
  }
}
