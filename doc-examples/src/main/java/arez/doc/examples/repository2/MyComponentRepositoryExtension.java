package arez.doc.examples.repository2;

import arez.annotations.Memoize;
import java.util.List;
import javax.annotation.Nonnull;

public interface MyComponentRepositoryExtension
{
  @Memoize
  default boolean isEmpty()
  {
    return self().findAll().isEmpty();
  }

  @Memoize
  default List<MyComponent> findAllActive()
  {
    return self().findAllByQuery( MyComponent::isActive );
  }

  @Nonnull
  MyComponentRepository self();
}
