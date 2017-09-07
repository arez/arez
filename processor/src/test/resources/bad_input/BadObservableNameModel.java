import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class BadObservableNameModel
{
  private long _field;

  @Observable( name = "-ace" )
  public long getField()
  {
    return _field;
  }

  @Observable( name = "-ace" )
  public void setField( final long field )
  {
    _field = field;
  }
}
