@Value.Style(
        visibility = Value.Style.ImplementationVisibility.PACKAGE,
        defaults = @Value.Immutable(builder = true, singleton = true),
        get = {"is*", "get*"},
        init = "with*",
        passAnnotations = SuppressWarnings.class
)
package info.victorchu.calcite.tutorials;

import org.immutables.value.Value;