# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

* Add an observable that monitors document element by periodically executing a task on a timer
  or as a scheduled idle task. This may need to wail till Arez gets time based scheduling in place.
  See https://github.com/arez/arez/issues/10

* Add a Gelocation component. See https://github.com/arez/arez/issues/9

* Add a DeviceMotion component. See https://github.com/arez/arez/issues/8

* Consider replacing `EventDrivenValue` with `SubsciptionBasedValue` that has `onActivate` hook and an
  `onDeactivate` hooked passed into constructor as well as `getValue()` hook. Alternatively could passing
  in `List` of events that drive `EventDrivenValue`.
