#dbLoadTemplate test.substitutions
dbLoadRecords("alarm-change.db", "P=TST:Alarm:,DBL=Double,LONG=Long,STR=String")
dbLoadRecords("ramp-change.db", "P=TST:Ramp:,DBL=Double,LONG=Long,STR=String,MBBI=Mbbi")
dbLoadDatabase phase1.db
iocInit
