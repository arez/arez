package com.example.misplaced_annotation;

import arez.annotations.MappedRelationshipComparator;

abstract class MappedRelationshipComparatorOutsideArezTypeModel {
  @MappedRelationshipComparator(name = "items")
  abstract Object value();
}
