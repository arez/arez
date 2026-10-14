package arez.component;

import static org.testng.Assert.*;

import arez.AbstractTest;
import org.testng.annotations.Test;

public final class IdentifiableTest extends AbstractTest {
    static class TestElement implements Identifiable<String> {

        @Override
        public String getArezId() {
            return "X";
        }
    }

    @Test
    public void identifiableElement() {
        final var element = new TestElement();
        assertEquals(Identifiable.getArezId(element), "X");
        assertEquals(Identifiable.<String>asIdentifiable(element), element);
    }

    @Test
    public void nonIdentifiableElement() {
        final var element = new Object();
        assertNull(Identifiable.getArezId(element));
        assertInvariantFailure(
                () -> Identifiable.asIdentifiable(element),
                "Arez-0158: Object passed to asIdentifiable does not " + "implement Identifiable. Object: " + element);
    }
}
