import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.ContainerNamePrefix;

@Container
public class ContainerNamePrefixMustNotHaveParametersModel
{
  @Action
  void myAction()
  {
  }

  @ContainerNamePrefix
  String getTypeName( int i )
  {
    return null;
  }
}
