MVC text editor in Java for group assignment.
___________________
In our project, Textr will work with version 0.9 of termios, so the command to open textR becomes: 

java -cp io.github.btj.termios-0.9.jar -jar system.jar <lineseperator> <files>

This line is followed by the line serperator of choice: "--lf" or "--crlf". If no line seperator is given, the system default will be used.

After the the line seperator, one or more paths can be used to open the desired files.
