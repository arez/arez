# Draw IO

This folder contains images generated for Arez by DrawIO.

These files can be exported from DrawIO as SVG. We then remove the  DOCTYPE, namespaces and
then replace the width/height attributes on top level svg element with a `viewbox="0 0 width height"`
and copy the files to `_includes` directory.
