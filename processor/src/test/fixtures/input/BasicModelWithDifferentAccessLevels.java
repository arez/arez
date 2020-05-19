import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class BasicModelWithDifferentAccessLevels
{
  private String _value;
  private long _time;

  BasicModelWithDifferentAccessLevels()
  {
  }

  public BasicModelWithDifferentAccessLevels( final String value, final long time )
  {
    _value = value;
    _time = time;
  }

  @Observable
  long getTime()
  {
    return _time;
  }

  @Observable
  public void setTime( final long time )
  {
    _time = time;
  }

  @Observable
  String getValue()
  {
    return _value;
  }

  @Observable
  public void setValue( final String value )
  {
    _value = value;
  }

  @Action
  public void doAction()
  {
  }

  @SuppressWarnings( "Arez:ProtectedMethod" )
  @Action
  protected void doAction2()
  {
  }

  @Action
  void doAction3()
  {
  }
}
