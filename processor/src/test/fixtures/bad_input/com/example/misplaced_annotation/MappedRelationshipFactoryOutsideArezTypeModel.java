package com.example.misplaced_annotation;

import arez.annotations.MappedRelationshipFactory;

abstract class MappedRelationshipFactoryOutsideArezTypeModel {
  @MappedRelationshipFactory(name = "items")
  abstract Object value();
}
