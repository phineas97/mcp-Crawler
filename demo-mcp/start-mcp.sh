#!/bin/bash

MCP_COMMAND="npx @playwright/mc
p@latest --port 8931"

while true; do
  echo "Starting MCP..."
  $MCP_COMMAND &
  MCP_PID=$!
    echo "MCP service started with PID $MCP_PID"
    wait $MCP_PID
    EXIT_STATUS=$?

    if [ $EXIT_STATUS -eq 130 ]; then
        echo "MCP service was stopped by user (Ctrl+C). Exiting..."
        break
    fi

    echo "MCP service has crashed: $EXIT_STATUS. Restarting in 5 seconds..."
    sleep 5
done