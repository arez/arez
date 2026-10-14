package arez.integration.sting.sequencing;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;
import sting.Injectable;
import sting.Injector;

@NullMarked
public final class NoSchedulesNoPostConstructIntegrationTest extends AbstractArezIntegrationTest {

    private static final List<String> _events = new ArrayList<>();

    @NullMarked
    @Injectable
    static class MyDependency {
        MyDependency() {
            _events.add("MyDependency()");
        }

        @Override
        public String toString() {
            return "MyDependency";
        }
    }

    @NullMarked
    @ArezComponent(sting = Feature.ENABLE, allowEmpty = true)
    public abstract static class MyComponent {
        MyComponent(final @Nullable MyDependency dependency) {
            _events.add("MyComponent()");
            _events.add("MyComponent._dependency = " + dependency);
        }
    }

    @NullMarked
    @Injector(includes = MyComponent.class, fragmentOnly = false)
    interface MyInjector {
        @Nullable
        MyComponent component1();

        static MyInjector create() {
            return new NoSchedulesNoPostConstructIntegrationTest_Sting_MyInjector();
        }
    }

    @Test
    public void scenario() {
        final var injector = MyInjector.create();
        assertEquals(String.join("\n", _events), "");
        assertNotNull(injector.component1());
        assertEquals(
                String.join("\n", _events),
                "MyDependency()\n" + "MyComponent()\n" + "MyComponent._dependency = MyDependency");
    }
}
