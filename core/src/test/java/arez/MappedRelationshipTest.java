package arez;

import static org.testng.Assert.*;

import arez.annotations.MappedRelationship;
import arez.annotations.Memoize;
import org.testng.annotations.Test;

public final class MappedRelationshipTest {
    @Test
    public void exposesEveryMemoizeOptionWithTheSameDeclaredDefault() throws Exception {
        final var options = Memoize.class.getDeclaredMethods();
        assertEquals(MappedRelationship.class.getDeclaredMethods().length, options.length + 1);
        assertEquals(
                MappedRelationship.class
                        .getDeclaredMethod("validateTypeAtRuntime")
                        .getDefaultValue(),
                false);
        for (final var option : options) {
            final var mapped = MappedRelationship.class.getDeclaredMethod(option.getName());
            assertEquals(mapped.getReturnType(), option.getReturnType(), option.getName());
            assertEquals(mapped.getDefaultValue(), option.getDefaultValue(), option.getName());
        }
    }
}
