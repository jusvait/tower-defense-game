package TD

import TD.EntityComponentSystem.{ECS, PhysicsComponent, ProjectileComponent, TargetingComponent, TowerComponent, VisualComponent}
import TD.GameObjects.Shop.{Item, Purchaseable, Shop, Tower}
import TD.GameObjects.{GameFloor, GameState}
import TD.utils.{Config, Serializer}
import com.badlogic.gdx.{Game, Gdx}
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Animation, BitmapFont, SpriteBatch, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array

/** The game class used for Tower Defense */

class TowerDefense extends Game {

  // Initialize variables, they are set to null here because LWJGL Application is needed first
  var config:    Config      = null
  var batch:     SpriteBatch = null
  var font:      BitmapFont  = null
  var ecs:       ECS         = null
  var floor:     GameFloor   = null
  var cursorInventory: Option[Purchaseable] = None

  // Set when loading a level/save
  var gameState: GameState   = null
  var shop:      Shop        = null

  // LWJGL Application is up, values can now be set
  def create() {
    config    = new Config
    batch     = new SpriteBatch
    font      = new BitmapFont
    ecs       = new ECS(this)
    shop      = new Shop

    this.setScreen(new MainMenuScreen(this))
  }

  // Place a purchased object on the game floor
  def placeObject(obj: Purchaseable, x: Float, y: Float) = obj match {
    case tower: Tower => placeTower(tower, x, y)
    case  item: Item  => placeItem(item, x, y)
  }

  def placeTower(tower: Tower, x: Float , y: Float)  = {
    val ent = ecs.newEntity(s"Entities/Towers/tower_${tower.towerType}.json")

    ent.addComponent(new PhysicsComponent(new Vector2(x, y), new Vector2(0,0)))
  }

  // TODO: Add method to place items
  def placeItem(item: Item, x: Float, y: Float) = { }



  // Load & Write game configuration, used in settings screen
  def loadConfig()  = Serializer.loadConfig(config)
  def writeConfig() = Serializer.writeConfig(config)

  override def render() = super.render()

  // Dispose libgdx objects when killing game
  override def dispose(): Unit = {
    batch.dispose()
    font.dispose()
  }

}
