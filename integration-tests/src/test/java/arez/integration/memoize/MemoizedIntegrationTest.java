package arez.integration.memoize;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Component;
import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.component.ComponentObservable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class MemoizedIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final ArezContext context = Arez.context();

        final AtomicInteger[] searchCounts =
                new AtomicInteger[] {new AtomicInteger(), new AtomicInteger(), new AtomicInteger(), new AtomicInteger()
                };

        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final PersonModel person = PersonModel.create("Bill", 15);

        context.observer("SearchResult - red", () -> {
            observeADependency();
            if (Disposable.isNotDisposed(person)) {
                recorder.mark("doesSearchMatch - red", person.doesSearchMatch("red"));
            }
            searchCounts[0].incrementAndGet();
        });
        final Observer observer3 = context.observer("SearchResult - ill", () -> {
            observeADependency();
            recorder.mark("doesSearchMatch - ill", person.doesSearchMatch("ill"));
            searchCounts[1].incrementAndGet();
        });
        context.observer("SearchResult - red - 20", () -> {
            observeADependency();
            if (Disposable.isNotDisposed(person)) {
                recorder.mark("doesSearchMatch - red", person.doesFullSearchMatch("red", 20));
            }
            searchCounts[2].incrementAndGet();
        });
        context.observer("SearchResult - red - 5", () -> {
            observeADependency();
            if (Disposable.isNotDisposed(person)) {
                recorder.mark("doesSearchMatch - red", person.doesFullSearchMatch("red", 5));
            }
            searchCounts[3].incrementAndGet();
        });

        for (final AtomicInteger count : searchCounts) {
            count.set(0);
        }

        context.action("Update 0", () -> person.setName("Gill"));
        context.action("Update 1", () -> person.setName("Fred"));
        context.action("Update 2", () -> person.setName("Donald"));

        observer3.dispose();

        context.action("Update 3", () -> person.setName("Fred"));
        context.action("Update 3", () -> person.setName("Bill"));

        assertEquals(searchCounts[0].get(), 4);
        assertEquals(searchCounts[1].get(), 1);
        assertEquals(searchCounts[2].get(), 0);
        assertEquals(searchCounts[3].get(), 4);

        Disposable.dispose(person);

        assertMatchesFixture(recorder);
        assertEquals(searchCounts[0].get(), 5);
        assertEquals(searchCounts[1].get(), 1);
        assertEquals(searchCounts[2].get(), 1);
        assertEquals(searchCounts[3].get(), 5);
    }

    @Test
    public void scenario_actionOnly() throws Throwable {
        final ArezContext context = Arez.context();

        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final PersonModel person = PersonModel.create("Bill", 15);

        context.action("Query 1", () -> person.doesSearchMatch("ill"));
        context.action("Query 2", () -> person.doesSearchMatch("red"));

        assertMatchesFixture(recorder);
    }

    @Test
    public void scenario_disposeWithoutDeactivate() throws Throwable {
        final ArezContext context = Arez.context();

        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final PersonModel person = PersonModel.create("Bill", 15);

        context.observer(
                "SearchResult - red - 20",
                () -> {
                    if (ComponentObservable.observe(person)) {
                        recorder.mark("doesSearchMatch - red", person.doesFullSearchMatch("red", 20));
                    }
                },
                Observer.Flags.AREZ_OR_NO_DEPENDENCIES);

        Disposable.dispose(person);

        assertMatchesFixture(recorder);
    }

    @Test
    public void scenario_disposeNoNativeComponentsEnabled() throws Throwable {
        ArezTestUtil.disableNativeComponents();

        final ArezContext context = Arez.context();

        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final PersonModel person = PersonModel.create("Bill", 15);

        context.observer(
                "SearchResult - red - 20",
                () -> {
                    if (ComponentObservable.observe(person)) {
                        recorder.mark("doesSearchMatch - red", person.doesFullSearchMatch("red", 20));
                    }
                },
                Observer.Flags.AREZ_OR_NO_DEPENDENCIES);

        Disposable.dispose(person);

        assertMatchesFixture(recorder);
    }

    @NullMarked
    @SuppressWarnings("WeakerAccess")
    @ArezComponent(observable = Feature.ENABLE)
    public abstract static class PersonModel {
        private @Nullable String _name;
        private int _age;

        public static PersonModel create(final String name, final int age) {
            return new MemoizedIntegrationTest_Arez_PersonModel(name, age);
        }

        PersonModel(final String name, final int age) {
            _name = name;
            _age = age;
        }

        @ComponentRef
        abstract @Nullable Component getComponent();

        @Observable
        public @Nullable String getName() {
            return _name;
        }

        public void setName(final String name) {
            _name = name;
        }

        @Observable
        public int getAge() {
            return _age;
        }

        public void setAge(final int age) {
            _age = age;
        }

        @Memoize
        public boolean doesSearchMatch(final String value) {
            return getName().contains(value);
        }

        @Memoize
        public boolean doesFullSearchMatch(final String value, final int minAge) {
            return getName().contains(value) && getAge() >= minAge;
        }
    }
}
