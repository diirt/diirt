#!/bin/bash
[ "$#" -eq 2 ] || echo "Usage: $0 <interface> <seconds>"; echo "    $0 eth0 30"; exit 1

ETHINT=$1
SEC=$2
echo Shutting down network $ETHINT
ifconfig $ETHINT down
sleep $SEC
echo Restart network $ETHINT
ifconfig $ETHINT up
