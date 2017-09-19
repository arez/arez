import java.text.ParseException;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.Container;

@Container
public class AutorunThrowsExceptionModel
{
  @Autorun
  void doStuff()
    throws ParseException
  {
  }
}
