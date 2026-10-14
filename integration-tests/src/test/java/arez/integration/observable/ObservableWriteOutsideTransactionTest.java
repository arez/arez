package arez.integration.observable;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class ObservableWriteOutsideTransactionTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent
    abstract static class MyComponent {
        @Observable(writeOutsideTransaction = Feature.ENABLE)
        abstract long getTime();

        abstract void setTime(long value);
    }

    @Test
    public void scenario() {
        final MyComponent component = new ObservableWriteOutsideTransactionTest_Arez_MyComponent();

        final AtomicInteger callCount = new AtomicInteger();

        observer(() -> {
            component.getTime();
            callCount.incrementAndGet();
        });

        assertEquals(callCount.get(), 1);

        // Write outside transaction
        component.setTime(37L);

        // If gets here it reportObserved so observer re-ran
        assertEquals(callCount.get(), 2);

        safeAction(() -> assertEquals(component.getTime(), 37L));

        // Write inside transaction
        safeAction(() -> component.setTime(42L));

        assertEquals(callCount.get(), 3);

        safeAction(() -> assertEquals(component.getTime(), 42L));
    }
}
