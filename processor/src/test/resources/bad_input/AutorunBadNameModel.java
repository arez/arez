import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.Container;

@Container
public class AutorunBadNameModel
{
  @Autorun( name = "-ace" )
  void foo()
  {
  }
}
