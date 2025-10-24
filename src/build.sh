#!/bin/bash

BUILD_DIR=target

javac -g -d "$BUILD_DIR" $(find src -name '*.java')

jar cfm "$BUILD_DIR/dlhc.jar" src/Manifest.txt -C "$BUILD_DIR" .

