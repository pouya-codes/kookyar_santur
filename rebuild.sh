#!/bin/bash

# Clean and rebuild script to fix PendingIntent FLAG_IMMUTABLE issue

echo "Cleaning project..."
./gradlew clean

echo "Building project..."
./gradlew build

echo "Build complete. The PendingIntent FLAG_IMMUTABLE issue should now be resolved."
