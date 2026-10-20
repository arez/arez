package arez.persist;

import static org.testng.Assert.*;

import arez.persist.runtime.ArezPersistTestUtil;
import arez.testng.ArezTestSupport;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractTest implements ArezTestSupport {
    private final TestLogger _logger = new TestLogger();

    @BeforeMethod
    public void preTest() throws Exception {
        ArezTestSupport.super.preTest();
        ArezPersistTestUtil.resetConfig(false);
        _logger.getEntries().clear();
        ArezPersistTestUtil.setLogger(_logger);
    }

    @AfterMethod
    public void postTest() {
        ArezPersistTestUtil.resetConfig(true);
        ArezTestSupport.super.postTest();
    }

    protected final TestLogger getTestLogger() {
        return _logger;
    }

    protected final void assertDefaultToString(final Object object) {
        assertEquals(object.toString(), object.getClass().getName() + "@" + Integer.toHexString(object.hashCode()));
    }

    protected final void assertInvariantFailure(final ThrowingRunnable throwingRunnable, final String message) {
        assertEquals(expectThrows(IllegalStateException.class, throwingRunnable).getMessage(), message);
    }

    protected final Map<String, @Nullable Object> randomState() {
        final HashMap<String, @Nullable Object> state = new HashMap<>();
        state.put(ValueUtil.randomString(), ValueUtil.randomString());
        return state;
    }
}
