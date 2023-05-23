package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class GameScreen implements Screen {

    OrthographicCamera camera;
    private final BattleofNamek game;
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
    private int titanHealth = 10; // Vida inicial del titán

    private boolean collisionOccurred = false;

    private Music backgroundMusic;

    private boolean isGameOver = false;


    public GameScreen(BattleofNamek game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,400);
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


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(namekRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        knightStateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentKnightFrame = knightAnimation.getKeyFrame(knightStateTime, true);
        float knightWidth = currentKnightFrame.getRegionWidth() * SPRITE_SCALE;
        float knightHeight = currentKnightFrame.getRegionHeight() * SPRITE_SCALE;
        game.batch.draw(currentKnightFrame, knightX, knightY, knightWidth, knightHeight);
        knightRectangle.set(knightX, knightY, knightWidth, knightHeight);

        titanStateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentTitanFrame = titanAnimation.getKeyFrame(titanStateTime, true);
        float titanWidth = currentTitanFrame.getRegionWidth() * SPRITE_SCALE;
        float titanHeight = currentTitanFrame.getRegionHeight() * SPRITE_SCALE;
       game.batch.draw(currentTitanFrame, titanX, titanY, titanWidth, titanHeight);
        titanRectangle.set(titanX, titanY, titanWidth, titanHeight);

        TextureRegion currentHealthBarFrame;

        if (collisionOccurred) {
            healthBarStateTime += Gdx.graphics.getDeltaTime() * 0.1f;
            currentHealthBarFrame = healthBarAnimation.getKeyFrame(healthBarStateTime, true);
            if (healthBarAnimation.getKeyFrameIndex(healthBarStateTime) >= 10) {
                healthBarStateTime = healthBarAnimation.getAnimationDuration();

            }
        } else {
            healthBarStateTime = 0f;
            currentHealthBarFrame = healthBarAnimation.getKeyFrame(healthBarStateTime);
        }

        float healthBarWidth = Gdx.graphics.getWidth();
        float healthBarHeight = currentHealthBarFrame.getRegionHeight() * HEALTH_BAR_SCALE;
        float healthBarX = 0;
        float healthBarY = Gdx.graphics.getHeight() - healthBarHeight;
        game.batch.draw(currentHealthBarFrame, healthBarX, healthBarY, healthBarWidth, healthBarHeight);
        if (healthBarAnimation.getKeyFrameIndex(healthBarStateTime) >= 10) {
            isGameOver = true;
        }
        game.batch.end();
//Movimiento del caballero
        if (Gdx.input.isTouched()) {
            int touchX = Gdx.input.getX();
            targetX = touchX - knightWidth / 2;
            isKnightMoving = true;
        }

        if (isKnightMoving) {
            if (Math.abs(knightX - targetX) > knightSpeed * Gdx.graphics.getDeltaTime()) {
                float directionX = (targetX - knightX) / Math.abs(targetX - knightX);
                knightX += directionX * knightSpeed * Gdx.graphics.getDeltaTime();
            } else {
                knightX = targetX;
                isKnightMoving = false;
            }
        }

        titanX += titanSpeed * Gdx.graphics.getDeltaTime();

        if (titanX < 0 || titanX + titanWidth > Gdx.graphics.getWidth()) {
            titanSpeed = -titanSpeed;
        }
//Colisiones
        timeSinceCollision += Gdx.graphics.getDeltaTime();
        if (knightRectangle.overlaps(titanRectangle)) {
            if (!isCollisionSoundPlaying) {
                collisionSound.play();
                isCollisionSoundPlaying = true;
                collisionOccurred = true;
                currentTitanSpeed += currentTitanSpeed * 0.005f;
                timeSinceCollision = 0f;
                collisionSound.loop();
                if (!isCollisionSoundPlaying) {
                    collisionSound.play();
                    isCollisionSoundPlaying = true;
                    collisionOccurred = true;

                    titanHealth--; // Reduce la vida del titán

                    if (titanHealth <= 0) {
                        // Si la vida del titán es igual o menor a 0, el juego termina
                        game.setScreen(new GameOverScreen(game));
                    }

                    currentTitanSpeed += currentTitanSpeed * 0.005f;
                    timeSinceCollision = 0f;
                    collisionSound.loop();
                }
            }
        } else {
            isCollisionSoundPlaying = false;
            collisionOccurred = false;
            currentTitanSpeed -= currentTitanSpeed * 0.005f;
            collisionSound.stop();
            if (currentTitanSpeed < titanSpeed) {
                currentTitanSpeed = titanSpeed;
            }
        }
    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
