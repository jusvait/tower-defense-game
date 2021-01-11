package TD.EntityComponentSystem

import TD.utils.ComponentMap

/** Entity
 *
 *  An entity represents a game object with its defined components
 *  @param Host Entity Component System this entity is bound to
 * */
class Entity(val Host: ECS) {

  // Initialize component map
  private val components: ComponentMap = new ComponentMap()

  /** Obtain an Option of specified component */
  def getComponent[T](classVal: Class[T]) = this.components.getComponent[T](classVal)

  /** Add a new component for the entity
   *  if the entity has the component already, replace the component */
  def addComponent(component: Composable) = this.components.addComponent(component)

  /** Check whether entity has a specific component */
  def hasComponent[T](classVal: Class[T]) = this.components.contains[T](classVal)

  /** Remove the specified component
   *
   * @return removed component, None if no component was removed*/
  def removeComponent[T](classVal: Class[T]) = this.components.removeComponent(classVal)

  /** Get all component belonging to this entity */
  def getComponents = this.components.getComponents

  /** Reset the entity
   *
   * Resetting increments an entity's generation by one, kills it and removes all components except EntityComponent
   * */
  def reset() = {
    this.components.getComponent(classOf[EntityComponent])
      .getOrElse(throw new Exception("No entity component for an entity was found"))
      .reset()
    this.components.clean()
  }
}