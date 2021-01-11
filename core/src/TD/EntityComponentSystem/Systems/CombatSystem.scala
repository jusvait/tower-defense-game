package TD.EntityComponentSystem.Systems

import TD.EntityComponentSystem.{BurnStatusComponent, CombatComponent, Entity, PoisonStatusComponent}
import TD.utils.{RemoveEntity, RewardPlayer}

/** Combat system
 *
 *  Damages entities with damage-over-time-effects and removes them if they are dead
 * */
object CombatSystem extends System {
  val classComponent = classOf[CombatComponent]

  def onFrameForEntity(ent: Entity) = {
    // Get components
    val combat = ent.getComponent(classOf[CombatComponent]).get
    val poison = ent.getComponent(classOf[PoisonStatusComponent])
    val burn = ent.getComponent(classOf[BurnStatusComponent])

    // Apply poison damage
    if (poison.isDefined) {
      combat.health -= poison.get.damage
    }

    // Apply burn damage
    if (burn.isDefined) {
      combat.health -= burn.get.damage
    }

    // Kill entity if their health is below zero
    if (combat.health < 0) {
      ent.Host.eventHandler.addJob(RewardPlayer(10))
      ent.Host.eventHandler.addJob(RemoveEntity(ent))
    }

  }
}
