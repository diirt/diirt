diirt: Data Integration In Real-Time
====================================

The overall project is divided into the following main modules: util, datasource,
service, pods and support.

util
====

  This module contains basic Java utility classes that should, in an ideal world,
be part of the JDK. When suitable replacement will be available, these classes
will be deprecated. For example: they used to contain nanosecond precision time
classes which are being phased out by JDK 8 java.time package.

  They currently provide collections for primitives, statistical operations,
text parsing and other utilities.

