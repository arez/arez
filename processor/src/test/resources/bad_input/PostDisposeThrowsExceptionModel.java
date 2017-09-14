import java.text.ParseException;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.PostDispose;

@Container
public class PostDisposeThrowsExceptionModel
{
  @PostDispose
  void doStuff()
    throws ParseException
  {
  }
}
