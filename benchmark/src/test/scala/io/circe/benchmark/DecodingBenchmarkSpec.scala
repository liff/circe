package io.circe.benchmark

import java.util.concurrent.atomic.AtomicLong
import com.google.monitoring.runtime.instrumentation.{AllocationRecorder, Sampler}
import org.scalatest.FlatSpec

class DecodingBenchmarkSpec extends FlatSpec {
  val benchmark: DecodingBenchmark = new DecodingBenchmark

  import benchmark._

  private[this] final def createSampler() = new Sampler {
//    private[this] val buf = Vector.newBuilder[String]
    private[this] val counter = new AtomicLong(0)

    override final def sampleAllocation(count: Int, desc: String, newObj: Any, size: Long): Unit = {
//      buf += s"count=$count desc=$desc size=$size"
      counter.incrementAndGet()
    }

//    def result(): Vector[String] = buf.result()
    def result(): Long = counter.get()
  }

/*
  "The decoding benchmark" should "correctly decode integers using Circe" in {
    assert(decodeIntsC === ints)
  }

  it should "correctly decode integers using Argonaut" in {
    assert(decodeIntsA === ints)
  }

  it should "correctly decode integers using Play JSON" in {
    assert(decodeIntsP === ints)
  }

  it should "correctly decode integers using Spray JSON" in {
    assert(decodeIntsS === ints)
  }

  it should "correctly decode case classes using Circe" in {
    assert(decodeFoosC === foos)
  }

  it should "correctly decode case classes using Argonaut" in {
    assert(decodeFoosA === foos)
  }

*/
  it should "correctly decode case classes using Play JSON" in {
    val sampler1 = createSampler()
    AllocationRecorder.addSampler(sampler1)
    try {
//      assert(decodeFoosA === foos)
      assert(decodeFoosC === foos)
//      assert(decodeFoosP === foos)
//      assert(decodeFoosS === foos)
    } finally {
      AllocationRecorder.removeSampler(sampler1)
    }
    val allocations1 = sampler1.result()
    println(s"run 1: $allocations1 allocations")

    val sampler2 = createSampler()
    AllocationRecorder.addSampler(sampler2)
    try {
//      assert(decodeFoosA === foos)
      assert(decodeFoosC === foos)
//      assert(decodeFoosP === foos)
//      assert(decodeFoosS === foos)
    } finally {
      AllocationRecorder.removeSampler(sampler2)
    }
    val allocations2 = sampler2.result()
    println(s"run 2: $allocations2 allocations")
  }
/*

  it should "correctly decode case classes using Spray JSON" in {
    assert(decodeFoosS === foos)
  }
*/
}
