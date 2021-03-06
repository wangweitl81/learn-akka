package com.allaboutscala.learn.akka.fsm

import akka.actor.ActorSystem
import akka.testkit.{TestKit, ImplicitSender, DefaultTimeout, TestFSMRef}
import com.allaboutscala.learn.akka.fsm.Tutorial_09_AkkaFSM_PartSix._
import org.scalatest.{WordSpecLike, BeforeAndAfterAll, Matchers}

/**
  * Created by Nadim Bahadoor on 28/06/2016.
  *
  *  Tutorial: Learn How To Use Akka
  *
  * [[http://allaboutscala.com/scala-frameworks/akka/]]
  *
  * Copyright 2016 Nadim Bahadoor (http://allaboutscala.com)
  *
  * Licensed under the Apache License, Version 2.0 (the "License"); you may not
  * use this file except in compliance with the License. You may obtain a copy of
  * the License at
  *
  *  [http://www.apache.org/licenses/LICENSE-2.0]
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  * License for the specific language governing permissions and limitations under
  * the License.
  */
class DonutBakingActorFSMTests
  extends TestKit(ActorSystem("DonutActorFSM"))
    with ImplicitSender
    with DefaultTimeout
    with WordSpecLike
    with BeforeAndAfterAll
    with Matchers {

  private var donutBakingActorFSM: TestFSMRef[BakingStates, BakingData, DonutBakingActor] = _

  override protected def beforeAll(): Unit = {
    donutBakingActorFSM = TestFSMRef(new DonutBakingActor())
  }

  "DonutBakingActor" should {
    "have initial state of BakingStates.Stop" in {
      donutBakingActorFSM.stateName shouldEqual Stop
    }
  }

  import scala.concurrent.duration._
  "DonutBakingActor" should {
    "process BakeDonut event and switch to the BakingStates.Start state" in {
      donutBakingActorFSM ! BakeDonut
      awaitCond(donutBakingActorFSM.stateName == Start, 2 second, 1 second)
    }
  }

  "DonutBakingActor" should {
    "process StopBaking event and switch to BakingStates.Stop state" in {
      donutBakingActorFSM ! StopBaking
      awaitCond(donutBakingActorFSM.stateName == Stop, 2 second, 1 second)
    }
  }


  "DonutBakingActor current donut quantity" should {
    "equal to 1 after the StopBaking event" in {
      donutBakingActorFSM.stateData.qty shouldEqual 1
    }
  }

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }
}

