package TD.utils

import TD.EntityComponentSystem.{BurnStatusComponent, CombatComponent, ECS, EnemyComponent, EnemySpawnerComponent, Entity, EntityComponent, EntityPool, FreezeStatusComponent, PathingComponent, PhysicsComponent, PoisonStatusComponent, ProjectileComponent, SpeedComponent, Target, TargetingComponent, TowerComponent, VisualComponent}
import TD.GameObjects.{GameFloor, GameState}
import TD.TowerDefense
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.{Json, JsonReader, JsonValue}
import com.badlogic.gdx.utils.JsonWriter.OutputType

import scala.collection.mutable

/** Serializer
 *
 *  Helper object to read & write game data
 * */
object Serializer {

  /** Add specified component to an entity */
  def addComponent(entity: Entity, component: JsonValue, componentName: String) = {
    componentName match {
      case "EntityComponent" => {
        val entityId = component.getInt("entityId")
        val gen = component.getInt("generation")
        val isAlive = component.getBoolean("isAlive")

        val entityComponent = new EntityComponent(entityId, gen, isAlive)
        entity.addComponent(entityComponent)
      }
      case "PhysicsComponent"      => {
        val position = component.get("Position")
        val (posX, posY) = (position.getFloat("x"),position.getFloat("y"))

        val speed = component.get("Speed")
        val (spdX, spdY) = (speed.getFloat("x"),speed.getFloat("y"))

        val entityComponent = new PhysicsComponent(new Vector2(posX, posY), new Vector2(spdX, spdY))
        entity.addComponent(entityComponent)
      }
      case "ProjectileComponent"   => {
        val projectile = component.getString("projectile")
        val damage = component.getFloat("damage")
        entity.addComponent(new ProjectileComponent(projectile, damage))
      }
      case "PathingComponent"      => {
        val path = mutable.Buffer[Vector2]()
        component.get("path").forEach(value => {
          path += new Vector2(value.getFloat("x"), value.getFloat("y"))
        })
        val currentTarget = component.getInt("currentTarget")
        entity.addComponent(new PathingComponent(path.toArray, currentTarget))
      }
      case "FreezeStatusComponent" => {
        val slow = component.getFloat("slow")
        entity.addComponent(new FreezeStatusComponent(slow))
      }
      case "BurnStatusComponent" => {
        val damage = component.getFloat("damage")
        entity.addComponent(new BurnStatusComponent(damage))
      }
      case "PoisonStatusComponent" => {
        val damage = component.getFloat("damage")
        entity.addComponent(new PoisonStatusComponent(damage))
      }
      case "TargetingComponent"    => {
        val targetObject = component.get("target")
        val target = targetObject.getInt("target")
        val gen = targetObject.getInt("gen")
        entity.addComponent(new TargetingComponent(new Target(target, gen)))
      }
      case "TowerComponent"        => {
        val firerate = component.getFloat("firerate")
        val projectile = component.getString("projectile")
        val range = component.getFloat("range")

        entity.addComponent(new TowerComponent(firerate,0f, projectile, range))
      }
      case "VisualComponent"       => {
        val tex = component.getString("texture")
        val entityComponent = new VisualComponent(tex)
        entity.addComponent(entityComponent)
      }
      case "CombatComponent"       => {
        val health = component.getFloat("health")
        val entityComponent = new CombatComponent(health)
        entity.addComponent(entityComponent)
      }
      case "EnemyComponent" => {
        entity.addComponent(new EnemyComponent)
      }
      case "EnemySpawnerComponent" => {
        val spawnrate = component.getFloat("spawnrate")
        val spawn = component.getString("spawn")
        entity.addComponent(new EnemySpawnerComponent(spawnrate, 0, spawn))
      }
      case "SpeedComponent" => {
        val speed = component.getFloat("speed")
        entity.addComponent(new SpeedComponent(speed))
      }
      case _ => {}
    }
  }

  /** Entities */

    /** Serialize an entity to JSON */
  def entityToJson(entity: Entity) = {
    val json = new Json
    json.setUsePrototypes(false)
    json.setOutputType(OutputType.json)

    json.setTypeName("component")

    json.toJson(entity.getComponents)
  }

  /** Load components for an entity from json */
  def loadEntity(entity: Entity, entityJson: String)  = {
    val reader = new JsonReader
    val json   = reader.parse(entityJson)

    json.forEach(c => addComponent(entity, c, c.get("component").asString.replace("TD.EntityComponentSystem.", "")))
  }

  /** load components for an entity from entity templates (json) */
  def loadEntityTemplate(entity: Entity, entityJson: String) = {
    val file   = Gdx.files.local(entityJson)
    val reader = new JsonReader
    val json   = reader.parse(file)
    val comps  = json.get("Components")

    comps.forEach(component => addComponent(entity, component, component.name))
  }

