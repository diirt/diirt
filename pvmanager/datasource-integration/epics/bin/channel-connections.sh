#!/bin/bash
CHANNEL=$1
OUTPUT=`(echo casr 2 ; sleep 10) | telnet localhost 12345 2> /dev/null | grep $1`
REGEX="s/.*$1(\([0-9]\+\).*).*/\1/"
CONNECTIONS=`echo $OUTPUT |  sed $REGEX`
if [ -z $CONNECTIONS ]
then
  echo 0
else
  echo $CONNECTIONS
fi