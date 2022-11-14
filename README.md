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