  /** Write entities
   * @param ecs Entities to write
   * @param path path to write to
   * */
  def writeEntities(ecs: ECS, path: String) = {
    val file = Gdx.files.local(path)

    file.writeString("", false)

    ecs.pool.entities.foreach(entity => {
      file.writeString(entityToJson(entity) + "\n", true)
    })
  }

  /** Load entities
   *  @param ecs ECS to read entities to
   *  @param savefilePath path to read entities from
   * */
  def loadEntities(ecs: ECS, savefilePath: String) = {
    val savefile = Gdx.files.local(savefilePath)
    val save = savefile.readString

    save.split("\n").foreach(line => {
      if (!line.isEmpty) {
        val ent = new Entity(ecs)
        loadEntity(ent, line)
        ecs.addToPool(ent)
      }
    })
  }

  /** GAME CONFIG */

  /** Load game configurations from config.txt */
  def loadConfig(gameConfig: Config) = {

    val file  = Gdx.files.local("config.txt")
    val config = file.readString().split("\n")

    for (setting <- config) {
      val settingName = setting.split("=")(0)
      val settingVal  = setting.split("=")(1)

      gameConfig.confs(settingName) = settingVal.toFloat
    }
  }

  /** Write game's configurations in config.txt */
  def writeConfig(gameConfig: Config) = {
    val file = Gdx.files.local("config.txt")
    val configs = gameConfig.confs.map{ case (k,v) => s"${k}=${v}"}.mkString("\n")
    file.writeString(configs, false)
  }

  /** LEVEL */

    /** Load floor for level */
  def loadLevelFloor(game: TowerDefense, levelPath: String) = {
    val file   = Gdx.files.local(levelPath)
    val reader = new JsonReader
    val json   = reader.parse(file)

    val width  = json.getInt("width")
    val height = json.getInt("height")
    val path = json.get("path").asFloatArray().grouped(2).map(pair => new Vector2(pair(0), pair(1))).toArray
    val level = json.getString("floor")

    game.floor = new GameFloor(width, height, level, path)
  }

  /** FLOOR */

    /** Load floor */
  def loadFloor(game: TowerDefense, floorPath: String) = {
    val file   = Gdx.files.local(floorPath)
    val reader = new JsonReader
    val json   = reader.parse(file)

    val width  = json.getInt("width")
    val height = json.getInt("height")
    val path = mutable.Buffer[Vector2]()
      json.get("path").forEach(value => {
        path += new Vector2(value.getFloat("x"), value.getFloat("y"))
    })
    val level = json.getString("floor")

    game.floor = new GameFloor(width, height, level, path.toArray)
  }

  /** Write floor */
  def writeFloor(game: TowerDefense, path: String) = {
    val file   = Gdx.files.local(path)
    val json = new Json()
    json.setOutputType(OutputType.json)

    val floorJson = json.toJson(game.floor)
    file.writeString(floorJson, false)
  }

  /** GAME STATE */

    /** Load game state */
  def loadGameState(game: TowerDefense, path: String) = {
    val file   = Gdx.files.local(path)
    val reader = new JsonReader
    val json   = reader.parse(file)

    val hp = json.getFloat("playerHP")
    val money = json.getFloat("playerMoney")
    val gain = json.getFloat("moneyGain")
    val killReward = json.getFloat("killReward")
    val time = json.getFloat("timePlayed")
    val toughTime = json.getFloat("toughTime")

    game.gameState = new GameState(hp, money, gain, killReward, time, toughTime)
  }


  /** Write game state */
  def writeGameState(game: TowerDefense, path: String) = {
    val file   = Gdx.files.local(path)
    val json = new Json()

    val gameJson = json.toJson(game.gameState)
    file.writeString(gameJson, false)
  }

  /** LEVELS  */

    /** Load level */
  def loadLevel(game: TowerDefense, level: String) = {
    game.ecs = new ECS(game)

    this.loadLevelFloor(game, s"Levels/${level}/floor.json")
    this.loadGameState(game, s"Levels/${level}/gamestate.json")
    this.loadEntities(game.ecs, s"Levels/${level}/entities.json")
  }

  /** SAVE */

    /** Load save */
  def loadSave(game: TowerDefense, save: String) = {
    game.ecs = new ECS(game)

    this.loadFloor(game, s"${save}/floor.json")
    this.loadGameState(game, s"${save}/gamestate.json")
    this.loadEntities(game.ecs, s"${save}/entities.json")
  }

  /** Write save */
  def writeSave(game: TowerDefense) = {
    this.writeEntities(game.ecs, "Save/entities.json")
    this.writeFloor(game, "Save/floor.json")
    this.writeGameState(game, "Save/gamestate.json")
  }
}
