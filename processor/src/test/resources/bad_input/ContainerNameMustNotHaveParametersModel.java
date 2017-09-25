import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerName;

@Container
public class ContainerNameMustNotHaveParametersModel
{
  @Action
  void myAction()
  {
  }

  @ContainerName
  String getTypeName( int i )
  {
    return null;
  }
}
