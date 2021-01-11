package TD.EntityComponentSystem.Systems

import TD.CONSTANTS
import TD.EntityComponentSystem.{Entity, EntityComponent, EntityPool, PhysicsComponent, ProjectileComponent, SpeedComponent, TargetingComponent}
import TD.utils.{BurnEntity, FreezeEntity, HurtEntity, PoisonEntity, RemoveEntity}

/** Projectile system
 *
 *  Direct projectiles to their targets
 *  If a projectile's target is removed or killed, the projectile will continue moving forward until out of bounds
 */
object ProjectileSystem extends System {
  val classComponent = classOf[ProjectileComponent]

  override def onFrame(entityPool: EntityPool) = {
    entityPool.getEntitiesWithComponent(classComponent)foreach(projectile => {
      // Get components
      val target = projectile.getComponent(classOf[TargetingComponent])
      val physical = projectile.getComponent(classOf[PhysicsComponent]).get
      val projetileComponent = projectile.getComponent(classComponent).get

      // Check that a target is defined
      if (target.isDefined) {
        // Get target
        val targetEntity = entityPool.findEntity(target.get.target)

        // Check that the target entity exists & is alive
        if (!targetEntity.isDefined || !targetEntity.get.getComponent(classOf[EntityComponent]).get.isAlive) {
          if (physical.speed.isZero ||  physical.position.x > CONSTANTS.WINDOW_WIDTH || physical.position.x < 0 ||
            physical.position.y > CONSTANTS.WINDOW_HEIGHT ||physical.position.y < CONSTANTS.HUD_HEIGHT ) {
            projectile.Host.eventHandler.addJob(RemoveEntity(projectile))
          }
        } else {
          val targetPos = targetEntity.get.getComponent(classOf[PhysicsComponent]).get.position.cpy()
          val speed = projectile.getComponent(classOf[SpeedComponent]).get

          physical.speed = targetPos.cpy().sub(physical.position).limit(speed.speed)

          // Check if the projectile is close to their target
          if (targetPos.dst(physical.position) < 16.0f) {
            projectile.Host.eventHandler.addJob(RemoveEntity(projectile))
            projectile.Host.eventHandler.addJob(HurtEntity(target.get.target.copy(), projetileComponent.damage.toInt))

            // Apply status effect based on projectile type
            projetileComponent.projectile match {
              case "fire_projectile" =>   {entityPool.ecs.eventHandler.addJob(BurnEntity(target.get.target))}
              case "ice_projectile" =>    {entityPool.ecs.eventHandler.addJob(FreezeEntity(target.get.target))}
              case "poison_projectile" => {entityPool.ecs.eventHandler.addJob(PoisonEntity(target.get.target))}
              case _ => {}
            }
          }
        }
      }

    })
  }

  def onFrameForEntity(entity: Entity) = {}
}
