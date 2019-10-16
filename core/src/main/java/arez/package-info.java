/**
 * Core Arez primitives.
 */
@OmitPattern( type = "^.*\\.Arez_.*$", symbol = "^toString$", unless = "arez.enable_names" )
@OmitPattern( type = "^.*(\\.|_)Arez_[^\\.]Repository$", symbol = "^getRepositoryName$", unless = "arez.enable_names" )
// No repository should have equals defined
@OmitPattern( type = "^.*(\\.|_)Arez_[^\\.]Repository$", symbol = "\\$equals" )
// No repositories need their own identity if native components disabled
@OmitPattern( type = "^.*(\\.|_)Arez_[^\\.]Repository$", symbol = "^\\$\\$arezi\\$\\$_id$", unless = "arez.enable_native_components" )
@OmitPattern( type = "^.*(\\.|_)Arez_[^\\.]Repository$", symbol = "^\\$\\$arezi\\$\\$_nextId$", unless = "arez.enable_native_components" )
@OmitPattern( symbol = "^\\$clinit$" )
@KeepPattern( type = "^arez\\.ArezContextHolder$", symbol = "^\\$clinit$", unless = "arez.enable_zones" )
@KeepPattern( type = "^arez\\.ArezZoneHolder$", symbol = "^\\$clinit$", when = "arez.enable_zones" )
package arez;

import grim.annotations.KeepPattern;
import grim.annotations.OmitPattern;
