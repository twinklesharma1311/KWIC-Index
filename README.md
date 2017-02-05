# KWIC-Index

The purpose of our project is to develop KWIC index System (Keyword In Context) proposed by David Parnas in early 70’s using Java Applet. This System provides a convenient search mechanism for information in a long list of lines, such as book titles, or online documentation entries.

Parnas described the KWIC problem as follows: 
“The KWIC index system accepts an ordered set of lines; each line is an ordered set of words, and each word is an ordered set of characters. Any line may be “circularly shifted” by repeatedly removing the first word and appending it at the end of the line. The KWIC index system outputs a list of all circular shifts of all lines in alphabetical order.”

In his paper of 1972, Parnas used the problem to contrast different criteria for decomposing a system into modules. Our team followed the same phenomena to implement KWIC system, by designing the system with 5 highly cohesive modules (input, line storage, circular shift, alphabetical sort, and output). We analyzed functional and nonfunctional requirement, design architecture styles, implement using Java applet and test the system. The KWIC system architecture style shall be an Abstract Data Type (ADT) style as this will provide clear object oriented structure with desire qualities of high cohesion and low coupling. 
