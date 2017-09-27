import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
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
