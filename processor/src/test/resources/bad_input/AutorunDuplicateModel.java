import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.Container;

@Container
public class AutorunDuplicateModel
{
  @Autorun( name = "doStuff" )
  void foo()
  {
  }

  @Autorun
  void doStuff()
  {
  }
}
