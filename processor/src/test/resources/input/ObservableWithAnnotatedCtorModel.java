import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import javax.annotation.Nonnull;

@SuppressWarnings( "WeakerAccess" )
@ArezComponent
public abstract class ObservableWithAnnotatedCtorModel
{
  public ObservableWithAnnotatedCtorModel( final long time,
                                           @SuppressWarnings( "" ) final long other,
                                           @Nonnull final String foo )
  {
  }

  @Observable
  public long getTime()
  {
    return 0;
  }

  @Observable
  public void setTime( final long time )
  {
  }
}
