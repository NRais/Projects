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
	
	Dim changes As Boolean = false ' boolean so we don't loop through the Mod's forever once we are done

	' to allow for good string manipulation we have this advanced class
	' this class 'Builds' the filename out of the advanced tags used
	' advanced tags always have {{XX}} around them (or ~:XX:~ as the alt option)
	' advanced tags can be used in file names or when editing the files themselves
	
	' ADVANCED TAGS INCLUDE:
	'  < STRINGS FOR EDITING >
	'  - ^{{FILE_NAME}} - to allow the user to insert the files original name
	'  - ^{{FILES_EXTENSION}} - to insert the files original extension (*without dot)
	'  - {{<X<}} - everything to the left of X in the file name
	'  - {{>X>}} - everything to the right of X in the file name
	'  - ^{{DIRECTORY_NAME}} - to insert the files original directory name (no /'s)
	'  - ^{{PATH_TO_FILE}} - to insert the files original path 					*(SHOULD NOT BE USED IN A FILE NAME)
	'  - ^{{NEW_LINE}} - to insert a new line 									*(SHOULD NOT BE USED IN A FILE NAME)
	'  - ^{{TOTAL_NUMBER}} - an integer of the total count of files EDITED
	'  - ^{{FILE_NUMBER}} - an integer of the count of the files EDITED in the current folder
	'  - NOTE: buffer chars should be optional in the above two methods (ex. two buffer 0's -> 009)
	
	'  - ^{{DATE_YEAR}} - current clock
	'  - ^{{DATE_MONTH}} - current clock
	'  - ^{{DATE_DAY}} - current clock	
	
	' < FOR SEARCHING >
	'  - {{*}} - anything that can be converted into a character (so pretty much everything)
	'  - {{#}} - anything that can be converted into an integer 
	'  - {{A}} - any normal alphebetical letter
	'  - {{!#}} - nothing that can be converted into an integer
	'  - {{!A}} - nothing that can be converted into an alphebetical letter
	'
	' NOTE: John{{@}}{{@}}{{@}}John  -> will find "John123John" & "JohnKKKJohn" & not "JohnJohn"
	' NOTE: John{{!#}} -> will find "JohnTT" & "JohnT5" & "John#RER" & not "John7TER"
	
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

	
	' EDIT FUNCTION: to allow user to insert aspects into their strings
	Public Function EditName(currentString As String, item As FileInfo) As String
		' store adv char characters in a shorter variable name
		aL = MainProgram.MySettings.advCharL
		aR = MainProgram.MySettings.advCharR
		
        currentString = replaceAforB(currentString, aL &  "FILE_NAME" 				& aR, Path.GetFileNameWithoutExtension(item.Name))
        currentString = replaceAforB(currentString, aL &  "FILE_EXTENSION" 			& aR, (item.Extension).Replace(".", ""))
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
		' the reason we do this is because we want to apply them in the order they are found
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
    
    
    ''''''''''''''''''''''''''''''''''''''''''''''''''''
    '''''''''''''''''''' TOOOOOOOLS ''''''''''''''''''''
    
    
    Function replaceAforB(theString As String, findString As String, replacementString As String) As String
        
        ' this is the REPLACEMENT function
        ' It finds occurances and replaces them with the correct data
        ' It is primarily used for replacing macros with their required code (ex. {{FILE_NAME}} -> John)
        
        ' It takes 3 arguments (String to check for replacements, Macro to find, Data to insert in those locations)
        ' the reason it is so complicated and not just a simple Replace is because it accomadates replacing only certain occurances of a string for example 2nd - 4th occurance
        
        ' _-----------_
        '
        ' _           _
        '  -----------
        
        ' THREE STEPS '
        ' 1. - Check if there are any occurances so we don't waste time
        
        ' 2. - skip through the string until our starting occurance
        
        ' 3. - replace the correct number of occurances after that
        
        If theString.contains(findString) Then   ' #1#
        
        	Console.WriteLine("IN")
        
        	Dim startingIndex As Integer = 0
        	' the first occurance we want to replace (for example it may be the 2nd)
        	Dim startingOccurance As Integer = MainProgram.MySettings.findOccurance(0)
        	' the number of occurances we want to replace (for example it may be 3)
        	' we do a +1 because it is inclusive for example 2-4 is 3 total replacements 2,3,4 (even normally though 4-2 = 2)
        	Dim totalReplacements As Integer = 1 + MainProgram.MySettings.findOccurance(1) - MainProgram.MySettings.findOccurance(0)
        
        	' Check if all replacements are being done (if so then specify that)
        	If (MainProgram.MySettings.findOccurance(1) = -1) Then
        		' then instead just set it to -1 which means all
        		totalReplacements = -1
        	End If
        
        	Console.WriteLine("* Finding " & findString & " in STRING: " & theString)
        
        	' #########2#########
        	' establish starting occurance
        	' IF WE ARE RUNNING AN ADVANCED TAG WE WILL NOT DO THIS
        	If Not (findString.Contains(aL) And findString.Contains(aR)) Then
        	
        		Console.WriteLine("* NOT ADVANCED applying filters: Start " & startingOccurance & " , Total " & totalReplacements)
        	
        		' check what our starting index is going to be
        		' (for Advanced Tag we will change everything)
        		startingIndex = GetNthIndexStringFunc(theString, findString, startingOccurance)
        	
        	End If
        	        	
        	' #########3#########
        	' replace all following occurances using the Strings.Replace function
        	' Note: the replace function takes the following arguments:
        	' *expression* 	- theString
        	' *find* 		- findString
    		' *replace* 	- replacementString
        	' *start* 		- startingIndex
        	' *count* 		- totalReplacements
        	
        	' NOTE: startingIndex +1 because it starts with 1 as the first letter of the string (i think)
        	' ***** '
        	' The new string will be:
        	' A. the string up to the Starting index [no change will be made]
        	' B. the string after the Starting index [it will be searched for replacements]
        	
        	' NOTE: only search and replace if we have a valid starting index
        	If Not (startingIndex = -1) Then
        		' RUN the Replacement
        		theString = theString.Substring(0, startingIndex) & Replace(theString, findString, replacementString, startingIndex + 1, totalReplacements)
        	
        		Console.WriteLine("* Result: {0}", theString)
        	Else
        		' otherwise
        		Console.WriteLine("* No Valid Starting Index for searching the string: " & theString)
        	End If
        End If
                
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
   	
   	
   	'''''''''''''''''''
   	' EXTRA FUNCTIONS '
   	'''''''''''''''''''
   	
   	' NOTE: works with everything that does not include "~" !!!!!!!! NOTE !!!!!!!!!
   	'
   	' NOTE: this finds overlapping indexs:
   	'
   	'		ToKiaiKaiaKs
   	'
   	' Will return index = 7 as the 2nd index
   	
   	' --HOWEVER--
   	' The String.Replace() Function will act as if there is only 1 occurance in total (not 2) and will replace the first one (and never see the second)
   	'
   	' This is okay as long as you remember that this happens
   	
   	 Public Function GetNthIndexStringFunc(search As String, find As String, index As Integer) As Integer
		' NOTE: we must try and find at least the 1st occurance
		Dim x As Integer = -1 ' if nothing is found then this will be returned
						
		' for each occurance down to the nth, while they are still occurances
		Do While index > 0 And search.contains(find)
		
			' get the index of the occurnace
			x = search.IndexOf(find)
		
	        
	        ' then modify the first character of that index (so it no long is the first index)
	        
	        ' Our new string is the old string up the the start of our find occurance
	        ' & the tilde character (instead of whatever used to be there)
	        ' & the rest of our string after wards
	        search = search.Substring(0, search.IndexOf(find)) & "~" & search.Substring(search.IndexOf(find) + 1)
	        	       	
	       	' then decrement the counter and loop again
	       	index = index - 1
       	Loop
       	
       	' If we actually got through until our index then good otherwise return a -1
       	If (index = 0) Then
			Return x
		Else
			Return -1
		End If
	    
    End Function
	
End Class
