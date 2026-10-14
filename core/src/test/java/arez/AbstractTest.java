package arez;

import static org.testng.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(MessageCollector.class)
public abstract class AbstractTest {

    private final TestLogger _logger = new TestLogger();

    private final List<String> _observerErrors = new ArrayList<>();

    private boolean _ignoreObserverErrors;

    @BeforeMethod
    protected void beforeTest() throws Exception {
        BrainCheckTestUtil.resetConfig(false);
        ArezTestUtil.resetConfig(false);
        ArezTestUtil.enableZones();
        _logger.getEntries().clear();
        ArezTestUtil.setLogger(_logger);
        _ignoreObserverErrors = false;
        _observerErrors.clear();
        Arez.context().addObserverErrorHandler(this::onObserverError);
    }

    @AfterMethod
    protected void afterTest() {
        BrainCheckTestUtil.resetConfig(true);
        ArezTestUtil.resetConfig(true);
        if (!_ignoreObserverErrors && !_observerErrors.isEmpty()) {
            fail("Unexpected Observer Errors: " + String.join("\n", _observerErrors));
        }
    }

    final TestLogger getTestLogger() {
        return _logger;
    }

    private Field getField(final Class<?> type, final String fieldName) {
        Class<?> clazz = type;
        while (null != clazz && Object.class != clazz) {
            try {
                final Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (final Throwable t) {
                clazz = clazz.getSuperclass();
            }
        }

        Assert.fail();
        throw new IllegalStateException();
    }

    @SuppressWarnings("SameParameterValue")
    final void setField(final Object object, final String fieldName, @Nullable final Object value)
            throws IllegalAccessException {
        getField(object.getClass(), fieldName).set(object, value);
    }

    final Observer newReadWriteObserver(final ArezContext context) {
        return context.observer(new CountAndObserveProcedure(), Observer.Flags.READ_WRITE);
    }

    final void setupReadOnlyTransaction() {
        setupReadOnlyTransaction(Arez.context());
    }

    final void setupReadOnlyTransaction(final ArezContext context) {
        Transaction.setTransaction(null);
        setCurrentTransaction(context.observer(new CountAndObserveProcedure()));
    }

    final void setupReadWriteTransaction() {
        setupReadWriteTransaction(Arez.context());
    }

    private void setupReadWriteTransaction(final ArezContext context) {
        Transaction.setTransaction(null);
        setCurrentTransaction(newReadWriteObserver(context));
    }

    final void setCurrentTransaction(final Observer observer) {
        Transaction.setTransaction(null);
        final ArezContext context = observer.getContext();
        Transaction.setTransaction(
                new Transaction(context, null, ValueUtil.randomString(), observer.isMutation(), observer, false));
    }

    protected final void ignoreObserverErrors() {
        _ignoreObserverErrors = true;
    }

    protected static void assertInvariantFailure(final ThrowingRunnable throwingRunnable, final String message) {
        assertEquals(expectThrows(IllegalStateException.class, throwingRunnable).getMessage(), message);
    }

    protected final void assertDefaultToString(final Object object) {
        assertEquals(object.toString(), object.getClass().getName() + "@" + Integer.toHexString(object.hashCode()));
    }

    protected static void observeADependency() {
        Arez.context().observable().reportObserved();
    }

    private void onObserverError(
            final Observer observer, final ObserverError error, @Nullable final Throwable throwable) {
        final String message = "Observer: " + observer.getName() + " Error: " + error + " " + throwable;
        _observerErrors.add(message);
        if (!_ignoreObserverErrors) {
            System.out.println(message);
            if (null != throwable) {
                throwable.printStackTrace();
            }
        }
    }

    final List<String> getObserverErrors() {
        return _observerErrors;
    }
}
