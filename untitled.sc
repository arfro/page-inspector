val l = List(List(1), List(2), List(3)).grouped(3).toList

def lint(left: List[String], right: List[List[Int]]): List[String] = {
  right.map({f =>
    println(s"processing $f")
      f.toString + "lala"
  })
}

l.foldLeft(List.empty[String])(lint)