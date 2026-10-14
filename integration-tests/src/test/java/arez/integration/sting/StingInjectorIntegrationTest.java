package arez.integration.sting;

import static org.testng.Assert.*;

import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import sting.Injector;

@NullMarked
public final class StingInjectorIntegrationTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent(sting = Feature.ENABLE)
    public abstract static class TestComponent {
        @Observable
        abstract @Nullable String getValue();

        abstract void setValue(@Nullable String value);
    }

    @NullMarked
    @Injector(includes = TestComponent.class, fragmentOnly = false)
    interface MyInjector {
        @Nullable
        TestComponent component();
    }

    @Test
    public void scenario() throws Throwable {
        final MyInjector injector = new StingInjectorIntegrationTest_Sting_MyInjector();
        final TestComponent component = injector.component();

        final ArezContext context = Arez.context();
        context.action(() -> assertNull(component.getValue()), ActionFlags.READ_ONLY);
        final String value = ValueUtil.randomString();
        context.action(() -> component.setValue(value));
        context.action(() -> assertEquals(component.getValue(), value), ActionFlags.READ_ONLY);
    }
}
