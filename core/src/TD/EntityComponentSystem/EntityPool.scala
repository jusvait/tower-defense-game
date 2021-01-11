package TD.EntityComponentSystem

import scala.collection.mutable.Buffer

/** Entity pool
 *
 *  Helper class to store entities in
 *
 *  @param ecs Entity Component System this pool is bound to
 * */
class EntityPool(val ecs: ECS) {
  val entities: Buffer[Entity] = Buffer()

  /** Get active entities */
  def getActive = this.entities.filter(_.getComponent(classOf[EntityComponent]).get.isAlive)

  /** Return entities with specified component */
  def getEntitiesWithComponent[T](classVal: Class[T]) =
    this.entities.filter(_.hasComponent(classVal))

  /** Find entity based on entityID and generation */
  def findEntity(target: Target) = this.entities.find(entity => {
    val ent = entity.getComponent(classOf[EntityComponent]).get
    ent.entityId == target.target && ent.generation == target.gen
  })

  /** Reset an entity in the pool */
  def removeEntity(entity: Entity) = {
    entity.reset()
  }
}
