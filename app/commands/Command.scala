package commands

trait Command[T] {
  def execute(t: T): T
}
