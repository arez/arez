package arez.integration.inverse.other;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.LinkType;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.integration.inverse.CrossPackageInverseIntegrationTest.Owner;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class CrossPackageReferences {
    private CrossPackageReferences() {}

    @NullMarked
    @ArezComponent
    public abstract static class Many {
        public static Many create(final @Nullable Integer ownerId) {
            return new CrossPackageReferences_Arez_Many(ownerId);
        }

        @Reference(inverseName = "children", inverseMultiplicity = Multiplicity.MANY)
        public abstract @Nullable Owner getOwner();

        @ReferenceId
        @Observable(initializer = Feature.ENABLE)
        public abstract @Nullable Integer getOwnerId();

        public abstract void setOwnerId(@Nullable Integer ownerId);
    }

    @NullMarked
    @ArezComponent
    public abstract static class One {
        public static One create(final @Nullable Integer ownerId) {
            return new CrossPackageReferences_Arez_One(ownerId);
        }

        @Reference(load = LinkType.LAZY, inverseName = "required", inverseMultiplicity = Multiplicity.ONE)
        public abstract @Nullable Owner getOwner();

        @ReferenceId
        @Observable(initializer = Feature.ENABLE)
        public abstract @Nullable Integer getOwnerId();

        public abstract void setOwnerId(@Nullable Integer ownerId);
    }

    @NullMarked
    @ArezComponent
    public abstract static class Optional {
        public static Optional create(final @Nullable Integer ownerId) {
            return new CrossPackageReferences_Arez_Optional(ownerId);
        }

        @Reference(load = LinkType.EXPLICIT, inverseName = "optional", inverseMultiplicity = Multiplicity.ZERO_OR_ONE)
        public abstract @Nullable Owner getOwner();

        @ReferenceId
        @Observable(initializer = Feature.ENABLE)
        public abstract @Nullable Integer getOwnerId();

        public abstract void setOwnerId(@Nullable Integer ownerId);
    }
}
