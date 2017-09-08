import java.text.ParseException;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;

@Container
public class UnsafeSpecificFunctionActionModel
{
  @Action
  public int doStuff( final long time )
    throws ParseException
  {
    return 0;
  }
}
