package arez.doc.examples.reference;

import arez.Arez;
import arez.component.TypeBasedLocator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ReferenceExample {
    // DOC ELIDE START
    private ReferenceExample() {}
    // DOC ELIDE END
    public static void main(String @Nullable [] args) {
        // EXAMPLE START
        final GroupRepository groupRepository = new Arez_GroupRepository();
        final PermissionRepository permissionRepository = new Arez_PermissionRepository();
        final UserRepository userRepository = new Arez_UserRepository();

        final var locator = new TypeBasedLocator();
        locator.registerLookup(Group.class, id -> groupRepository.findById((Integer) id));
        locator.registerLookup(Permission.class, id -> permissionRepository.findById((Integer) id));
        locator.registerLookup(User.class, id -> userRepository.findById((Integer) id));

        Arez.context().registerLocator(locator);
        // EXAMPLE END
    }
}
