package TD.EntityComponentSystem.Systems

import TD.EntityComponentSystem.{Composable, Entity, EntityPool, FreezeStatusComponent, PhysicsComponent, ProjectileComponent, SpeedComponent}
import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.Buffer

/** Physics System
 *
 *  Moves entities
 * */
object PhysicsSystem extends System {
  val classComponent = classOf[PhysicsComponent]

  def onFrameForEntity(entity: Entity) = {
    // Get components
    val physical = entity.getComponent(classOf[PhysicsComponent]).get
    val speed = entity.getComponent(classOf[SpeedComponent])
    val freeze = entity.getComponent(classOf[FreezeStatusComponent])

    // Limit speed based on status effects & speed component
    if (speed.isDefined) {
      physical.speed.limit(speed.get.speed)
    }

    if (freeze.isDefined) {
      physical.speed.limit(freeze.get.slow)
    }

    physical.position = physical.position.add(physical.speed)
  }
}
