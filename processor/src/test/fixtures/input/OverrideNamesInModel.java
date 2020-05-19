import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import java.text.ParseException;

@ArezComponent( name = "MyContainer" )
public abstract class OverrideNamesInModel
{
  @Observable( name = "myField" )
  public long getTime()
  {
    return 0;
  }

  @Observable( name = "myField" )
  public void setTime( final long time )
  {
  }

  @Action( name = "myAction" )
  public void doAction()
  {
  }

  @Observe( name = "zzzzzz" )
  void zapZap()
  {
  }

  @Memoize( name = "myMemoized" )
  int compute()
  {
    return 0;
  }

  @Observe( executor = Executor.EXTERNAL, name = "XX" )
  public void render()
    throws ParseException
  {
  }

  @OnDepsChange( name = "XX" )
  void onRenderDepsChange()
  {
  }
}
