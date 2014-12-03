#!/bin/bash
BASEDIR=$(dirname $0)
PID_NAME="softIoc.pid"
PORT=12345
cd $BASEDIR/..
if [ ! -f $PID_NAME ]; then
  echo "No running process. Nothing to do"
else
  ( echo exit || sleep 1) | telnet localhost $PORT
  echo Waiting
  sleep 5
  kill `cat $PID_NAME`
  rm $PID_NAME
fi
