package TD.EntityComponentSystem.Systems
import TD.EntityComponentSystem.{Composable, Entity, EntityPool, PathingComponent, PhysicsComponent}
import TD.utils.EntityHasReachedGoal

/** Pathing system
 *
 *  Set directions for entities with a pathing component
 * */
object PathingSystem extends System {
  override val classComponent = classOf[PathingComponent]

  override def onFrameForEntity(entity: Entity) = {
    // Get components
    val physical = entity.getComponent(classOf[PhysicsComponent]).get
    val pathing = entity.getComponent(classComponent).get
    val targetPos = pathing.path(pathing.currentTarget)

    // If distance from target is less than 1 unit, switch to next target
    if (targetPos.dst(physical.position) < 1.0f) {
      if (pathing.currentTarget == pathing.path.length - 1) {
        entity.Host.eventHandler.addJob(EntityHasReachedGoal(entity))
      } else {
        pathing.currentTarget += 1
      }
    }

    physical.speed = targetPos.cpy().sub(physical.position.cpy()).limit(2f)

  }


}
