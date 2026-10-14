package arez.integration.sting.sequencing;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.Observe;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;
import sting.Injectable;
import sting.Injector;

@NullMarked
public final class SchedulesPostConstructIntegrationTest extends AbstractArezIntegrationTest {

    private static final List<String> _events = new ArrayList<>();

    @NullMarked
    @Injectable
    static class MyDependency {
        MyDependency() {
            _events.add("MyDependency()");
        }

        @Override
        public @Nullable String toString() {
            return "MyDependency";
        }
    }

    @NullMarked
    @ArezComponent(sting = Feature.ENABLE)
    public abstract static class MyComponent {
        private final @Nullable MyDependency _dependency;

        MyComponent(final @Nullable MyDependency dependency) {
            _events.add("MyComponent()");
            _events.add("MyComponent._dependency = " + dependency);
            _dependency = dependency;
        }

        @PostConstruct
        void postConstruct() {
            _events.add("MyComponent.postConstruct()");
            _events.add("MyComponent._dependency = " + _dependency);
        }

        @Observe(depType = DepType.AREZ_OR_NONE)
        void run() {
            _events.add("MyComponent.run()");
            _events.add("MyComponent._dependency = " + _dependency);
        }
    }

    @NullMarked
    @Injector(includes = MyComponent.class, fragmentOnly = false)
    interface MyInjector {
        @Nullable
        MyComponent component1();

        static @Nullable MyInjector create() {
            return new SchedulesPostConstructIntegrationTest_Sting_MyInjector();
        }
    }

    @Test
    public void scenario() {
        final MyInjector injector = MyInjector.create();
        assertEquals(String.join("\n", _events), "");
        injector.component1();
        assertEquals(
                String.join("\n", _events),
                "MyDependency()\n" + "MyComponent()\n"
                        + "MyComponent._dependency = MyDependency\n"
                        + "MyComponent.postConstruct()\n"
                        + "MyComponent._dependency = MyDependency\n"
                        + "MyComponent.run()\n"
                        + "MyComponent._dependency = MyDependency");
    }
}
