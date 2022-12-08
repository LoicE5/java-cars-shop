> Group : Fares Ezzaouia & Loïc Etienne

# Project execution

<font color="orange">For reasons that are further detailed in the dev.pdf document, we have chosen not to offer the possibility to compile the source code into .class files using the command line. The project is delivered with an *out* folder that already contains .class files. This README explain how to execute them.</font>

## Database setup

First, you need to have a running MySQL server and to execute in it the sample data provided in the `sample_data.sql` file. This will create the database, the tables, columns, rules and append multiple rows of data.

To install MySQL on Linux, you may run :

`sudo apt update`
<br>
`sudo apt install mysql-server`
<br>
`sudo systemctl start mysql.service`

Now, you may change the password for the default user "root" by running
`mysql`
<br>
then
<br>
`ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '{{NEW PASSWORD}}';`

Once this is done, you may connect to mysql in CLI using :
`mysql -u root -p`

You will be prompted to enter your newly set password. Please do, then copy-paste the content of the `sample_data.sql` file. It should execute with no issues.

You may now leave the sql prompt by typing `exit`.

## Java installation

If java is not installed, or your version of java is under 16, then you may run :
`sudo apt install openjdk-17-jre-headless`

To check about the current version of java and/or its install status, run :
`java --version`
## Run using make

The makefile in this project will be handling the execution. Simply open a terminal at this directory, then type `make`.

If you don't have make installed, you may install it via `sudo apt install make`.

## Run using the CLI

Open a terminal at the project's directory, then type : `java -classpath ./out/production/java-cars-shop:./lib/mysql-connector-java-8.0.30.jar:./lib/jackson-databind-2.13.3.jar:./lib/jackson-annotations-2.13.3.jar:./lib/jackson-core-2.13.3.jar Main`

---

### ***Testing environment***

**OpenJdk**

`
openjdk version "17.0.5" 2022-10-18
OpenJDK Runtime Environment (build 17.0.5+8-Ubuntu-2ubuntu122.04)
OpenJDK 64-Bit Server VM (build 17.0.5+8-Ubuntu-2ubuntu122.04, mixed mode, sharing)
`

**MySQL**

`
mysql  Ver 8.0.31-0ubuntu0.22.04.1 for Linux on aarch64 ((Ubuntu))
`