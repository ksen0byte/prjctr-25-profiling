import scala.annotation.tailrec

@main
def main(): Unit = {
  val bst = new BST[String, Long]
  insertNodes(bst, 50000000L)
}

def insertNodes(bst: BST[String, Long], num: Long): Unit =
  (1L to num).foreach(bst.insert("key", _))

class BST[K, V](implicit ord: Ordering[K]) {
  private sealed trait Tree[K, V]
  private case object Leaf                                                         extends Tree[K, V]
  private case class Branch[K, V](left: Tree[K, V], right: Tree[K, V], k: K, v: V) extends Tree[K, V]

  private var root: Tree[K, V] = Leaf

  def find(k: K): Option[V]       = find(root, k)
  def insert[A](k: K, v: V): Unit = this.root = insert(root, k, v)

  @annotation.tailrec
  private def find(t: Tree[K, V], key: K): Option[V] = t match {
    case Leaf => None
    case Branch(l, r, k, v) =>
      if (ord.equiv(k, key)) Some(v)
      else if (ord.lt(key, k)) find(l, key)
      else find(r, key)
  }

  private def insert(t: Tree[K, V], key: K, value: V): Tree[K, V] = t match {
    case Leaf                                    => Branch(Leaf, Leaf, key, value)
    case Branch(l, r, k, v) if ord.equiv(k, key) => Branch(l, r, k, value)
    case Branch(l, r, k, v) if ord.lt(key, k) =>
      Branch(insert(l, key, value), r, k, v)
    case Branch(l, r, k, v) => Branch(l, insert(r, key, value), k, v)
  }

}
