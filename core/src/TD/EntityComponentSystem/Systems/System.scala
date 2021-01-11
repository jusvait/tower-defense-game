package TD.EntityComponentSystem.Systems

import TD.EntityComponentSystem.{Composable, Entity, EntityPool}

import scala.collection.mutable
/** Base class for systems
 *
 * classComponent represents the component a system handles
 * */
abstract class System() {
  val classComponent: Class[_ <: Composable]

  /** onFrame updates entities with a system's classComponent*/
  def onFrame(entityPool: EntityPool) = entityPool.entities.filter(_.hasComponent(classComponent))
                                              .foreach(onFrameForEntity)

  /** Update single entity */
  def onFrameForEntity(entity: Entity)
}
