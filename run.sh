#!/bin/bash

# Academic Seminar Management System - Build & Run Script

echo "========================================="
echo "Academic Seminar Management System"
echo "========================================="
echo ""

# Clean previous build
echo "Cleaning previous build..."
find . -name "*.class" -type f -delete
rm -f sources.txt

# Find all Java source files
echo "Finding Java source files..."
find . -name "*.java" -type f > sources.txt

# Count source files
file_count=$(wc -l < sources.txt)
echo "Found $file_count Java source files"
echo ""

# Compile
echo "Compiling Java files..."
javac -d . @sources.txt

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo ""
    
    # Run the application
    echo "Launching application..."
    echo ""
    java gui.AppGUI
else
    echo "✗ Compilation failed!"
    exit 1
fi
