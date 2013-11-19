#!/bin/bash
if [ ! "$#" -eq 1 ]; then
  echo "Usage: $0 <ioc_name>"
  echo "    $0 phase1"
  exit 1
fi

BASEDIR=$(dirname $0)
SOFTIOC_NAME=$1
PORT=12345
SOFTIOC="/usr/bin/softIoc"
cd $BASEDIR/../$SOFTIOC_NAME
echo Starting IOC $SOFTIOC_NAME on $PORT from `pwd`
procServ --noautorestart -p ../softIoc.pid $PORT $SOFTIOC st.cmd
