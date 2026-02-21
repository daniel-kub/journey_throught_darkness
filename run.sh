#!/bin/bash

cd "$(dirname "$0")"

echo "Kompilacja..."
javac -d out src/main/java/com/rpg/**/*.java

if [ $? -eq 0 ]; then
    echo "Uruchamianie gry..."
    java -cp out com.rpg.engine.GameEngine
else
    echo "Błąd kompilacji!"
    exit 1
fi
