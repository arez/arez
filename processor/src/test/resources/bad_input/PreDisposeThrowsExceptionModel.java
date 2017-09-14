import java.text.ParseException;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PreDispose;

@Container
public class PreDisposeThrowsExceptionModel
{
  @PreDispose
  void doStuff()
    throws ParseException
  {
  }
}
