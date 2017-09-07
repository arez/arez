import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class BasicModelWithDifferentAccessLevels
{
  private String _value;
  private long _time;

  BasicModelWithDifferentAccessLevels()
  {
  }

  protected BasicModelWithDifferentAccessLevels( final String value )
  {
    _value = value;
  }

  public BasicModelWithDifferentAccessLevels( final String value, final long time )
  {
    _value = value;
    _time = time;
  }

  @Observable
  protected long getTime()
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

  @Action
  protected void doAction2()
  {
  }

  @Action
  void doAction3()
  {
  }
}
