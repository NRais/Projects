class Far {

  val list1 = List(2,4,6,8)
  val list2 = List("x", "y")

  val qq = for {
    x <- this.list1
    y <- this.list2
    z <- this.list1
  } yield x*z + "-" + y


  println("function " + qq)

  // OPTION
  // NOTE: this will return None, because one is none all are none

  /*  val upper = for {
       name <- request getParameter "name"
       trimmed <- Some(name.trim)
       upper <- Some(trimmed.toUpperCase) if trimmed.length != 0
    } yield upper

     println(upper getOrElse "")

    val nameMaybe = request getParameter "name"
    nameMaybe match {
       case Some(name) =>
           println(name.trim.toUppercase)
       case None =>
           println("No name value")
    }

    val q2 = for {
    x <- Some("blah")
    y <- None
    z <- Some("lbah")
    } yield x + y + z

   println("Optionals " + q2)


   // EITHER
   // in general Left is an error message nad right is normal
   // left overrides right (so right, right, right returns, right right left throws left)
   val q3 = for {
   x <- Right("blah")
   y <- Left("err")
   z <- Right("lbah")
   } yield x + y + z

   println("Either " + q3)*/
}
