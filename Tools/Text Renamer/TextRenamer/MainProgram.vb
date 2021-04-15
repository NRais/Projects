Imports System.IO
'
' Created by SharpDevelop.
' User: Nuser
' Date: 16/04/2018
' Time: 3:39 p.m.
' 
' To change this template use Tools | Options | Coding | Edit Standard Headers.
'
Module MainProgram

	' TODO : cool features for the future
	' - Change file timestamp
	' - Change file attributes (read-only, archive, hidden, system)
	' - read files and edit their text
	' - search archives


	Dim FileList As List(Of FileInfo)
	' FileInfo Class
	' .DirectoryName 	(gets full directory path)
	' .Extension 		(gets file extension)
	' .FullName 		(gets full path and name)
	' .Name 			(gets the name of the file)
	' 
	
	Public Dim MySettings As TheInput = new TheInput ' stored in a variable so we can throw it around between classes
	
	Sub Main()
	
		'Get Input from user, input falls into 2 categories
		' : Criteria for success
		' : Variables for editing
		MySettings.Run()
	
		' Search directory and return the array of matching files
		Dim s As Searcher = new Searcher
		FileList = s.RunSearch()
		
		
		' Loop through all the files and edit them
		Dim e As Renamer = new Renamer
		e.EditFiles(FileList) ' NOTE: now FileList is all files to be edited
		
		
		
		Console.ReadLine() ' leave open

	End Sub
End Module
