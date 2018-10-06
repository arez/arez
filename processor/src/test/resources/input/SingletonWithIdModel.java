import arez.Observer;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Executor;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
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

  @Observe
  void render()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }

  @ObserverRef
  abstract Observer getRenderObserver();

  @Observe( executor = Executor.APPLICATION )
  public void render2( int i )
  {
  }

  @OnDepsChanged
  protected void onRender2DepsChanged()
  {
  }

  @Observe
  protected void myAutorun()
  {
  }
}
