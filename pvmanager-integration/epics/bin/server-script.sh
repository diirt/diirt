#!/bin/bash
BASEDIR=$(dirname $0)
FIRSTIOC="phase1"
ETH="eth1"
cd $BASEDIR
./start-ioc.sh $FIRSTIOC
while true; do
  COMMAND=`./wait-for-command.sh 2> /dev/null`
  case $COMMAND in
     start*) echo Executing \"$COMMAND\"
             NSEC=`echo $COMMAND | cut -d " " -f 3`
             IOC=`echo $COMMAND | cut -d " " -f 2`
             echo Stopping current IOC
             ./stop-ioc.sh &> /dev/null
             echo Waiting $NSEC seconds
             sleep `echo $COMMAND | cut -d " " -f 3`
             ./start-ioc.sh `echo $COMMAND | cut -d " " -f 2`
             sleep 1
             ;;
     netpause*) echo Executing \"$COMMAND\"
             ./network-pause.sh $ETH `echo $COMMAND | cut -d " " -f 2`
             ;;
     stop*) echo Shutting down
             echo Stopping current IOC
             ./stop-ioc.sh &> /dev/null
             echo Done
             exit 0;
             ;;
     *) echo Unrecognized command $COMMAND
        ;;
  esac
  echo Ready for next command
  caput command ready &>/dev/null
done