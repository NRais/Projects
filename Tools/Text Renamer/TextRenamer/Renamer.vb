﻿Imports System.IO
Imports System.Linq
Imports System.String

'
' Created by SharpDevelop.
' User: Nuser
' Date: 16/04/2018
' Time: 4:44 p.m.
' 
' To change this template use Tools | Options | Coding | Edit Standard Headers.
'
Public Class Renamer

	Dim renameFiles As Boolean = True
	Dim editingContent As Boolean = False

	Public Sub EditFiles(FileList As List(Of FileInfo))
	
		''' Sort List
		FileList = sortFiles(FileList)
		
		For i As Integer = 0 To (FileList.Count - 1)
			Console.WriteLine(FileList.Item(i).FullName)
		Next
		
		'''
		' FOR EACH FILE/FOLDER IN THE LIST OF FILES
		For i As Integer = 0 To (FileList.Count - 1)
		
			' update the counters
			MainProgram.MySettings.total_count = i
			MainProgram.MySettings.file_count = i
			
			' CHECK FOR READING AND EDITING FILE CONTENTS
			If (editingContent) Then
				'Dim e As FileEditor = New FileEditor 
			End If
			
			' CHECK FOR RENAMING FILE
			If (renameFiles) Then
			
				Dim isFolder As Boolean = FileList.Item(i).Attributes().HasFlag(FileAttributes.Directory)
			
				If (isFolder) Then
					
					' launch the renaming procedure with condition for the folders
					renameFile(FileList.Item(i), true)
					
				Else
				
					' launch the renaming procedure
					renameFile(FileList.Item(i))
				
				End If
			
			End If
			
		Next
		
	End Sub
	
	' FUNCTION take in files list output files list sorted
	' SORT MEANS:
	' 1. Files come first
	' 2. Folders follow sorted by length
	'	A. First long folders
	'	B. Then the sorter top-level folders
	
	' (Note: this is necessary because otherwise the files will get renamed and then the subfolders and files will be unavailable)
	Function sortFiles(list As List(Of FileInfo)) As List(Of FileInfo)

			'Dim chars As List(Of String) = {"C:\New\New2\New3.txt", "C:\New\New2\New4.txt", "C:\New\", "C:\New\New2", "C:\New\New2\New5.txt", "C:\New\New3.txt", "C:\New\New4.txt", "", "C:\New\New5.txt", "C:\New3.txt"}.toList()

		    'Dim orderByResult2 = From s In chars
            '       Order By s.Length() Descending, s.Contains(".")
            '       Select s

			' create a new IEnumerable object which is the sorted FileInfo list
			' SORT BY ATTRIBUTES
			' - Its pathway length (longest first)
			' - Folder vs. file (files first)
		    Dim orderByResult = From s In list
                   Order By s.DirectoryName().Length Descending, s.Attributes().HasFlag(FileAttributes.Directory)
                   Select s
                   
            list = new List(Of FileInfo)(orderByResult)
	
		Return list
	End Function
	
	
	' PROCEDURE to rename a file give (with the option of renaming a folder given)
	' and so rename it
	Sub renameFile(item As FileInfo, Optional isFolder As Boolean = false)
		Dim newNameString As String
		Dim newFindString As String
		Dim newReplaceString As String
		
		' call the builder
		Dim build As Builder = new Builder
		' the builder will convert any edits such as {INT_INDEX} into actual values
		newReplaceString = build.EditName(MainProgram.MySettings.NewString, item)		
		newFindString = build.EditName(MainProgram.MySettings.FindString, item)
		
		newNameString = item.Name()
		
		If (MainProgram.MySettings.findMode = "partial") Then ' mode is partial
		
			' replace the old occurance of the string with the new
			newNameString = newNameString.Replace(newFindString , newReplaceString)
			
		Else ' if mode is all
			
			' replace the whole old string with the new
			newNameString = newReplaceString
		End If
		
		' the builder will convert any modifications such as {{CASE_UPPER}} into the specified results (mods are applied in the order they appear)
		newNameString = build.ModName(newNameString, item)
		
		' RenameFile(<fileName>, <newName>)
		Try
			If isFolder Then
				FileIO.FileSystem.RenameDirectory(item.FullName(), newNameString)
			Else
				FileIO.FileSystem.RenameFile(item.FullName(), newNameString)
			End If
		Catch ex As Exception
			' if it breaks then it was unable to rename the directory
			
			' A. FILE ALREADY EXISTS
			' - tried to change to another file which is there
			' - tried to change to another capitilization of the same file
			' B. LACK OF PERMISSIONS
			Console.WriteLine("ERROR: File not renamed - {0} <WITH> {1}", item.FullName(), newNameString)
		End Try
	End Sub
	

	
End Class
