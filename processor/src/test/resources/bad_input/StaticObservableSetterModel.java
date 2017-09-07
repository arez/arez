import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class StaticObservableSetterModel
{
  @Observable
  public long getField()
  {
    return 1;
  }

  @Observable
  public static void setField( final long field )
  {
  }
}
