package arez.doc.examples.repository;

import arez.annotations.Computed;
import java.util.List;

public interface MyComponentRepositoryExtension
  extends MyComponentBaseRepositoryExtension
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
}
