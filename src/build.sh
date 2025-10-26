#!/bin/bash

BUILD_DIR=target

# shellcheck disable=SC2046
javac -g -d "$BUILD_DIR" $(find src -name '*.java')

jar cfm "$BUILD_DIR/dlhc.jar" src/Manifest.txt art -C "$BUILD_DIR" hu

