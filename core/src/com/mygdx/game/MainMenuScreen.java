package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    private final BattleofNamek game;
    SpriteBatch batch;
    OrthographicCamera camera;

    Texture background;
    Texture title;

    public MainMenuScreen(BattleofNamek game) {
        this.game = game;
        this.batch=batch;

        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,400);
        background=new Texture("namek_background.jpeg");
        title= new Texture("fonttitle.png");



    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        game.batch.draw(title,50,30,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game));
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
