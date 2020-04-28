package arez.doc.examples.reference;

import arez.Arez;
import arez.component.TypeBasedLocator;

public class ReferenceExample
{
  public static void main( String[] args )
  {
    //EXAMPLE START
    final GroupRepository groupRepository = new Arez_GroupRepository();
    final PermissionRepository permissionRepository = new Arez_PermissionRepository();
    final UserRepository userRepository = new Arez_UserRepository();

    final TypeBasedLocator locator = new TypeBasedLocator();
    locator.registerLookup( Group.class, id -> groupRepository.findById( (Integer) id ) );
    locator.registerLookup( Permission.class, id -> permissionRepository.findById( (Integer) id ) );
    locator.registerLookup( User.class, id -> userRepository.findById( (Integer) id ) );

    Arez.context().registerLocator( locator );
    //EXAMPLE END
  }
}
