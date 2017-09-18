import javax.annotation.PostConstruct;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class PostConstructNotStaticModel
{
  @PostConstruct
  static void postConstruct()
  {
  }

  @Computed
  public int someValue()
  {
    return 0;
  }
}
