package TD.utils

import com.badlogic.gdx.math.Vector2

import scala.collection.mutable
import TD.EntityComponentSystem.{BurnStatusComponent, CombatComponent, ECS, Entity, FreezeStatusComponent, PathingComponent, PhysicsComponent, PoisonStatusComponent, ProjectileComponent, Target, TargetingComponent, VisualComponent}
import TD.TowerDefense

/** ECS Event Handler
 *
 *  Handles events created by systems
 * */
class ECSEventHandler(val game: TowerDefense) {
  val jobs = mutable.Buffer[Job]()

  def addJob(job: Job) = this.jobs += job

  /** Go through all current jobs */
  def work() = {
    for (i <- 0 until this.jobs.length) {
      jobs(0) match {
        case RewardPlayer(money) => {
          this.game.gameState.playerMoney += money
        }
        case SpawnProjectile(pos, target, proj) => {
          val ent = game.ecs.newEntity(s"Entities/Projectiles/${proj}_projectile.json")
          ent.addComponent( new TargetingComponent(target.copy()))
          ent.addComponent( new PhysicsComponent(pos, new Vector2(0,0)))
        }
        case RemoveEntity(ent) => {
          game.ecs.pool.removeEntity(ent)
        }
        case EntityHasReachedGoal(ent) => {
          game.gameState.playerHP -= 10
          game.ecs.pool.removeEntity(ent)
        }
        case SpawnEnemy(enemyJsonPath, pos, path) => {
          val ent = game.ecs.newEntity(enemyJsonPath)
          ent.addComponent(new PhysicsComponent(pos, new Vector2(0,0)))
          ent.addComponent(new PathingComponent(path, 0))
        }
        case HurtEntity(target: Target, damage: Int) => {
          val entity = game.ecs.pool.findEntity(target)

          if (entity.isDefined && entity.get.hasComponent(classOf[CombatComponent])) {
            entity.get.getComponent(classOf[CombatComponent]).get.health -= damage
          }
        }
        case BurnEntity(target: Target) => {
          val entity = game.ecs.pool.findEntity(target)

          if (entity.isDefined && entity.get.hasComponent(classOf[CombatComponent])) {
            entity.get.addComponent(new BurnStatusComponent(0.1f))
          }
        }
        case FreezeEntity(target: Target) => {
          val entity = game.ecs.pool.findEntity(target)

          if (entity.isDefined && entity.get.hasComponent(classOf[CombatComponent])) {
            entity.get.addComponent(new FreezeStatusComponent(0.3f))
          }
        }
        case PoisonEntity(target: Target) => {
          val entity = game.ecs.pool.findEntity(target)

          if (entity.isDefined && entity.get.hasComponent(classOf[CombatComponent])) {
            entity.get.addComponent(new PoisonStatusComponent(0.2f))
          }
        }
        case _ => {}
      }

      jobs.remove(0)
    }
  }
}

abstract class Job

case class SpawnProjectile(pos: Vector2, target: Target, proj: String) extends Job
case class RemoveEntity(entity: Entity) extends Job
case class EntityHasReachedGoal(entity: Entity) extends Job
case class SpawnEnemy(enemyJsonPath: String, pos: Vector2, path: Array[Vector2]) extends Job
case class RewardPlayer(money: Float) extends Job

/** Hurting entities */
case class HurtEntity(target: Target, damage: Int) extends Job
case class BurnEntity(target: Target) extends Job
case class FreezeEntity(target: Target) extends Job
case class PoisonEntity(target: Target) extends Job

