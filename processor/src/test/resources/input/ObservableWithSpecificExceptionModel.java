import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.text.ParseException;

@ArezComponent
public class ObservableWithSpecificExceptionModel
{
  @Observable
  public long getTime()
    throws ParseException
  {
    return 0;
  }

  @Observable
  public void setTime( final long time )
    throws ParseException
  {
  }
}
