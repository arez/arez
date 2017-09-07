import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class StaticObservableGetterModel
{
  @Observable
  public static long getField()
  {
    return 1;
  }

  @Observable
  public void setField( final long field )
  {
  }
}
