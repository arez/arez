package arez.integration.observable;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ObservableInitialIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final ArezContext context = Arez.context();
        final PersonModel person = PersonModel.create();

        safeAction(() -> assertEquals(person.getName(), "Bob"));
        safeAction(() -> assertEquals(person.getAge(), 42));
        safeAction(() -> assertEquals(person.getNickname(), "Bobby"));

        context.action(() -> {
            person.setName("Jane");
            person.setAge(37);
            person.setNickname(null);
        });

        safeAction(() -> assertEquals(person.getName(), "Jane"));
        safeAction(() -> assertEquals(person.getAge(), 37));
        safeAction(() -> assertNull(person.getNickname()));
    }

    @NullMarked
    @ArezComponent
    abstract static class PersonModel {

        static PersonModel create() {
            return new ObservableInitialIntegrationTest_Arez_PersonModel();
        }

        @Observable
        abstract String getName();

        abstract void setName(String name);

        @Observable
        abstract int getAge();

        abstract void setAge(int age);

        @Observable
        @Nullable
        abstract String getNickname();

        abstract void setNickname(@Nullable String nickname);

        @ObservableInitial
        static final String INITIAL_NAME = "Bob";

        @ObservableInitial
        static int getInitialAge() {
            return 42;
        }

        @ObservableInitial(name = "nickname")
        static @Nullable String initialNickname() {
            return "Bobby";
        }
    }
}
