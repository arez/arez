import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Executor;
import arez.annotations.Observable;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;
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

  @Observed( name = "zzzzzz" )
  protected void zapZap()
  {
  }

  @Computed( name = "myComputed" )
  int compute()
  {
    return 0;
  }

  @Observed( executor = Executor.APPLICATION,name = "XX" )
  public void render()
    throws ParseException
  {
  }

  @OnDepsChanged( name = "XX" )
  public void onRenderDepsChanged()
  {
  }
}
