#!/bin/bash
file="webdmx.$(date +%Y%m%d).jar"
echo $file
if [ ! -f "$file" ]
then
  cp webdmx.jar "webdmx.$(date +%Y%m%d).jar"
fi
