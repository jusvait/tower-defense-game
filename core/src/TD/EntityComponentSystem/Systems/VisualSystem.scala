package TD.EntityComponentSystem.Systems

import TD.EntityComponentSystem.{ Entity,PhysicsComponent, VisualComponent}
import TD.TowerDefense
import com.badlogic.gdx.graphics.{ OrthographicCamera, Texture}
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** Visual System
 *
 *  Renders entities with a visual component
 * */
object VisualSystem extends System {
  val classComponent = classOf[VisualComponent]

  def draw(game: TowerDefense, camera: OrthographicCamera) = {
    val spritebatch = new SpriteBatch()

    game.batch.setProjectionMatrix(camera.combined)
    spritebatch.setProjectionMatrix(camera.combined)

    spritebatch.begin()

    // Group entities by their texture
    val drawable = game.ecs.pool.getEntitiesWithComponent(classComponent)
      .groupBy(_.getComponent(classComponent).get.texture)

    // Draw entities for each texture
    drawable.keys.foreach(texturePath => {
      val texture = new Texture(texturePath)

      drawable(texturePath).foreach(entity => onFrameForEntity(entity, spritebatch, texture))

    })

    spritebatch.end()

  }

  def onFrameForEntity(entity: Entity, sb: SpriteBatch, texture: Texture) = {
    val pos = entity.getComponent(classOf[PhysicsComponent]).get.position

    sb.draw(texture, pos.x, pos.y)
  }

  override def onFrameForEntity(entity: Entity): Unit = {}
}
