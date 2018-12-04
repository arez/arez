import arez.Observer;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Executor;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChange;
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

  @Memoize
  public int someValue()
  {
    return 0;
  }

  @Observe
  void render()
  {
  }

  @OnDepsChange
  public void onRenderDepsChange()
  {
  }

  @ObserverRef
  abstract Observer getRenderObserver();

  @Observe( executor = Executor.EXTERNAL )
  public void render2( int i )
  {
  }

  @OnDepsChange
  protected void onRender2DepsChange()
  {
  }

  @Observe
  protected void myAutorun()
  {
  }
}
