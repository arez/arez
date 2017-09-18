import javax.annotation.PostConstruct;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class PostConstructMustNotHaveParametersModel
{
  @PostConstruct
  void postConstruct( int x )
  {
  }

  @Computed
  public int someValue()
  {
    return 0;
  }
}
