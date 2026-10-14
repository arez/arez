package arez.component;

import arez.Arez;

/**
 * Interface implemented by components that can verify their internal state.
 */
public interface Verifiable {
    /**
     * verify the state of the component and raise an exception if any state fails to verify.
     *
     * @throws Exception if entity invalid.
     */
    void verify() throws Exception;

    /**
     * Verify they supplied object if it is verifiable.
     *
     * @param object the object to verify.
     * @throws Exception if entity invalid.
     */
    static void verify(final Object object) throws Exception {
        if (Arez.isVerifyEnabled()) {
            if (object instanceof Verifiable) {
                ((Verifiable) object).verify();
            }
        }
    }
}
