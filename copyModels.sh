#!/bin/bash

# Set the source and destination module paths
SOURCE_MODULE_PATH="./rpc-rmi-server/src/main/java/models"
DESTINATION_MODULES=("client" "gateway")

# Loop through destination modules and copy models
for MODULE in "${DESTINATION_MODULES[@]}"
do
  DESTINATION_MODULE_PATH="./$MODULE/src/main/java"

  # Ensure the destination module path exists
  if [ ! -d "$DESTINATION_MODULE_PATH" ]; then
    echo "Error: Destination module path $DESTINATION_MODULE_PATH not found."
    exit 1
  fi

  # Copy the models package
  cp -r "$SOURCE_MODULE_PATH" "$DESTINATION_MODULE_PATH"

  # Optional: Print a message indicating success
  echo "Models package copied from $SOURCE_MODULE_PATH to $DESTINATION_MODULE_PATH"
done
