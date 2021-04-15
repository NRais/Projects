Imports System.Reflection

'
' Created by SharpDevelop.
' User: Nuser
' Date: 16/04/2018
' Time: 4:35 p.m.
' 
' To change this template use Tools | Options | Coding | Edit Standard Headers.
'
Public Class TheInput

	' a hash table storing all the variable settings of the program
	Public Dim settings As HashTable = new HashTable()

	Public Sub Run()
	
		InitializeSettings()
	
		Console.WriteLine("### WELCOME TO THE FILE RENAMER ###" & Environment.NewLine & "  * Enter a config file to access:")
		
		LoadConfig(Console.ReadLine())
	
	End Sub
	
	
	Sub LoadConfig(path As String)
	
		' we open the file of the path
		
		' we run through all the lines of the file
		
		' if any line contains the name of a variable then we update its value
		
		' the update value can be found on the following line
	End Sub
	
	
	Sub InitializeSettings()
		' --- SETTINGS --- '
		settings.add("FindDirectory",  "") ' directory to search
		' NOTE: usually find and filter strings are the same, but they can be different
		settings.add("FindFilter",  "")	' the string by which results which be tested
		settings.add("FindString",  "")	' the string of which results will be searched and modifed
		settings.add("FindExtension",  "" )
		
		settings.add("FindStringException",  "") ' a string that if found will automatically cancel
		settings.add("FindStringExtension",  "") ' an extension that if found will automatically cancel
		
		settings.add("NewString",  "") ' the string to replace the name with
		
		
		' --- DUNGEON --- '
		' in the deep dark dungeon we store the lesser known mainly static variables
		' :FOR FILE NAMES: ->
		settings.add("findMode",  "partial")
		' partial -> find part and replace that part
		' full -> find part and replace the full name
		settings.add("findOccuranceStart",  1) ' an array StartIndex, EndIndex (if -1 then it will go until the strings end)
		settings.add("findOccuranceEnd",  -1) 
	
		settings.add("findSpecificType",  false) ' (false = find all files / true = find certain extensions)
													  
		settings.add("findCaseSensitive",  true) ' all the find file options are case sensetive, or insensitive
		settings.add("findRegEx",  false) ' if the file name must contain no wierd characters, or not
		settings.add("findSubfolders",  true)
		
		settings.add("total_count",  0) ' total files edited
		settings.add("file_count",  0) ' files edited in folder
		
		settings.add("advCharL",  "{{") ' alt  ~:
		settings.add("advCharR",  "}}") ' alt  :~
		
		settings.add("editReplace",  true) ' whether the new text should replace text found or appended after it	
		' ---------------- '
	End Sub
	
End Class
