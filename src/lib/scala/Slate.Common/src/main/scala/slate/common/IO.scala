/**
  * <slate_header>
  * author: Kishore Reddy
  * url: https://github.com/kishorereddy/scala-slate
  * copyright: 2016 Kishore Reddy
  * license: https://github.com/kishorereddy/scala-slate/blob/master/LICENSE.md
  * desc: a scala micro-framework
  * usage: Please refer to license on github for more info.
  * </slate_header>
  */
package slate.common

import slate.common.results.ResultCode

/**
  * ========================================================================
  * IO abstraction - serves as an light-weight alternative to IO Monad.
  *
  * All IO actions ( in other slate kit components ) will be replaced later
  * by this IoAction[I,O] trait.
  *
  * NOTE: This IoAction[I,O] is not monadic to avoid ( from what i understand )
  * a potential stack overflow issue as outlined in the 1st link below.
  * The solution ( in the link ) involves usage of a Trampoline to avoid Overflow
  * 1. http://etorreborre.blogspot.com/2011/12/pragmatic-io.html
  *
  * This implementation does not itself implement map/flatmap but wraps the
  * io call inside an try/catch and returns the SlateKit Result[O] which is
  * basically a slight variation to Try/Success/Failure.
  *
  * This is a decent alternative that can be used to for unit-testing
  * and abstraction real IO actions.
  * ========================================================================
  */
/**
  *
  * @tparam I    : The type of the input parameter
  * @tparam O    : The type of the output parameter
  * @return      : Option[O] of output parameter
  */
trait IoAction[I,+O] {
  def run(input:I): Result[O]
}


/**
  *
  * @param action: The output parameter
  * @tparam I    : The type of the input parameter
  * @tparam O    : The type of the output parameter
  * @return      : Option[O] of output parameter
  */
class IoWrap[I,+O](action:(I) => O) extends IoAction[I,O]
{
  def run(input:I): Result[O] = {
    val result = try {
      val output = action(input)
      new SuccessResult(output, ResultCode.SUCCESS)
    }
    catch {
      case ex: Exception => new FailureResult(ResultCode.FAILURE, err = Some(ex))
    }
    result
  }
}


/**
  *
  * @param action: The output parameter
  * @tparam I    : The type of the input parameter
  * @tparam O    : The type of the output parameter as Result[O]
  * @return      : Result[O] of output parameter
  */
class IoFlat[I,+O](action:(I) => Result[O]) extends IoAction[I,O]
{
  def run(input:I): Result[O] = {
    val result = try {
      val output = action(input)
      output
    }
    catch {
      case ex: Exception => new FailureResult(ResultCode.FAILURE, err = Some(ex))
    }
    result
  }
}


class Print    extends IoWrap[Any, Unit]      ((input) => Predef.print(input))
class PrintLn  extends IoWrap[Any, Unit]      ((input) => Predef.println(input))
class GetLn    extends IoWrap[String, String] ((input) => scala.io.StdIn.readLine(input))



//http://etorreborre.blogspot.com/2011/12/pragmatic-io.html
//http://functionaltalks.org/2013/06/20/paul-chiusano-how-to-write-a-functional-program-with-io-mutation-and-other-effects/

/*
class IO[+A](ioAction:() => A)
{
  def run(): A = ioAction()


  def map[B]( f: A => B ) : IO[B] = {
    new IO[B]( () => { f( ioAction() ) })
  }


  def flatMap[B]( f: A => IO[B]): IO[B] = {
    new IO[B]( () => { f( ioAction() ).run() })
  }
}
case class Print    (msg:String     ) extends IO[Unit]  ( () => Predef.print(msg) )
case class PrintLn  (msg:String     ) extends IO[Unit]  ( () => Predef.println(msg) )
case class GetLn    (msg:String     ) extends IO[String]( () => scala.io.StdIn.readLine() )
*/