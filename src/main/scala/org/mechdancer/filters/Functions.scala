package org.mechdancer.filters

object Functions {
  //(memory,input)->(memory,output)
  def delay(state: (List[Double], Double)): (List[Double], Double) =
    (state._1.tail.::(state._2), state._1.head)

  //(k,input)->(k,output)
  def gain(state: (Double, Double)): (Double, Double) = (state._1, state._1 * state._2)

  //(last,input)->(current,output)
  def difference(state: (Double, Double)): (Double, Double) = (state._2, state._2 - state._1)

  //(sum,input)->(sum,output)
  def integral(state: (Double, Double)): (Double, Double) = {
    val temp = state._1 + state._2
    (temp, temp)
  }

  //(parameter,input)->(parameter,output)
  //parameter:=(kp,ki,kd,sum,last)
  //input:=error
  def pid(state: ((Double, Double, Double, Double, Double), Double))
  : ((Double, Double, Double, Double, Double), Double) = {
    val ((kp, ki, kd, sum, last), error) = state
    val newSum = sum + error
    ((kp, ki, kd, newSum, error), kp * error + ki * newSum + kd * (error - last))
  }

  //((min,max),input)->((min,max),output)
  def limit(state: ((Double, Double), Double)): ((Double, Double), Double) = {
    val ((min, max), input) = state
    ((min, max), math.max(min, math.min(max, input)))
  }
}
