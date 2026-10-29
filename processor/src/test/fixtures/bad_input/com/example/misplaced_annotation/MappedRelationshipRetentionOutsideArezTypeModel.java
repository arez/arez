package com.example.misplaced_annotation;

import arez.annotations.MappedRelationshipRetention;

abstract class MappedRelationshipRetentionOutsideArezTypeModel {
  @MappedRelationshipRetention(name = "items")
  abstract Object value();
}
