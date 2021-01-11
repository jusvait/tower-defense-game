package TD.EntityComponentSystem

import TD.EntityComponentSystem.Systems.{CombatSystem, EnemySpawningSystem, PathingSystem, PhysicsSystem, ProjectileSystem, TowerSystem, VisualSystem}
import TD.TowerDefense
import TD.utils.{ECSEventHandler, Serializer}
import com.badlogic.gdx.graphics.OrthographicCamera

/** ECS - Entity Component Systems
 *
 * @param game game bound to this ECS instance
 * */

class ECS(val game: TowerDefense) {
  val pool = new EntityPool(this)
  val eventHandler = new ECSEventHandler(this.game)
  var lastSaved = 0

  /** Add an entity to the pool */
  def addToPool(entity: Entity) = this.pool.entities += entity

  /** Create new entity */
  def newEntity(jsonPath: String) = {
    val newEnt = this.pool.entities.find(!_.getComponent(classOf[EntityComponent]).get.isAlive) match {
      // There is a free entity in the pool
      case Some(entity: Entity) => {
        val entityComponent =  entity.getComponent(classOf[EntityComponent]).get
        entityComponent.isAlive = true
        entityComponent.generation += 1
        entity
      }
      // There is no free entity in the pool
      case None => {
        val entity = new Entity(this)
        entity.addComponent(new EntityComponent(this.largestEntityID+1, 0, true))
        addToPool(entity)
        entity
      }
    }

    Serializer.loadEntityTemplate(newEnt, jsonPath)
    newEnt
  }

  /** Helper method to find the biggest entity ID */
  def largestEntityID = {
    if (this.pool.entities.isEmpty) {-1} else {this.pool.entities.map(_.getComponent(classOf[EntityComponent]).get.entityId).max}
  }

  /** Run systems */
  def update(game: TowerDefense, camera: OrthographicCamera) = {
    this.eventHandler.work()

    EnemySpawningSystem.onFrame(this.pool)
    PathingSystem.onFrame(this.pool)
    TowerSystem.onFrame(this.pool)
    ProjectileSystem.onFrame(this.pool)
    CombatSystem.onFrame(this.pool)
    PhysicsSystem.onFrame(this.pool)

    VisualSystem.draw(game, camera)

    /** Save every 30 updates/frames */

    if (lastSaved > 30) {
      lastSaved = 0
      Serializer.writeSave(this.game)
    }

    lastSaved += 1
  }
}
