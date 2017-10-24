import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
public class ObservableTypeParametersModel<T extends Integer>
{
  @Observable
  public T getTime()
  {
    return null;
  }

  public void setTime( final T time )
  {
  }
}
