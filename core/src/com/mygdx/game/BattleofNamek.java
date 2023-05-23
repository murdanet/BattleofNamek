package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class BattleofNamek extends Game {
	SpriteBatch batch;
	private TextureRegion namekRegion;

	private Animation<TextureRegion> knightAnimation;
	private float knightStateTime;
	private float knightX;
	private float knightY;
	private float knightSpeed = 200f; // Velocidad de movimiento del caballero
	private boolean isKnightMoving = false;
	private float targetX;

	private Animation<TextureRegion> titanAnimation;
	private float titanStateTime;
	private float titanX;
	private float titanY;
	private float titanSpeed = 150f; // Velocidad de movimiento del titán
	private float currentTitanSpeed = titanSpeed; // Velocidad actual del titán
	private float timeSinceCollision = 0f; // Tiempo transcurrido desde la última colisión

	private static final int FRAME_COLS = 4;
	private static final int FRAME_ROWS = 4;
	private static final float SPRITE_SCALE = 5f;

	private Rectangle knightRectangle;
	private Rectangle titanRectangle;

	private Sound collisionSound;
	private boolean isCollisionSoundPlaying = false;

	private Texture healthBarTexture;
	private TextureRegion[] healthBarFrames;
	private Animation<TextureRegion> healthBarAnimation;
	private float healthBarStateTime;

	private static final int HEALTH_BAR_COLS = 2;
	private static final int HEALTH_BAR_ROWS = 5;
	private static final float HEALTH_BAR_FRAME_DURATION = 0.1f;
	private static final float HEALTH_BAR_SCALE = 0.5f;
	private boolean collisionOccurred = false;

	private Music backgroundMusic;

	private boolean isGameOver = false;
	BitmapFont font; // Puedes cargar una fuente para mostrar el texto de "Game Over"

	public BattleofNamek() {
	}

	@Override
	public void create() {




		batch = new SpriteBatch();
		// Cargar el atlas y obtener la región de la imagen de Namek
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("battleofnamek.atlas"));
		namekRegion = atlas.findRegion("namek");

		// Cargar los frames del caballero y crear la animación
		TextureRegion[][] knightFrames = atlas.findRegion("knight").split(
				atlas.findRegion("knight").getRegionWidth() / FRAME_COLS,
				atlas.findRegion("knight").getRegionHeight() / FRAME_ROWS
		);
		TextureRegion[] knightAnimationFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				knightAnimationFrames[index++] = knightFrames[i][j];
			}
		}
		knightAnimation = new Animation<>(0.1f, knightAnimationFrames);
		knightStateTime = 0f;
		knightX = 100;
		knightY = 100;
		knightRectangle = new Rectangle(knightX, knightY, knightAnimationFrames[0].getRegionWidth() * SPRITE_SCALE, knightAnimationFrames[0].getRegionHeight() * SPRITE_SCALE);

		// Cargar los frames del titán y crear la animación
		TextureRegion[][] titanFrames = atlas.findRegion("caveman").split(
				atlas.findRegion("caveman").getRegionWidth() / FRAME_COLS,
				atlas.findRegion("caveman").getRegionHeight() / FRAME_ROWS
		);
		TextureRegion[] titanAnimationFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				titanAnimationFrames[index++] = titanFrames[i][j];
			}
		}
		titanAnimation = new Animation<>(0.1f, titanAnimationFrames);
		titanStateTime = 0f;
		titanX = 1200;
		titanY = 100;
		titanRectangle = new Rectangle(titanX, titanY, titanAnimationFrames[0].getRegionWidth() * SPRITE_SCALE, titanAnimationFrames[0].getRegionHeight() * SPRITE_SCALE);

		collisionSound = Gdx.audio.newSound(Gdx.files.internal("die.mp3"));

		healthBarTexture = new Texture("barra_vida.png");

		TextureRegion[][] healthBarFrames = TextureRegion.split(
				healthBarTexture,
				healthBarTexture.getWidth() / HEALTH_BAR_COLS,
				healthBarTexture.getHeight() / HEALTH_BAR_ROWS
		);

		TextureRegion[] healthBarAnimationFrames = new TextureRegion[HEALTH_BAR_COLS * HEALTH_BAR_ROWS];
		int indexheart = 0;
		for (int row = 0; row < HEALTH_BAR_ROWS; row++) {
			for (int col = 0; col < HEALTH_BAR_COLS; col++) {
				healthBarAnimationFrames[indexheart++] = healthBarFrames[row][col];
			}
		}

		healthBarAnimation = new Animation<>(HEALTH_BAR_FRAME_DURATION, healthBarAnimationFrames);
		healthBarStateTime = 0f;

		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("boofury.mp3"));
		backgroundMusic.setVolume(0.5f);
		backgroundMusic.setLooping(true);
		backgroundMusic.play();

		this.setScreen(new MainMenuScreen(this));
		MainMenuScreen mainMenuScreen = new MainMenuScreen(this);
		setScreen(mainMenuScreen);

	}

	public void dispose() {
		batch.dispose();
		collisionSound.dispose();
		backgroundMusic.dispose();
	}

}
