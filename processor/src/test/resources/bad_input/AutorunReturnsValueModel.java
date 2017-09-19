import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.Container;

@Container
public class AutorunReturnsValueModel
{
  @Autorun
  int doStuff()
  {
    return 0;
  }
}
