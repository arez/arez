import java.io.IOException;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerName;

@Container
public class ContainerNameThrowsExceptionModel
{
  @Action
  void myAction()
  {
  }

  @ContainerName
  String getTypeName()
    throws IOException
  {
    return null;
  }
}
