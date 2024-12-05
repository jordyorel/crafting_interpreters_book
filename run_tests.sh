#!/bin/bash

# Set the classpath for the project
CLASSPATH="."

# Compile all source and test files
echo "Compiling all .java files..."
javac -cp "$CLASSPATH" $(find . -name "*.java")

# Check if compilation was successful
if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "Compilation successful."

# Option to run the main program (REPL)
read -p "Do you want to start the main program (REPL)? (yes/no): " start_repl
if [ "$start_repl" = "yes" ]; then
    echo "Starting the REPL..."
    java -cp "$CLASSPATH" com.craftinginterpreters.lox.Lox
    exit 0
fi

# Run tests if REPL is not chosen
echo "Running tests..."

# Find and execute all test classes in the test directory
for TEST_CLASS in $(find ./test -name "*.class" | sed 's|./||;s|.class||' | tr '/' '.'); do
    echo "Running test: $TEST_CLASS"
    java -cp "$CLASSPATH" "$TEST_CLASS"

    # Check if the test passed
    if [ $? -ne 0 ]; then
        echo "Test failed: $TEST_CLASS"
        exit 1
    fi
done

echo "All tests passed successfully!"
