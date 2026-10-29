package com.example.misplaced_annotation;

import arez.annotations.MappedRelationship;

abstract class MappedRelationshipOutsideArezTypeModel {
  @MappedRelationship
  abstract Object value();
}
