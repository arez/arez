package arez.integration.dispose;

import static org.testng.Assert.*;

import arez.Arez;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.PreDispose;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

@NullMarked
public final class AllowWriteInDisposeIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final String name = ValueUtil.randomString();
        final var model1 = Model1.create(name);
        final var model2 = Model2.create(model1);

        final var callCount = new AtomicInteger();
        Arez.context().observer(() -> {
            // Perform observation
            model1.getName();
            callCount.incrementAndGet();
        });

        safeAction(() -> assertEquals(model1.getName(), name));
        assertEquals(callCount.get(), 1);

        Disposable.dispose(model2);

        safeAction(() -> assertEquals(model1.getName(), "X"));
        assertEquals(callCount.get(), 2);
    }

    @NullMarked
    @ArezComponent
    abstract static class Model1 {

        static Model1 create(final String name) {
            return new AllowWriteInDisposeIntegrationTest_Arez_Model1(name);
        }

        @Observable
        abstract String getName();

        abstract void setName(String name);
    }

    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class Model2 {
        @SuppressWarnings("Arez:UnmanagedComponentReference")
        private final Model1 _other;

        static Model2 create(final Model1 other) {
            return new AllowWriteInDisposeIntegrationTest_Arez_Model2(other);
        }

        Model2(final Model1 other) {
            _other = other;
        }

        @PreDispose
        void preDispose() {
            _other.setName("X");
        }
    }
}
