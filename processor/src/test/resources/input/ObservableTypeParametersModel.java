import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container
public class ObservableTypeParametersModel
{
  @Observable
  public <T extends Integer> T getTime()
  {
    return null;
  }

  public <T extends Integer> void setTime( final T time )
  {
  }
}
