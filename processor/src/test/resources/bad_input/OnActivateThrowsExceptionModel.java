import java.text.ParseException;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnActivate;

@Container
public class OnActivateThrowsExceptionModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate
  void onMyValueActivate()
    throws ParseException
  {
  }
}
