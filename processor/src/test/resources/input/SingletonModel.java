import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Executor;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent( nameIncludesId = false )
public abstract class SingletonModel
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

  @Observe( executor = Executor.APPLICATION )
  public void render()
  {
  }

  @OnDepsChange
  public void onRenderDepsChange()
  {
  }

  @Observe
  protected void myAutorun()
  {
  }
}
