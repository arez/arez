package arez.integration.memoize;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class MemoizeWriteOutsideTransactionIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void explicitEnableOnParameterizedMemoize() {
        final var element = new MemoizeWriteOutsideTransactionIntegrationTest_Arez_ExplicitElement();
        final var first = new ArrayList<Integer>();
        final var second = new ArrayList<Integer>();
        observer(() -> first.add(element.value(1)));
        observer(() -> second.add(element.value(2)));

        element._value = 10;
        element.getValueComputableValue(1).reportPossiblyChanged();
        assertEquals(first, List.of(1, 11));
        assertEquals(second, List.of(2));
        element.getValueComputableValue(2).reportPossiblyChanged();
        assertEquals(second, List.of(2, 12));
    }

    @Test
    public void inheritsComponentDefaultAndAllowsExplicitDisable() {
        final var element = new MemoizeWriteOutsideTransactionIntegrationTest_Arez_InheritedElement();
        final var observed = new ArrayList<Integer>();
        observer(() -> observed.add(element.value() + element.pure()));

        element._value = 10;
        element.getValueComputableValue().reportPossiblyChanged();
        assertEquals(observed, List.of(1, 11));

        assertInvariant(
                element.getDisabledComputableValue()::reportPossiblyChanged,
                "Arez-0117: Attempting to get current transaction but no transaction is active.");
        Arez.context().safeAction(element.getDisabledComputableValue()::reportPossiblyChanged);
    }

    @Test
    public void disabledComponentDefaultRequiresTransaction() {
        final var element = new MemoizeWriteOutsideTransactionIntegrationTest_Arez_ExplicitElement();
        assertInvariant(
                element.getDisabledComputableValue()::reportPossiblyChanged,
                "Arez-0117: Attempting to get current transaction but no transaction is active.");
    }

    @NullMarked
    @ArezComponent(defaultWriteOutsideTransaction = Feature.DISABLE)
    abstract static class ExplicitElement {
        int _value;

        @Memoize(depType = DepType.AREZ_OR_EXTERNAL, writeOutsideTransaction = Feature.ENABLE)
        int value(final int offset) {
            return _value + offset;
        }

        @ComputableValueRef
        abstract ComputableValue<Integer> getValueComputableValue(int offset);

        @Memoize(depType = DepType.AREZ_OR_EXTERNAL)
        int disabled() {
            return _value;
        }

        @ComputableValueRef
        abstract ComputableValue<Integer> getDisabledComputableValue();
    }

    @NullMarked
    @ArezComponent(defaultWriteOutsideTransaction = Feature.ENABLE)
    abstract static class InheritedElement {
        int _value;

        @Memoize(depType = DepType.AREZ_OR_EXTERNAL)
        int value() {
            return _value;
        }

        @ComputableValueRef
        abstract ComputableValue<Integer> getValueComputableValue();

        @Memoize(depType = DepType.AREZ_OR_EXTERNAL, writeOutsideTransaction = Feature.DISABLE)
        int disabled() {
            return _value;
        }

        @ComputableValueRef
        abstract ComputableValue<Integer> getDisabledComputableValue();

        @Memoize(depType = DepType.AREZ_OR_NONE)
        int pure() {
            return 1;
        }
    }
}
