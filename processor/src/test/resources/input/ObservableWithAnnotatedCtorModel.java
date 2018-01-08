import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

@SuppressWarnings( "WeakerAccess" )
@ArezComponent
public class ObservableWithAnnotatedCtorModel
{
  public ObservableWithAnnotatedCtorModel( @Nonnegative final long time,
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
