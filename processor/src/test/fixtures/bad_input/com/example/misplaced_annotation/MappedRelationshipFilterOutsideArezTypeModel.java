package com.example.misplaced_annotation;

import arez.annotations.MappedRelationshipFilter;

abstract class MappedRelationshipFilterOutsideArezTypeModel {
  @MappedRelationshipFilter(name = "items")
  abstract Object value();
}
