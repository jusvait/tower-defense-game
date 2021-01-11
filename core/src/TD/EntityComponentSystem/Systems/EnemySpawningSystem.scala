package TD.EntityComponentSystem.Systems
import TD.EntityComponentSystem.{Composable, EnemySpawnerComponent, Entity, PhysicsComponent}
import TD.utils.SpawnEnemy
import com.badlogic.gdx.Gdx.graphics


/** Enemy spawning system
 *
 *  Spawns enemies for entities which have a enemy spawner component
 *  Spawn frequency increases over time, as defined by the 'toughTime' variable with gameState.
 * */
object EnemySpawningSystem extends System {
  override val classComponent = classOf[EnemySpawnerComponent]

  override def onFrameForEntity(entity: Entity) = {
    // Get components
    val toughTime  = entity.Host.game.gameState.toughTime
    val timePlayed = entity.Host.game.gameState.timePlayed

    val spawner = entity.getComponent(classComponent).get
    val path = entity.Host.game.floor.path

    // Update timer on last spawn
    spawner.lastSpawned += graphics.getDeltaTime

    if (spawner.lastSpawned >= spawner.spawnrate * (toughTime / (toughTime + timePlayed))) {spawnEnemy()}

    // Helper function to spawn an enemy
    def spawnEnemy() = {
      val pos = entity.getComponent(classOf[PhysicsComponent]).get.position.cpy()
      entity.Host.eventHandler.addJob(SpawnEnemy(s"Entities/Enemies/${spawner.spawn}.json", pos, path))
      spawner.lastSpawned = 0f
    }
  }
}
