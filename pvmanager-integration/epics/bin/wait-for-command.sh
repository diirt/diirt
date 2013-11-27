#!/bin/bash
COUNT=0
while read i; do
  CHILD_PID=$!
  COMMAND=`echo $i | cut -c 36-99`
  if [ $COUNT -eq 0 ]; then
    (( COUNT++ ))
  else
    echo $COMMAND
    exit 0
  fi
done < <(camonitor command)
