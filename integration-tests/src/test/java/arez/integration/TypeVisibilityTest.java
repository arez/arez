package arez.integration;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.lang.reflect.Modifier;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class TypeVisibilityTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent
    public abstract static class PersonModel {
        PersonModel() {}

        @Observable
        String getFirstName() {
            return "";
        }

        void setFirstName(final String firstName) {}
    }

    @NullMarked
    @ArezComponent
    public abstract static class PersonModel2 {
        @Observable
        String getFirstName() {
            return "";
        }

        void setFirstName(final String firstName) {}
    }

    @Test
    public void typeVisibilities() {
        assertFalse(Modifier.isPublic(TypeVisibilityTest_Arez_PersonModel.class.getModifiers()));
        assertFalse(Modifier.isPublic(TypeVisibilityTest_Arez_PersonModel2.class.getModifiers()));
    }
}
