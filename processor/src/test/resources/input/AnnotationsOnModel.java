import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Observable;

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
