package TD.EntityComponentSystem.Systems

import TD.EntityComponentSystem.{EnemyComponent, Entity, EntityComponent, EntityPool, PathingComponent, PhysicsComponent, Target, TargetingComponent, TowerComponent}
import TD.utils.SpawnProjectile
import com.badlogic.gdx.Gdx.graphics

import scala.collection.mutable

/** Tower System
 *
 *  Selects targets for towers & spawns projectiles
 * */
object TowerSystem extends System {
  val classComponent = classOf[TowerComponent]

  override def onFrame(entityPool: EntityPool) = {
    entityPool.entities.filter(_.hasComponent(classOf[TowerComponent])).foreach(targeter => {
      val physical = targeter.getComponent(classOf[PhysicsComponent]).get
      val target = targeter.getComponent(classOf[TargetingComponent])
      val tower = targeter.getComponent(classOf[TowerComponent]).get


      if (!target.isDefined) {
        val entitiesNearby = findNearbyEnemies
        if (!entitiesNearby.isEmpty) {
          val targetEntity = findFurthestEnemy(findNearbyEnemies).getComponent(classOf[EntityComponent]).get
          targeter.addComponent(new TargetingComponent(new Target(targetEntity.entityId, targetEntity.generation)))
        }
      }

      /** Helper method to find enemies near a tower */
      def findNearbyEnemies = {
        entityPool.entities.filter(_.hasComponent(classOf[EnemyComponent])).filter(entity => {
          val entityPos = entity.getComponent(classOf[PhysicsComponent]).get.position.cpy()
          entityPos.dst(physical.position) < tower.range
        })
      }

      /** Find the enemy closest to finish */
      def findFurthestEnemy(enemies: mutable.Buffer[Entity]) = {
        enemies.groupBy(_.getComponent(classOf[PathingComponent]).get.currentTarget).maxBy(_._1)._2.minBy(ent => {
          val path = ent.getComponent(classOf[PathingComponent]).get
          val phys = ent.getComponent(classOf[PhysicsComponent]).get
          path.path(path.currentTarget).dst(phys.position)
        })

      }

    })

    TowerSystem.super.onFrame(entityPool)
  }

  def onFrameForEntity(ent: Entity) = {

    val target = ent.getComponent(classOf[TargetingComponent])

    // Check that tower has a target which is alive
    if (target.isDefined && ent.Host.pool.findEntity(target.get.target).isDefined) {
      val tower    = ent.getComponent(classOf[TowerComponent]).get
      val physical = ent.getComponent(classOf[PhysicsComponent]).get

      tower.lastFired += graphics.getDeltaTime

      if (tower.lastFired >= tower.firerate) {
        ent.Host.eventHandler.addJob(SpawnProjectile(
          physical.position.cpy(),
          target.get.target,tower.projectile))
        tower.lastFired = 0f
      }
    } else {
      //Remove target if it has been killed or removed
      ent.removeComponent(classOf[TargetingComponent])
    }
  }

}
