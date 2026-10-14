package arez.integration.lifecycle;

import static org.testng.Assert.*;

import arez.ArezTestUtil;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.PostConstruct;
import arez.annotations.PostDispose;
import arez.annotations.PreDispose;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class LifecycleSequencingIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final var model = Model1.create();

        Disposable.dispose(model);

        assertEquals(String.join(" ", model._steps), "Constructor PostConstruct PreDispose PostDispose");
    }

    @Test
    public void scenario_NativeComponentsDisabled() {
        ArezTestUtil.disableNativeComponents();
        final var model = Model2.create();

        Disposable.dispose(model);

        assertEquals(String.join(" ", model._steps), "Constructor PostConstruct PreDispose PostDispose");
    }

    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class Model1 {
        final ArrayList<String> _steps = new ArrayList<>();

        static Model1 create() {
            return new LifecycleSequencingIntegrationTest_Arez_Model1();
        }

        Model1() {
            _steps.add("Constructor");
        }

        @PostConstruct
        void postConstruct() {
            _steps.add("PostConstruct");
        }

        @PreDispose
        void preDispose() {
            _steps.add("PreDispose");
        }

        @PostDispose
        void postDispose() {
            _steps.add("PostDispose");
        }
    }

    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class Model2 {
        final ArrayList<String> _steps = new ArrayList<>();

        static Model2 create() {
            return new LifecycleSequencingIntegrationTest_Arez_Model2();
        }

        Model2() {
            _steps.add("Constructor");
        }

        @PostConstruct
        void postConstruct() {
            _steps.add("PostConstruct");
        }

        @PreDispose
        void preDispose() {
            _steps.add("PreDispose");
        }

        @PostDispose
        void postDispose() {
            _steps.add("PostDispose");
        }
    }
}
