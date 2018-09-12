package arez.doc.examples.repository2;

import arez.annotations.Computed;
import java.util.List;
import javax.annotation.Nonnull;

public interface MyComponentRepositoryExtension
{
  @Computed
  default boolean isEmpty()
  {
    return self().findAll().isEmpty();
  }

  @Computed
  default List<MyComponent> findAllActive()
  {
    return self().findAllByQuery( MyComponent::isActive );
  }

  @Nonnull
  MyComponentRepository self();
}
