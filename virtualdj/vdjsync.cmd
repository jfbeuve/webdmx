cd D:\documents\workspace\virtualdj
set CLASSPATH=..\libs\commons-io-1.4.jar
set CLASSPATH=%CLASSPATH%;..\libs\log4j-1.2.15.jar
set CLASSPATH=%CLASSPATH%;bin
java fr.beuve.vdj.comments.MusicStoreFile canal50.properties
pause
java fr.beuve.vdj.title.TitleCleaner canal50.properties
pause
