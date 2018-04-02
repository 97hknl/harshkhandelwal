
Helper scripts for Advanced Programming, Problem 1 - Calculator


================================================================
  						   Install
================================================================
In this folder you will find two scripts. One for windows 
and one for linux and mac:
	
	- Linux/Mac --> test_nix
	- Windows   --> test_win.ps1

Copy the appropriate file to the root folder of your project.

================================================================
  							 Run
================================================================

IMPORTANT: Before you run the script you must have a runnable
           .jar named AP1-1.0.jar placed in build/libs/ folder.
           You can run ./gradlew jar for it.


--Linux-Mac--
  
  1. Open a terminal and navigate to your project directory

  2. Paste the following to your terminal:  
	
	 ./test_nix


--Windows--

  1. Open a powershell and navigate to your project folder, or 
	 just open your project folder, shift+right click on the
     white area and select "Open PowerShell window here"
  
  2. Once in powershell paste the following:
	 
	 powershell -ExecutionPolicy ByPass .\test_win



================================================================
  						   About
================================================================

The script will execute ./util/generate_expressions.py and paste
its output in a file called expressions.in.txt. Then that file
will be evaluated (not by your solution) and the correct results
of the expressions will be pasted in a file expressions.out.txt
Then the contents of expressions.in.txt are given as input to 
your program.
The output of the script contains the generated expressions as
well as their solutions and the results of your program so you
can check if the two match. 
expressions.in.txt and expressions.out.txt will persist in your
directory until you run the test again. Every time you run the
script these two files will be overwritten.

IMPORTANT: You may find minor differences between your solution
           and the 'correct' solution. These will be rounding
           errors and are unavoidable since the expressions are
           evaluated in awk and powershell for the test and in
           Java for your program. In such cases your answer is
           considered correct of course!

If you face any issues, use the Canvas forum to ask for help!



