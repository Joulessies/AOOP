#!/bin/bash

echo "Starting Cofitearia Milktea Inventory and Sales Management System..."
echo

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 17 or higher and try again"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed or not in PATH"
    echo "Please install Maven and try again"
    exit 1
fi

echo "Building the application..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "Error: Build failed"
    exit 1
fi

echo
echo "Starting the application..."
echo
echo "Accessibility Features:"
echo "- Press F1 for help"
echo "- Press F2 to toggle high contrast mode"
echo "- Press F3 to toggle large text mode"
echo "- Press F4 to toggle accessibility mode"
echo "- Use Tab to navigate between fields"
echo "- Use Enter to activate buttons"
echo

mvn javafx:run
