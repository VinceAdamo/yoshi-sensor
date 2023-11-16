#!/bin/bash

set -e

if [ ! -f "/mosquitto/config/password-temp.txt" ]
then
  echo "$0: File password-temp.txt not found."
  exit 1
fi

cp /mosquitto/config/password-temp.txt /mosquitto/config/password.txt 

# Convert the password file.
mosquitto_passwd -U /mosquitto/config/password.txt

exec "$@"