# Using an IntelliJ-structured project, we have decided not to deal with compiling in this makefile. Please see further details in dev.pdf.

JDK = java
ARGFILE = -cp "./out/production/java-cars-shop:./lib/mysql-connector-java-8.0.30.jar"
CLASSNAME = Main

run:
	$(JDK) $(ARGFILE) $(CLASSNAME)