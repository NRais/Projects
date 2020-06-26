class Foo {
    
    // note if you change these values to not be Lists then the monads will change for all subsequent actions, because the Monad is implicitly coded into List
    val list1 = List(2,4,6,8)
    val list2 = List("x", "y")


    System.out.println("Hello")

    val res = list1.map(x => x + 3)

    println("function " + res)

    val rez = list1.map(x => list2.map(y => x + " - " +  y))

    println("2d " + rez)

    val req = list1.flatMap(x => list2.map(y => x + " - " +  y))        // nesting the composition

    println("flat 2d " + req)

    val qq = for {
        x <- this.list1
        y <- this.list2
    } yield x + "-" + y

    // this unfolded code does the EXACT same thing as the flat map map compination

    println("unfolder flat 2d " + qq)
}