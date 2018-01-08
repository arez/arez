import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observable;
import javax.annotation.Nonnull;

@ArezComponent
public class AnnotationsOnModel
{
  @Observable
  @Nonnull
  @SuppressWarnings( "ThisANnotationShouldNotBeCopied" )
  public String getTime()
  {
    return "";
  }

  @Observable
  @SuppressWarnings( "ThisANnotationShouldNotBeCopied" )
  public void setTime( @Nonnull @SuppressWarnings( "ThisANnotationShouldNotBeCopied" ) final String time )
  {
  }

  @Action
  @SuppressWarnings( "ThisANnotationShouldNotBeCopied" )
  public void doStuff( @Nonnull @SuppressWarnings( "ThisANnotationShouldNotBeCopied" ) final String time )
  {
  }

  @Computed
  @Nonnull
  @SuppressWarnings( "ThisANnotationShouldNotBeCopied" )
  public Integer someValue()
  {
    return 0;
  }
}
