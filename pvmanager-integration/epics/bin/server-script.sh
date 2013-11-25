#!/bin/bash
BASEDIR=$(dirname $0)
FIRSTIOC="phase1"
ETH="eth1"
cd $BASEDIR
./start-ioc.sh $FIRSTIOC
while true; do
  COMMAND=`./wait-for-command.sh 2> /dev/null`
  case $COMMAND in
     start*) echo Starting ioc `echo $COMMAND | cut -d " " -f 2` after a pause of `echo $COMMAND | cut -d " " -f 3` seconds
             ./stop-ioc.sh
             sleep `echo $COMMAND | cut -d " " -f 3`
             ./start-ioc.sh `echo $COMMAND | cut -d " " -f 2`
             sleep 1
             ;;
     netpause*) echo Pausing network for `echo $COMMAND | cut -d " " -f 2` seconds
             ./network-pause.sh $ETH `echo $COMMAND | cut -d " " -f 2`
             ;;
     stop*) echo Finished
             ./stop-ioc.sh
             exit 0;
             ;;
     *) echo Unrecognized command $COMMAND
        ;;
  esac
  echo Ready for next command
  caput command ready >/dev/null
done