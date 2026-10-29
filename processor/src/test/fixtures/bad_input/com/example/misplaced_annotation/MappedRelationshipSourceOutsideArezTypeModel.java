package com.example.misplaced_annotation;

import arez.annotations.MappedRelationshipSource;

abstract class MappedRelationshipSourceOutsideArezTypeModel {
  @MappedRelationshipSource(name = "items")
  abstract Object value();
}
