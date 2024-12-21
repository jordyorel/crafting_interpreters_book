# Java Compiler
JAVAC = javac
JAVA = java

# Directories
SRC_DIR = com/craftinginterpreters
TOOL_DIR = $(SRC_DIR)/tool
MAIN_DIR = $(SRC_DIR)/lox
TEST_DIR = test

# Main entry point
MAIN_CLASS = com.craftinginterpreters.lox.Lox

# Files
SOURCES = $(wildcard $(MAIN_DIR)/*.java $(TOOL_DIR)/*.java $(TEST_DIR)/*.java)
CLASSES = $(SOURCES:.java=.class)

# Default target: compile everything
all: compile

# Compile the source code and tests
compile: $(CLASSES)

%.class: %.java
	$(JAVAC) $<

# Generate the AST (Assumes GenerateAst is a utility)
generate_ast: $(TOOL_DIR)/GenerateAst.class
	$(JAVA) com.craftinginterpreters.tool.GenerateAst $(MAIN_DIR)

# Run the main program (REPL)
run: compile
	$(JAVA) $(MAIN_CLASS)

# Run a Lox script
run_script: compile
	@read -p "Enter the Lox script path: " script; \
	$(JAVA) $(MAIN_CLASS) $$script

# Run tests
test: compile
	./run_tests.sh

# Clean compiled files
clean:
	rm -f $(MAIN_DIR)/*.class $(TOOL_DIR)/*.class $(TEST_DIR)/*.class

# Help
help:
	@echo "Available targets:"
	@echo "  all          - Compile the project"
	@echo "  compile      - Compile source and test files"
	@echo "  generate_ast - Run the AST generator"
	@echo "  run          - Run the main program (REPL)"
	@echo "  run-script   - Run a Lox script"
	@echo "  test         - Run tests"
	@echo "  clean        - Remove compiled files"

.PHONY: all compile run run-script test clean help generate_ast
