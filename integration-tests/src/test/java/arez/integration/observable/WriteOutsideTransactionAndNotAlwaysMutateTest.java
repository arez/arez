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
public final class WriteOutsideTransactionAndNotAlwaysMutateTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent
    abstract static class MyComponent {

        private String _name = "";

        @Observable(writeOutsideTransaction = Feature.ENABLE, setterAlwaysMutates = false)
        String getName() {
            return _name;
        }

        void setName(String name) {
            _name = name.trim();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void scenario() {
        final MyComponent component = new WriteOutsideTransactionAndNotAlwaysMutateTest_Arez_MyComponent();

        final AtomicInteger callCount = new AtomicInteger();

        observer(() -> {
            component.getName();
            callCount.incrementAndGet();
        });

        assertEquals(callCount.get(), 1);

        // Write outside transaction
        component.setName("ABC");

        // If gets here it reportObserved so observer re-ran
        assertEquals(callCount.get(), 2);

        safeAction(() -> assertEquals(component.getName(), "ABC"));

        // Write inside transaction
        safeAction(() -> component.setName("DEF"));

        assertEquals(callCount.get(), 3);

        safeAction(() -> assertEquals(component.getName(), "DEF"));

        // Write but produce no changes
        component.setName("    DEF    ");

        assertEquals(callCount.get(), 3);

        safeAction(() -> assertEquals(component.getName(), "DEF"));
    }
}
