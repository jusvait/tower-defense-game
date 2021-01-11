package TD.utils

import TD.EntityComponentSystem.{Composable, EntityComponent}

import scala.collection.mutable.Map

/** Component map
 *
 * Helper class to store an entity's components */
class ComponentMap {

  private var components = Map[Class[_] , Composable]()

  /** Add a component to the map */
  def addComponent(component: Composable) = components(component.getClass) = component

  /** Obtain an Option of specified component */
  def getComponent[T](classVal: Class[T]): Option[T] = {
    if (components.contains(classVal)) {
      Some(components(classVal).asInstanceOf[T])
    } else None
  }

  /** Remove a component
   *
   * @return removed component, None if no component was removed
   * */
  def removeComponent[T](classVal: Class[T]) = {
    components.remove(classVal)
  }

  /** Check whether component map has a specified component */
  def contains[T](classVal: Class[T]) = this.components.contains(classVal)

  /** Get all component belonging to this entity */
  def getComponents = this.components.values.toArray

  /** Delete all components except EntityComponent */
  def clean() = {
    val ent = this.getComponent(classOf[EntityComponent]).get
    this.components = Map[Class[_] , Composable]()
    this.addComponent(ent)
  }

}