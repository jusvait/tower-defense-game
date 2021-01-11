package TD

import TD.GameObjects.Shop.{BoomTower, FireTower, IceTower, Item, PoisonTower, ShopButton, Tower}
import TD.GameObjects._
import TD.utils.{Debug, Serializer}
import com.badlogic.gdx.{Gdx, Screen}
import com.badlogic.gdx.graphics.{Color, GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch, TextureRegion}
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.{InputEvent, InputListener, Stage}
import com.badlogic.gdx.scenes.scene2d.ui.{Label, Table, TextButton}
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.StretchViewport

class GameScreen(var game: TowerDefense) extends Screen {

  Serializer.writeSave(game)

  /** USER INTERFACE*/

  var stage = new Stage(new StretchViewport(240, 128))
  var loseStage = new Stage(new StretchViewport(CONSTANTS.SCREEN_WIDTH, CONSTANTS.SCREEN_HEIGHT))

  Gdx.input.setInputProcessor(stage)

  var table = new Table 
  table.setFillParent(true)
  stage.addActor(table)
  table.bottom.left

  var statusTable = new Table
  stage.addActor(statusTable)

  /** Widgets */

  var buttonFont = new BitmapFont()

  val fireTowerButton   = new ShopButton(new FireTower, game.shop, game, "Hud/fire_tower_icon.png")
  val iceTowerButton    = new ShopButton(new IceTower, game.shop, game, "Hud/ice_tower_icon.png")
  val poisonTowerButton = new ShopButton(new PoisonTower, game.shop, game, "Hud/poison_tower_icon.png")
  val boomTowerButton   = new ShopButton(new BoomTower, game.shop, game, "Hud/boom_tower_icon.png")

  table.add(fireTowerButton)
  table.add(iceTowerButton)
  table.add(poisonTowerButton)
  table.add(boomTowerButton)

  var labelStyle  = new LabelStyle(buttonFont, new Color(255,255,255,255))
  var healthLabel = new Label(s"Health: ${game.gameState.playerHP}", labelStyle)
  var moneyLabel  = new Label(s"Money:  ${game.gameState.playerHP}", labelStyle)

  healthLabel.setFontScale(0.5f)
  moneyLabel.setFontScale(0.5f)

  statusTable.bottom().padLeft(400).padBottom(4)

  statusTable.add(healthLabel)
  statusTable.row()
  statusTable.add(moneyLabel)

  /** LOSING STAGE */
  var loseTable = new Table
  loseTable.setFillParent(true)
  loseStage.addActor(loseTable)
  loseTable.center()

  var playButtonUp = new Texture(Gdx.files.internal("Textures/button_1.png"))
  var playButtonDown = new Texture(Gdx.files.internal("Textures/button_1_down.png"))

  var upRegion   = new TextureRegion(playButtonUp,128, 64)
  var downRegion = new TextureRegion(playButtonDown, 128, 64)
  var playButtonStyle = new TextButtonStyle()
  playButtonStyle.up  = new TextureRegionDrawable(upRegion)
  playButtonStyle.down = new TextureRegionDrawable(downRegion)
  playButtonStyle.font = buttonFont

  var playButton = new TextButton("You've lost! Play again?", playButtonStyle)

