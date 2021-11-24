#!/bin/sh

MODULE_PATH=""
FLAGS=""
if [ "$1" = "x" ]; then
  FLAGS="-Xlint"
fi
javac -classpath src --add-modules=ALL-MODULE-PATH -d ./build src/Main.java $FLAGS
