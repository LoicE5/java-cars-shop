# Using an IntelliJ-structured project, we have decided not to deal with compiling in this makefile. Please see further details in dev.pdf.

JDK = java
ARGFILE = -classpath ./out/production/java-cars-shop:./lib/mysql-connector-java-8.0.30.jar:./lib/jackson-databind-2.13.3.jar:./lib/jackson-annotations-2.13.3.jar:./lib/jackson-core-2.13.3.jar
CLASSNAME = Main

run:
	$(JDK) $(ARGFILE) $(CLASSNAME)