
#!/usr/bin/env bash
set -e

# Run from the project directory
cd "$(dirname "$0")"

case "${1:-all}" in
    all)
        echo "Building and testing library..."
        mvn clean verify

        echo "Starting library simulator..."
        mvn exec:java
        ;;

    test)
        echo "Running JUnit tests..."
        mvn clean test
        ;;

    demo)
        echo "Starting library simulator..."
        mvn compile exec:java
        ;;

    *)
        echo "Usage: ./run.sh [all|test|demo]"
        exit 1
        ;;
esac
