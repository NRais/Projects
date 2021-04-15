
Imports System.IO

'
' Created by SharpDevelop.
' User: Nuser
' Date: 16/04/2018
' Time: 3:41 p.m.
' 
' To change this template use Tools | Options | Coding | Edit Standard Headers.
'
Public Class Searcher

	Dim FileList As List(Of FileInfo)
	Dim eachFileInMydirectory As String()

	Public Function RunSearch() As List(Of FileInfo)
	
		FileList = new List(Of FileInfo)
		
		' CHECK DIRECTORY
		If Directory.Exists(CStr(MainProgram.MySettings.settings("FindDirectory"))) Then
		
			' get all stuff from directory and subdirectory (all stuff that matches the Extension specified)
			' CRITERIA 1. Extension must match
			' (note: this will return files and folders)
			eachFileInMydirectory = IO.Directory.GetFileSystemEntries(CStr(MainProgram.MySettings.settings("FindDirectory")), "*" & CStr(MainProgram.MySettings.settings("FindExtension")), SearchOption.AllDirectories)
			'.Where(s => s.EndsWith(".txt", StringComparison.OrdinalIgnoreCase) || s.EndsWith(".doc", StringComparison.OrdinalIgnoreCase));
					
			StoreFileObjects()
			
			RemoveFilesThatDontMatch()
			
		Else
			' NO SUCH DIRECTORY
		End If
		
		Return FileList
	End Function
	
	
	' STORE EACH FILE AS A FILE OBJECT IN OUR LIST
	Sub StoreFileObjects()
	
		For i As Integer = 0 To (eachFileInMydirectory.Length - 1)
			
			Dim theFilesInfo As FileInfo = new FileInfo(eachFileInMydirectory(i))
			
			FileList.Add(theFilesInfo)
			'''
		Next	
		
	End Sub
	
		
	' REMOVE FILES THAT DO NOT MATCH CRITERIA
	Sub RemoveFilesThatDontMatch()
	
		Dim length as Integer = FileList.Count() - 1
		Dim i As Integer = 0
		
		Console.WriteLine("$$$" & length & "$$$")
		
		' loop through files until i == length
		Do While i <= length
			' if it doesn't meet then remove it
			If Not MeetsCriteria(i) Then
				
				FileList.RemoveAt(i)
				
				' IF IT DOESN'T MEET
				' decrease length because we have one less item to look for
				length = length - 1
				
			Else
				' IF IT DOES MEET
				' increment i because we have another item in our list
				i = i + 1 
			End If
		
		Loop
	
	End Sub
	
	
	' FUNCTION TO TEST FILES AND CRITERIA
	' Plug in a file and we will check TheInput() and tell you if this one matches
	Function MeetsCriteria(i As Integer) As Boolean
		Dim correct As Boolean = true
		
		' call the builder
		Dim build As Builder = new Builder
		' the builder will convert any edits such as {INT_INDEX} into actual values
		
		' ## Console.WriteLine("FILTER : " & MainProgram.MySettings.settings("FindFilter & " , ITEM : " & FileList.Item(i).Name)
		
		Dim newReplaceString As String = build.EditName(CStr(MainProgram.MySettings.settings("FindFilter")), FileList.Item(i))
		
		' TODO TODO CRITERIA
		
		' CRITERIA 2. File name must match
		If  Not (FileList.Item(i).Name).Contains(newReplaceString) Then
			
			' REMOVE FILE
			correct = false		
			
		Else
			Console.WriteLine("###" & FileList.Item(i).Name & "####")		
		
		End If
	
		Return correct
	End Function
	
End Class
