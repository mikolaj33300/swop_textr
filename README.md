In our project, Textr will be opened with version 0.7 of termios, so the command to open textR becomes: 

java -cp io.github.btj.termios-0.7.jar -jar system.jar


This line is  followed by the line serperator of choice: "--lf" or "--crlf". if no line seperator is given, the system default will be used.

After the the line seperator, one or more paths can be used to open the desired files.
