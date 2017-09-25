import java.io.IOException;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerNamePrefix;

@Container
public class ContainerNamePrefixThrowsExceptionModel
{
  @Action
  void myAction()
  {
  }

  @ContainerNamePrefix
  String getTypeName()
    throws IOException
  {
    return null;
  }
}
