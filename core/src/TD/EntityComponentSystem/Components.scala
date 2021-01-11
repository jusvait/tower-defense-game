package TD.EntityComponentSystem

import com.badlogic.gdx.math.Vector2

/** The composable trait defines components for entities */
trait Composable {}

/** All entities have an entity component */
class EntityComponent(var entityId: Int, var generation: Int, var isAlive: Boolean) extends Composable {

  /** Increment entity generation by one */
  def increaseGeneration() = this.generation += 1

  /** Kill entity */
  def kill() = this.isAlive = false

  /** Reset the entity */
  def reset() = {
    this.increaseGeneration()
    this.kill()
  }
}

/** Physical properties */
class PhysicsComponent(var position: Vector2, var speed: Vector2) extends Composable

/** Projectile properties */
class ProjectileComponent(val projectile: String, val damage: Float) extends Composable

/** Pathing component for enemies */
class PathingComponent( val path: Array[Vector2], var currentTarget: Int ) extends Composable

/** Status Components */
class FreezeStatusComponent (val slow: Float)  extends Composable
class BurnStatusComponent   (val damage: Float)  extends Composable
class PoisonStatusComponent (val damage: Float)  extends Composable

class TargetingComponent(var target: Target) extends Composable
/** Helper class for TargetingComponent */
class Target(val target: Int, val gen: Int) {
  def copy() = {
    new Target(this.target, this.gen)
  }
}

/** Enemy component */
class EnemyComponent() extends Composable

/** Enemy spawner component
 *
 * @param spawnrate How often enemies spawn
 * @param lastSpawned Last time an enemy was spawned
 * @param spawn what enemy is spawned
 * */
class EnemySpawnerComponent(var spawnrate: Float, var lastSpawned: Float, val spawn: String) extends Composable

/** Speed component
 *
 * @param speed Fastest speed obtainable by entity
 * */
class SpeedComponent(val speed: Float) extends Composable

/** Tower component
 * @param firerate How quickly can the tower shoot
 * @param lastFired How many seconds ago did the tower shoot */
class TowerComponent(val firerate: Float, var lastFired: Float, var projectile: String, var range: Float) extends Composable

/** Visual component
 *
 * @param texture texture path for entity
 * */
class VisualComponent(val texture: String) extends Composable

/** Combat component
 *
 * @param health entity's health
 * */
class CombatComponent(var health: Float) extends Composable {
  def alive = this.health > 0f
}