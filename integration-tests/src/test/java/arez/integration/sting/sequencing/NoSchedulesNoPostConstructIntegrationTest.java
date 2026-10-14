package arez.integration.sting.sequencing;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import sting.Injectable;
import sting.Injector;

public final class NoSchedulesNoPostConstructIntegrationTest extends AbstractArezIntegrationTest {
    @Nonnull
    private static final List<String> _events = new ArrayList<>();

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

    @ArezComponent(sting = Feature.ENABLE, allowEmpty = true)
    public abstract static class MyComponent {
        MyComponent(final MyDependency dependency) {
            _events.add("MyComponent()");
            _events.add("MyComponent._dependency = " + dependency);
        }
    }

    @Injector(includes = MyComponent.class, fragmentOnly = false)
    interface MyInjector {
        MyComponent component1();

        static MyInjector create() {
            return new NoSchedulesNoPostConstructIntegrationTest_Sting_MyInjector();
        }
    }

    @Test
    public void scenario() {
        final MyInjector injector = MyInjector.create();
        assertEquals(String.join("\n", _events), "");
        assertNotNull(injector.component1());
        assertEquals(
                String.join("\n", _events),
                "MyDependency()\n" + "MyComponent()\n" + "MyComponent._dependency = MyDependency");
    }
}
