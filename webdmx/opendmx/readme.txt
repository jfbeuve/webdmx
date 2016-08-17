Add VM arguments (for opendmx.dll) 

-Djava.library.path=dll

mvn package -Dmaven.test.skip=true
mvn install:install-file -Dfile=OpenDmx.jar -DgroupId=com.juanjo -DartifactId=opendmx -Dversion=2008.05.22 -Dpackaging=jar