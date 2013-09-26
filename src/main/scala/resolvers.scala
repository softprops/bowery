package bowery

trait Resolver {
  def resolve: Option[Package]
}

