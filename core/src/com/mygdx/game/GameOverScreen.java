package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {
    private final BattleofNamek game;
    SpriteBatch batch;
    OrthographicCamera camera;
    Texture fondo;
    public GameOverScreen(BattleofNamek game) {
        this.game = game;
        this.batch=batch;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,400);
        fondo=new Texture("gameover.jpg");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 2, 0, 1);
        game.batch.begin();
        game.batch.draw(fondo, 0, 0);
        game.batch.end();

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
