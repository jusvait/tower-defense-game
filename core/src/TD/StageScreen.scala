package TD

import TD.utils.Serializer
import com.badlogic.gdx.{Gdx, Screen}
import com.badlogic.gdx.graphics.{Color, GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.graphics.g2d.{BitmapFont, TextureRegion}
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.{InputEvent, InputListener, Stage}
import com.badlogic.gdx.scenes.scene2d.ui.{Label, Table, TextButton}
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ScreenViewport

/** Level selection screen */
class StageScreen(var game: TowerDefense) extends Screen {

  var camera: OrthographicCamera = new OrthographicCamera
  camera.setToOrtho(false, 960, 540)

  var background = new Texture("Textures/menu.png")

  /** USER INTERFACE*/
  var stage = new Stage(new ScreenViewport())
  Gdx.input.setInputProcessor(stage)

  var table = new Table
  table.setFillParent(true)
  stage.addActor(table)
  table.setPosition(0, 40)

  /** Widgets */

  var buttonFont = new BitmapFont()
  var labelStyle  = new LabelStyle(buttonFont, new Color(255,255,255,255))
  var stageLabel = new Label("Select Stage", labelStyle)
  stageLabel.setScale(2.0f)

  table.add(stageLabel).padBottom(20)
  table.row()

  var playButtonUp = new Texture(Gdx.files.internal("Textures/forestbutton.png"))
  var playButtonDown = new Texture(Gdx.files.internal("Textures/forestbutton.png"))

  var upRegion   = new TextureRegion(playButtonUp,128, 64)
  var downRegion = new TextureRegion(playButtonDown, 128, 64)
  var playButtonStyle = new TextButtonStyle()
  playButtonStyle.up  = new TextureRegionDrawable(upRegion)
  playButtonStyle.down = new TextureRegionDrawable(downRegion)
  playButtonStyle.font = buttonFont

  var forestButton = new TextButton("Forest", playButtonStyle)

  // Listen for clicks
  forestButton.addListener(new InputListener(){
    override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) = {
      Serializer.loadLevel(game, "Level_01")
      Serializer.writeSave(game)
      game.setScreen(new GameScreen(game))
      dispose()
      true
    }
  })

  table.add(forestButton).padBottom(10)
  table.row()

  var backButtonUp    = new Texture(Gdx.files.internal("Textures/button_1.png"))
  var backButtonDown  = new Texture(Gdx.files.internal("Textures/button_1_down.png"))
  var backButtonStyle = new TextButtonStyle()
  var backUpRegion    = new TextureRegion(backButtonUp,128, 64)
  var backDownRegion  = new TextureRegion(backButtonDown, 128, 64)

  backButtonStyle.up   = new TextureRegionDrawable(backUpRegion)
  backButtonStyle.down = new TextureRegionDrawable(backDownRegion)
  backButtonStyle.font = buttonFont

  var backButton = new TextButton("Back", backButtonStyle)

  backButton.addListener(new InputListener(){
    override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) = {
      game.setScreen(new PlayScreen(game))
      dispose()
      true
    }
  })

  table.row()
  table.add(backButton)


  override def show(): Unit = {}

  override def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    camera.update
    game.batch.setProjectionMatrix(camera.combined)

    game.batch.begin
    game.batch.draw(background, 0, 0)
    game.batch.end()

    stage.draw()
  }

  override def resize(width: Int, height: Int): Unit = {}

  override def pause(): Unit = {}

  override def resume(): Unit = {}

  override def hide(): Unit = {}

  override def dispose(): Unit = {
    background.dispose()
    stage.dispose()
  }
}
