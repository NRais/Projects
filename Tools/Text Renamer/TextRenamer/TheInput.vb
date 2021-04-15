'
' Created by SharpDevelop.
' User: Nuser
' Date: 16/04/2018
' Time: 4:35 p.m.
' 
' To change this template use Tools | Options | Coding | Edit Standard Headers.
'
Public Class TheInput

	' - allow finding xth index

	Public Dim FindDirectory As String = "" ' directory to search
	' NOTE: usually find and filter strings are the same, but they can be different
	Public Dim FindFilter As String = ""	' the string by which results which be tested
	Public Dim FindString As String = ""	' the string of which results will be searched and modifed
	Public Dim FindExtension As String = ""
	
	Public Dim FindStringException As String = "" ' a string that if found will automatically cancel
	Public Dim FindStringExtension As String = "" ' an extension that if found will automatically cancel
	
	Public Dim NewString As String = ""
	
	Public Sub Run()
	
		Console.WriteLine("### WELCOME TO THE FILE RENAMER ###" & Environment.NewLine & "  * Enter a directory to access:")
		FindDirectory = Console.ReadLine()
		
		Console.WriteLine("  * Enter a string to find:")
		FindFilter = Console.ReadLine()
		
		' TODO : UNLESS OTHERWISE SPECIFIED
		FindString = FindFilter
		
		Console.WriteLine("  * Enter a string to replace it with:")
		NewString = Console.ReadLine()
		
		Console.WriteLine("  * Enter a file extension (or put a * for all):")
		FindExtension = Console.ReadLine()
	
	End Sub
	
	' --- DUNGEON --- '
	' in the deep dark dungeon we store the lesser known mainly static variables
	' :FOR FILE NAMES: ->
	Public Dim findMode As String = "partial" 
	' partial -> find part and replace that part
	' full -> find part and replace the full name
	Public Dim findOccurance() As Integer = {0, -1} ' an array StartIndex, EndIndex (if -1 then it will go until the strings end)

	Public Dim findSpecificType As Boolean = false ' (false = find all files / true = find certain extensions)
												  
	Public Dim findCaseSensitive As Boolean = true ' all the find file options are case sensetive, or insensitive
	Public Dim findRegEx As Boolean = false ' if the file name must contain no wierd characters, or not
	Public Dim findSubfolders As Boolean = true
	
	Public Dim total_count As Integer = 0 ' total files edited
	Public Dim file_count As Integer = 0 ' files edited in folder
	
	Public Dim advCharL As String = "{{" ' alt  ~:
	Public Dim advCharR As String = "}}" ' alt  :~
	
	Public Dim editReplace As Boolean = true ' whether the new text should replace text found or appended after it
	
End Class
