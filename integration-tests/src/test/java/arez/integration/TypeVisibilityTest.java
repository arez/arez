package arez.integration;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.lang.reflect.Modifier;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;

public final class TypeVisibilityTest extends AbstractArezIntegrationTest {
    @ArezComponent
    public abstract static class PersonModel {
        PersonModel() {}

        @Observable
        @Nonnull
        String getFirstName() {
            return "";
        }

        void setFirstName(@Nonnull final String firstName) {}
    }

    @ArezComponent
    public abstract static class PersonModel2 {
        @Observable
        @Nonnull
        String getFirstName() {
            return "";
        }

        void setFirstName(@Nonnull final String firstName) {}
    }

    @Test
    public void typeVisibilities() {
        assertFalse(Modifier.isPublic(TypeVisibilityTest_Arez_PersonModel.class.getModifiers()));
        assertFalse(Modifier.isPublic(TypeVisibilityTest_Arez_PersonModel2.class.getModifiers()));
    }
}
