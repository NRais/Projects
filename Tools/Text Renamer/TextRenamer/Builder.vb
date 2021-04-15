Imports System.IO

' Created by SharpDevelop.
' User: Nuser
' Date: 17/04/2018
' Time: 8:31 p.m.
' 
' To change this template use Tools | Options | Coding | Edit Standard Headers.
'
Public Class Builder

	Dim aL As String
	Dim aR As String
	
	Dim changes As Boolean = false ' boolean so we don't loop forever once we are done

	' to allow for good string manipulation we have this advanced class
	' this class 'Builds' the filename out of the advanced tags used
	' advanced tags always have {{XX}} around them (or ~:XX:~ as the alt option)
	' advanced tags can be used in file names or when editing the files themselves
	
	' ADVANCED TAGS INCLUDE:
	'  < STRINGS FOR EDITING >
	'  - ^{{FILE_NAME}} - to allow the user to insert the files original name
	'  - ^{{FILES_EXTENSION}} - to insert the files original extension (without dot)
	'  - {{<X<}} - everything to the left of X in the file name
	'  - {{>X>}} - everything to the right of X in the file name
	'  - ^{{DIRECTORY_NAME}} - to insert the files original directory name (no /'s)
	'  - ^{{PATH_TO_FILE}} - to insert the files original path (SHOULD NOT BE USED IN A FILE NAME)
	'  - ^{{NEW_LINE}} - to insert a new line (SHOULD NOT BE USED IN A FILE NAME)
	'  - ^{{TOTAL_NUMBER}} - an integer of the total count of files EDITED
	'  - ^{{FILE_NUMBER}} - an integer of the count of the files EDITED in the current folder
	'  - NOTE: buffer chars (ex. 009) should be optional in the above two methods
	
	'  - ^{{DATE_YEAR}} - current clock
	'  - ^{{DATE_MONTH}} - current clock
	'  - ^{{DATE_DAY}} - current clock
	
	' < GENERIC MODIFICATIONS >
	'  - NOTE: Generic options can be included ONCE each in any part of the string (they will be removed and their action will be applied)
	'  - NOTE: Generic options always take effect last, IN THE ORDER THEY ARE LISTED IN
	'  - ^{{CASE_UPPER}} - if included will change any entire string to upper case
	'  - ^{{CASE_LOWER}} - if included will change any entire string to lower case
	'  - ^{{CASE_TITLE}} - if included will change any entire string to Camel Caps (all letters followed by spaces)
	
	'  - {{REMOVE_L_##}} - remove the first ## characters on the left of the string
	'  - {{REMOVE_R_##}} - remove the last ## characters on the right of the string
	'  - {{REMOVE_TO_##}} - remove until the ##th character
	'  - {{REMOVE_FROM_##}} - remove after the ##th character
	
	'  - {{TRIM_@}} - at the end all @ chars will be removed from the start and end of the string
	'  - {{SWAP_@}} - at the end all the characters after @ will be swapped with all the characters before it (SHOULD NOT BE USED IF MORE THAN ONE @ char exists)
	
	' < FOR SEARCHING >
	'  - {{LENGTH_##}} - if their are ## chars in the string (For example: John{{LENGTH_15}} if their are 15 chars following a occurance of "John")
	'  - {{##}} - everything that can be converted to a number that is next to eachother (For example: John{{##}} on John334Tyson.txt and ABigJohn2.txt but not JohnT88.txt)
	
	' EDIT FUNCTION: to allow user to insert aspects into their strings
	Public Function EditName(currentString As String, item As FileInfo) As String
		' store adv char characters in a shorter variable name
		aL = MainProgram.MySettings.advCharL
		aR = MainProgram.MySettings.advCharR
		
        currentString = replaceAforB(currentString, aL &  "FILE_NAME" 				& aR, Path.GetFileNameWithoutExtension(item.Name))
        currentString = replaceAforB(currentString, aL &  "FILES_EXTENSION" 		& aR, item.Extension)
        currentString = replaceAforB(currentString, aL &  "DIRECTORY_NAME" 			& aR, item.Directory.Name)
        currentString = replaceAforB(currentString, aL &  "PATH_TO_FILE" 			& aR, item.DirectoryName)
        currentString = replaceAforB(currentString, aL &  "FILE_NUMBER" 			& aR, CStr(MainProgram.MySettings.file_count))
        currentString = replaceAforB(currentString, aL &  "TOTAL_NUMBER" 			& aR, CStr(MainProgram.MySettings.total_count))
        currentString = replaceAforB(currentString, aL &  "NEW_LINE" 				& aR, Environment.NewLine)
        
        currentString = replaceAforB(currentString, aL &  "DATE_YEAR" 				& aR, CStr(Date.Today.Year))
        currentString = replaceAforB(currentString, aL &  "DATE_MONTH" 				& aR, CStr(Date.Today.Month))
        currentString = replaceAforB(currentString, aL &  "DATE_DAY" 				& aR, CStr(Date.Today.Day))
	        	    
        Return currentString
    End Function
    
    ' MODIFY FUNCTION: to allow user to modify their entire strings
    Public Function ModName(currentString As String, item As FileInfo) As String
    
		' loop through all of these repeatedly until they have all been resolved
		' the reason we do this is because we want to apply them in the order the are found
		Do While (currentString.Contains(aL) And currentString.Contains(aR) And changes = false)
			changes = false 'reset change counter, if it is still false at the end we will stop because nothing is being done
		
	   		' this calculates what the next modification is (by cutting out the string between {{ and }})
	   		Dim start As Integer = currentString.indexOf(aL)
	   		Dim length As Integer = currentString.indexOf(aR) + 2 - currentString.indexOf(aL)
	   		
	   		Dim modification As String = currentString.Substring(start, length)
	   		
			' check which mod matches the next modification and apply that
			' NOTE: at the end of these mods we pass the new string to the method, the new string is used if the method is true
			' TODO: NOTE: case modifications cannot be done alone
			currentString = genericModification(modification, currentString, aL & "CASE_UPPER"		& aR, StrConv(currentString.replace(modification, ""), VbStrConv.UpperCase)) 
			currentString = genericModification(modification, currentString, aL & "CASE_LOWER"		& aR, StrConv(currentString.replace(modification, ""), VbStrConv.LowerCase))   
			currentString = genericModification(modification, currentString, aL & "CASE_TITLE"		& aR, StrConv(currentString.replace(modification, ""), VbStrConv.ProperCase))		
	    
	    Loop  

		Return currentString
    End Function
    
    
    
    
    
    
    
    
    
    
    Function replaceAforB(theString As String, findString As String, replacementString As String) As String
        
        ' if there is any occurances to edit then do so
        If theString.contains(findString) Then
        
	        ' cut the array into pieces seperated by the string
	        Dim tempArray() As String = theString.Split(findString.ToCharArray, StringSplitOptions.None)
	        Dim tempString As String = ""
	        
	        ' for each of the parts of the string
	        For i = 0 To tempString.length - 1
	        
	        	' if it is within the range being searched
	        	If (i > MainProgram.MySettings.findOccurance(0)) And (i < MainProgram.MySettings.findOccurance(1)) Then
					' then add on that section of the string, AND the replacement
					tempString = tempString + tempArray(i) + replacementString
				Else	
					' otherwise add on that section of the string, AND the old thing (un-replaced)
					tempString = tempString + tempArray(i) + findString
				End If
	        Next
	        
	        theString = tempString
	        
	    End If
        
        ' OLD METHOD
        'If (MainProgram.MySettings.findOccurance(1) = -1) Then
        '	' then find all the occurances and replace
        '	theString = theString.replace(findString, replacementString)
        'End If
        
        Return theString
    End Function
    
   	Function genericModification(modification As String, theString As String, testCase As String, replacementString As String) As String
  		
   		' if the correct mod is this generic mod string (then the user wants this modification) so change it to the new string and then apply the mod
   		If (modification = testCase) Then
   			theString = replacementString		'use the replacement and remove the modification string
   			
   			changes = true
   		End If
   		
   		return theString
   	End Function
	
End Class
