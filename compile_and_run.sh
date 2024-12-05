#!/bin/sh

# Compile all Java files in the project
javac ast/*.java com/craftinginterpreters/lox/*.java com/craftinginterpreters/tool/*.java

# Check if the compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful."
    # Run the GenerateAst class
    java com.craftinginterpreters.tool.GenerateAst
    
    # Optionally, you can run the main Lox class or any other class
    java com.craftinginterpreters.lox.Lox
else
    echo "Compilation failed."
fi



# chmod +x compile_and_run.sh
# ./compile_and_run.sh


