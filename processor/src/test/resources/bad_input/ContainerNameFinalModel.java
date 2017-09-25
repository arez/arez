import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerName;

@Container
public class ContainerNameFinalModel
{
  @Action
  void myAction()
  {
  }

  @ContainerName
  final String getTypeName()
  {
    return null;
  }
}
