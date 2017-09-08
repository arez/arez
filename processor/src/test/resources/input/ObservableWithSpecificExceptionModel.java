import java.text.ParseException;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
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
