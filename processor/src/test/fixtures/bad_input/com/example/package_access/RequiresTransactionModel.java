package com.example.package_access;

import arez.annotations.ArezComponent;
import com.example.package_access.other.BaseRequiresTransactionModel;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class RequiresTransactionModel
  extends BaseRequiresTransactionModel
{
}
