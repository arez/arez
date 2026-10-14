package arez.integration.sting.package_access;

import static org.testng.Assert.*;

import arez.integration.AbstractArezIntegrationTest;
import arez.integration.sting.package_access.other.TestComponent2;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;
import sting.Injector;

@NullMarked
public final class ComponentInDifferentPackageIntegrationTest extends AbstractArezIntegrationTest {
    @NullMarked
    @Injector(includes = TestComponent2.class, fragmentOnly = false)
    interface MyInjector {
        @Nullable
        TestComponent2 getTestComponent2();
    }

    @Test
    public void scenario() {
        // This test mostly is checking that the code correctly compiles by ensuring that all the artifacts
        // have the correct access level
        final MyInjector injector = new ComponentInDifferentPackageIntegrationTest_Sting_MyInjector();

        assertNotNull(injector.getTestComponent2());
    }
}
