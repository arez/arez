import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class ObservableTypeParametersModel<T extends Integer>
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