  // Listen for clicks
  playButton.addListener(new InputListener(){
    override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) = {
      game.setScreen(new PlayScreen(game))
      dispose()
      true
    }
  })

  loseTable.add(playButton)

  var camera: OrthographicCamera = new OrthographicCamera
  camera.setToOrtho(false, CONSTANTS.SCREEN_WIDTH, CONSTANTS.SCREEN_HEIGHT)

  var batch = new SpriteBatch
  var backgroundMusic = Gdx.audio.newSound(Gdx.files.internal("Audio/gameplay.mp3"))

  val LRTex = new Texture(Gdx.files.internal("Textures/LR.png"))
  val DRTex = new Texture(Gdx.files.internal("Textures/DR.png"))
  val LDTex = new Texture(Gdx.files.internal("Textures/LD.png"))
  val LUTex = new Texture(Gdx.files.internal("Textures/LU.png"))
  val UDTex = new Texture(Gdx.files.internal("Textures/UD.png"))
  val URTex = new Texture(Gdx.files.internal("Textures/UR.png"))
  var freeTex = new Texture(Gdx.files.internal("Textures/grass.png"))
  var tileCursorTex = new Texture(Gdx.files.internal("Textures/tileCursor.png"))
  
  var hudBarTex = new Texture(Gdx.files.internal("Textures/HUD_bar.png"))

  var spriteBatch = new SpriteBatch()

  override def show(): Unit = {
    game.loadConfig()
    backgroundMusic.loop(game.config.confs("musicVolume"))
  }

  override def render(delta: Float) = {
    Gdx.gl.glClearColor(0,0,0,1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    camera.update
    game.batch.setProjectionMatrix(camera.combined)
    spriteBatch.setProjectionMatrix(camera.combined)
    spriteBatch.begin

    /** Draw tiles */

    for {
      x <- 0 until game.floor.width
      y <- 0 until game.floor.height
    } {

      game.floor.tiles(x)(y).tileType match {
        case "F"  =>  {spriteBatch.draw(freeTex, x*32, y*32 + CONSTANTS.HUD_HEIGHT)}
        case "T" =>  spriteBatch.draw(freeTex ,x*32, y*32 + CONSTANTS.HUD_HEIGHT)
        case "LR" => spriteBatch.draw(LRTex ,x*32, y*32 + CONSTANTS.HUD_HEIGHT)
        case "UD" => spriteBatch.draw(UDTex ,x*32, y*32 + CONSTANTS.HUD_HEIGHT)
        case "DR" => spriteBatch.draw(DRTex ,x*32, y*32 + CONSTANTS.HUD_HEIGHT)
        case "LU" => spriteBatch.draw(LUTex ,x*32, y*32 + CONSTANTS.HUD_HEIGHT)
        case "UR" => spriteBatch.draw(URTex ,x*32, y*32 + CONSTANTS.HUD_HEIGHT)
        case "LD" => spriteBatch.draw(LDTex ,x*32, y*32 + CONSTANTS.HUD_HEIGHT)
        case _ => {}
      }
    }

    spriteBatch.draw(hudBarTex, 0, 0)


    /** Tile cursor */
    val tileCursor = Cursor.hoverTileCoordinates
    val tile = Cursor.hoverTile(this.game.floor)
    if (tileCursor.isDefined && tile.isDefined) {
      // Get current tile cursor
      val (tileCursorX, tileCursorY) = tileCursor.get
      if (Gdx.input.isTouched() && Cursor.hasItem && tile.get.tileType=="F") {
        tile.get.changeTileType("T")
        game.placeObject(Cursor.getItem, tileCursorX.toFloat*32, tileCursorY.toFloat*32 + CONSTANTS.HUD_HEIGHT)
        Cursor.removeItem()

      }
      spriteBatch.draw(tileCursorTex, tileCursorX*32, tileCursorY*32 + CONSTANTS.HUD_HEIGHT)
    }

    spriteBatch.end

    /** Run systems */
    if (game.gameState.alive) {
      this.game.ecs.update(game, camera)
      game.gameState.update()
      stage.draw()
    } else {
      Serializer.writeSave(game)
      Gdx.input.setInputProcessor(loseStage)
      table.reset()
      loseStage.draw()
    }

    healthLabel.setText(s"Health:\t${game.gameState.playerHP}")
    moneyLabel.setText(s"Money:\t${game.gameState.playerMoney.round}")
  }

  override def pause(): Unit = {}

  override def resume(): Unit = {}

  override def hide(): Unit = {
    game.writeConfig()
  }

  override def dispose(): Unit = {
    batch.dispose()
    backgroundMusic.dispose()

    freeTex.dispose()
    LRTex.dispose()
    LDTex.dispose()
    DRTex.dispose()
    LUTex.dispose()
    UDTex.dispose()
    URTex.dispose()

    hudBarTex.dispose()
    stage.dispose()
    loseStage.dispose()
  }

  override def resize(width: Int, height: Int) = {}
}
