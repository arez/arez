package arez.integration.inverse;

import static org.testng.Assert.*;

import arez.Arez;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.PostInverseAdd;
import arez.annotations.PreInverseRemove;
import arez.component.Linkable;
import arez.component.TypeBasedLocator;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.inverse.other.CrossPackageReferences;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class CrossPackageInverseIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void relationships() {
        final List<String> trace = new ArrayList<>();
        final var first = Owner.create("first", trace);
        final var second = Owner.create("second", trace);
        final var locator = new TypeBasedLocator();
        locator.registerLookup(Owner.class, id -> Integer.valueOf(1).equals(id) ? first : second);
        Arez.context().registerLocator(locator);

        final var changes = new AtomicInteger();
        final var inverseObserver = observer(() -> {
            first.getChildren();
            changes.incrementAndGet();
        });
        final var many = safeAction(() -> CrossPackageReferences.Many.create(1));
        final var one = CrossPackageReferences.One.create(1);
        final var optional = CrossPackageReferences.Optional.create(1);
        safeAction(() -> {
            assertSame(one.getOwner(), first);
            Linkable.link(optional);
        });
        assertEquals(first.getChildren(), List.of(many));
        assertSame(first.getRequired(), one);
        assertSame(first.getOptional(), optional);
        assertEquals(changes.get(), 2);

        safeAction(() -> {
            many.setOwnerId(2);
            one.setOwnerId(2);
            assertSame(one.getOwner(), second);
            optional.setOwnerId(2);
            Linkable.link(optional);
        });
        assertTrue(first.getChildren().isEmpty());
        assertNull(first.getOptional());
        assertEquals(second.getChildren(), List.of(many));
        assertSame(second.getRequired(), one);
        assertSame(second.getOptional(), optional);
        assertEquals(changes.get(), 3);
        assertEquals(trace, List.of("first+", "first-", "second+"));
        Disposable.dispose(inverseObserver);

        for (final Object component : List.of(first, many, one, optional)) {
            assertFalse(Modifier.isPublic(component.getClass().getModifiers()));
            for (final var constructor : component.getClass().getDeclaredConstructors()) {
                assertFalse(Modifier.isPublic(constructor.getModifiers()));
            }
            for (final var method : component.getClass().getDeclaredMethods()) {
                if (method.getName().startsWith("$$arezir$$_")
                        || method.getName().startsWith("$$arezi$$_delink_")) {
                    assertFalse(Modifier.isPublic(method.getModifiers()));
                }
            }
        }

        Disposable.dispose(many);
        Disposable.dispose(one);
        assertTrue(second.getChildren().isEmpty());
        assertEquals(trace, List.of("first+", "first-", "second+", "second-"));
        Disposable.dispose(second);
        final var optionalError = expectThrows(
                IllegalStateException.class,
                () -> safeAction(() -> {
                    optional.getOwner();
                }));
        assertTrue(optionalError.toString().contains("reference has not been resolved"));
        Disposable.dispose(optional);

        final var remainingMany = safeAction(() -> CrossPackageReferences.Many.create(1));
        final var remainingOne = CrossPackageReferences.One.create(1);
        safeAction(() -> {
            remainingOne.getOwner();
        });
        Disposable.dispose(first);
        final var manyError = expectThrows(
                IllegalStateException.class,
                () -> safeAction(() -> {
                    remainingMany.getOwner();
                }));
        assertTrue(manyError.toString().contains("reference has not been resolved"));
        Disposable.dispose(remainingMany);
        Disposable.dispose(remainingOne);
    }

    @NullMarked
    @ArezComponent(defaultReadOutsideTransaction = Feature.ENABLE)
    public abstract static class Owner {
        private final String _name;
        private final List<String> _trace;

        Owner(final String name, final List<String> trace) {
            _name = name;
            _trace = trace;
        }

        static Owner create(final String name, final List<String> trace) {
            return new CrossPackageInverseIntegrationTest_Arez_Owner(name, trace);
        }

        @Inverse(referenceName = "owner")
        abstract Collection<CrossPackageReferences.Many> getChildren();

        @Inverse(referenceName = "owner")
        abstract CrossPackageReferences.One getRequired();

        @Inverse(referenceName = "owner")
        abstract CrossPackageReferences.@Nullable Optional getOptional();

        @PostInverseAdd
        void postChildrenAdd(final CrossPackageReferences.Many child) {
            _trace.add(_name + "+");
        }

        @PreInverseRemove
        void preChildrenRemove(final CrossPackageReferences.Many child) {
            _trace.add(_name + "-");
        }
    }
}
