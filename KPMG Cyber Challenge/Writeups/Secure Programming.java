/*------------------------
*
*   SECURE PROGRAMMING
*
*------------------------*/

// 1) Path traversal in Java Web App

// A) Simple input sanitizing

if (!input.contains("/..") && !input.contains("../")) {
  //... then no path traversal is possile ...//
}

// B) Fail if path traversal OR if absolute path supplied
// If absolute path and canconical path do not match then there must be path traversal

public void failIfDirectoryTraversal(String relativePath)
{
    File file = new File(relativePath);

    if (file.isAbsolute())
    {
        throw new RuntimeException("Directory traversal attempt - absolute path not allowed");
    }

    String pathUsingCanonical;
    String pathUsingAbsolute;
    try
    {
        pathUsingCanonical = file.getCanonicalPath();
        pathUsingAbsolute = file.getAbsolutePath();
    }
    catch (IOException e)
    {
        throw new RuntimeException("Directory traversal attempt?", e);
    }


    // Require the absolute path and canonicalized path match.
    // This is done to avoid directory traversal 
    // attacks, e.g. "1/../2/" 
    if (! pathUsingCanonical.equals(pathUsingAbsolute))
    {
        throw new RuntimeException("Directory traversal attempt?");
    }
}

// 2) Securing SQL Injection in Java Web App

// A) Output Escape HTML input

String output = ESAPI.encoder().escapeForHTML(String s);

// B) Escape SQL input

String output = ESAPI.encoder().escapeForSQL(String s);

// C) Input validation

Validator.PathFile=^([a-zA-Z]:)?(\\\\[\\w. -]+)+$;

ESAPI.validator().getValidInput("PathFile", "C:\\TEMP\\file.txt", "PathFile", 100, false); // NOTE: for Validation Exceptions for failure to convert String to char[] use  ESAPI.validator().getValidDirectoryPath()
