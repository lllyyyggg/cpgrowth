package com.lanyage.version.one.scala

object HelloWorld {
  def main(args: Array[String]): Unit = {
    //    println("hello world")
    //    var x = 1
    //    val s = "Hello world"
    //    println(x, s)

    val numList = List(1, 2, 3, 4, 5)

    val retVal = for {item <- numList if item != 3; if item < 8} yield item

    println(retVal)
    for (item <- retVal) {
      println("value of item " + item)
    }

  }
}

// private
class Outer {

  class Inner {
    private def f(): Unit = {
      println("f")
    }

    (new Inner).f()
  }

  // Error
  //  (new Inner).f()
}


// protected
class Super {
  protected def f(): Unit = {
    println("f")
  }

  class Sub extends Super {
    f()
  }

  class Other {
    (new Super).f()
  }

}


object Test {
  var add = (x: Int) => (y: Int) => x + y

  def addInt(x: Int, y: Int): Int = {
    return x + y
  }

  def main(args: Array[String]): Unit = {
    //    函数
    //    print(addInt(1, 2))
    //    val increment = add(1)
    //    print(increment(2))

    //    字符串
    //    val s = "hello world"
    //    val s2: String = "HELLO WORLD"
    //    println(s)
    //    println(s2)
    //
    //    val buf = new StringBuilder
    //    buf.append("hello ").append("world").append("!")
    //    println(buf.toString())
    //    println(s.length)
    //    val s3 = s.concat(s2)
    //    println(s3.hashCode)

    //    数组
    //    val sArray: Array[String] = new Array[String](3)
    //    val sArray2: Array[Int] = Array(1, 2, 3);
    //    sArray(0) = "hello"
    //    println(sArray(0))
    //    println(sArray2.length)

    //    var max: Int = 0
    //    for (i <- 0 to (sArray2.length - 1)) {
    //      if(max < sArray2(i)) max = sArray2(i)
    //      println(sArray2(i))
    //    }
    //    println(max)

    //    多位数组
    //    var myMatrix = Array(Array(1, 2, 3), Array(2, 3, 4))
    //    var myMatrix2 = Array.ofDim(3, 3)
    //    合并数组
    //    val arrayOne = Array(1, 2, 3)
    //    val arrayTwo = Array(4, 5, 6)
    //    val arrayThree = Array.concat(arrayOne, arrayTwo)
    //    for (i <- 0 to (arrayThree.length - 1)) {
    //      println(arrayThree(i))
    //    }

    //    数组区间
    //    val myList = Array.range(10, 20, 2)
    //    for (i <- 0 to (myList.length - 1)) {
    //      println(myList(i))
    //    }


    //    集合
    //    val x = List(1, 2, 3, 4)
    //    val y = Set("hello world")
    //    val m = Map("one" -> 1, "two" -> 2)
    //    val tup = (10, "hello")
    //    val opt: Option[Int] = Some(5)
    //    println(x)
    //    println(y)
    //    println(m)
    //    println(tup)
    //    println(opt)

    //    val opt2:Option[Int] = m.get("three")
    //    println(opt2)

    //    val itb = Iterator(20, 30, 40, 50)
    //    println(itb.max)
    //    while (itb.hasNext) {
    //      val now = itb.next()
    //      println(now)
    //    }

    //    var point = new Point(1, 2)
    //    println(point)

    //    new Circle(1)

    //    val array = Array(1, 2, 3, 4, 5, 6)

    //    高阶函数
    //    array.map(x => x * 2).filter(x => x < 10).foreach(println)
  }
}

//class Point(val xa: Int, val ya: Int) {
//  var x: Int = xa
//  var y: Int = ya
//
//  def move(dx: Int, dy: Int): Unit = {
//    x = x + dx
//    y = y + dy
//  }
//
//  override def toString: String = "(" + x + "," + y + ")"
//}

//    伴生对象，可以访问私有方法和私有属性
//object Point {
//
//}


//trait Equal {
//  println("特征构造器")
//  def isEqual(x: Any): Boolean
//  def isNotEqual(x: Any): Boolean
//}
//
//class Circle(val radius: Int) extends Equal {
//  var $radius: Int = radius;
//  override def isEqual(x: Any): Boolean = x.isInstanceOf[Circle] && x.asInstanceOf[Circle].radius == $radius
//
//  override def isNotEqual(x: Any): Boolean = ???
//}

//特征也有构造器
//调用超类的构造器；
//特征构造器在超类构造器之后、类构造器之前执行；
//特征由左到右被构造；
//每个特征当中，父特征先被构造；
//如果多个特征共有一个父特征，父特征不会被重复构造
//所有特征被构造完毕，子类被构造。