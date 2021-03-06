package breeze.linalg.support

import breeze.collection.mutable.Beam
import breeze.linalg.{QuasiTensor, argtopk}

/**
 * TODO
 *
 * @author dlwh
 **/
private[linalg] trait LowPriorityArgTopK {
  implicit def argtopkWithQT[Q, I, V](
      implicit qt: Q <:< QuasiTensor[I, V],
      ord: Ordering[V]): argtopk.Impl2[Q, Int, IndexedSeq[I]] = {
    new argtopk.Impl2[Q, Int, IndexedSeq[I]] {

      def apply(q: Q, k: Int): IndexedSeq[I] = {
        implicit val ordK = ord.on(q.apply)
        val queue = new Beam[I](k)
        queue ++= q.keysIterator
        queue.toIndexedSeq.sorted(ordK.reverse)
      }
    }
  }
}
