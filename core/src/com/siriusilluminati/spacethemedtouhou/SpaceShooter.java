package com.siriusilluminati.spacethemedtouhou;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Iterator;
import java.util.Objects;

public class SpaceShooter extends ApplicationAdapter {
	private String directionMS;
	int scrollSpeed;
	int scrollDist;
	int scrollDist2;
	int focusMovementReduction;
	int shotDelay;
	int score;
	SpriteBatch batch;
	Texture bgImg1;
	Texture bgImg2;
	Texture mothershipImg;
	Texture shipImg;
	Texture rayImg;
	BitmapFont font;
	Texture rocketImg;
	Rectangle ship;
	Rectangle mothershipEntity;
	Array<Rectangle> rockets;
	Array<Rectangle> rayProjectiles;
	OrthographicCamera screen;
	
	@Override
	public void create () {
		scrollSpeed = 2;
		scrollDist = 0;
		scrollDist2 = 1920;
		screen = new OrthographicCamera();
		screen.setToOrtho(false, 640, 640);
		batch = new SpriteBatch();
		bgImg1 = new Texture("bg.png");
		bgImg2 = bgImg1;
		rocketImg = new Texture("rocket.png");
		shipImg = new Texture("ship.png");
		mothershipImg = new Texture("mothership.png");
		rayImg = new Texture("ray.png");
		ship = new Rectangle();
		ship.x = 240;
		ship.y = 50;
		ship.height = 79;
		ship.width = 82;
		rockets = new Array<Rectangle>();
		mothershipEntity = new Rectangle();
		mothershipEntity.x = -200;
		mothershipEntity.y = 300;
		mothershipEntity.height = 150;
		mothershipEntity.width = 150;
		directionMS = "left";
		shotDelay = 0;
		score = 0;
		font = new BitmapFont();
	}

	public void spawnRocket() {
		Rectangle rocket = new Rectangle();
		rocket.x = ship.x + 32;
		rocket.y = ship.y + 20;
		rocket.width = 18;
		rocket.height = 45;
		rockets.add(rocket);
	}
	public void spawnRay(){
		Rectangle ray = new Rectangle();
		ray.x = mothershipEntity.x - 100;
		ray.y = mothershipEntity.y + 50;
		ray.height = 50;
		ray.width = 50;
		rayProjectiles.add(ray);
	}


	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		screen.update();

		batch.begin();
		batch.draw(bgImg1, 0, scrollDist);
		batch.draw(bgImg2, 0, scrollDist2);
		for (Rectangle rocket : rockets) {
			batch.draw(rocketImg, rocket.x, rocket.y);
		}
		for (Rectangle ray : rayProjectiles) {
			batch.draw(rayImg, ray.x, ray.y);
		}
		batch.draw(mothershipImg, mothershipEntity.x, mothershipEntity.y);
		batch.draw(shipImg, ship.x, ship.y);
		font.draw(batch, "Current score: " + score, 20, 20);
		batch.end();

		// move
		if (Gdx.input.isKeyPressed(Input.Keys.A) & ship.x > 0)
			ship.x -= 400 * Gdx.graphics.getDeltaTime() / focusMovementReduction;
		if (Gdx.input.isKeyPressed(Input.Keys.D) & ship.x < 640 - ship.width)
			ship.x += 400 * Gdx.graphics.getDeltaTime() / focusMovementReduction;
		if (Gdx.input.isKeyPressed(Input.Keys.S) & ship.y > 10)
			ship.y -= 600 * Gdx.graphics.getDeltaTime() / focusMovementReduction;
		if (Gdx.input.isKeyPressed(Input.Keys.W) & ship.y < 300)
			ship.y += 200 * Gdx.graphics.getDeltaTime() / focusMovementReduction;
		if (Gdx.input.isKeyPressed(Input.Keys.K)) {
			mothershipEntity.x = MathUtils.random(10, 640 - 80);
			System.out.println("Loaded mothership");
		}

		// shoot
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) & shotDelay == 0) {
			spawnRocket();
			shotDelay = 10 / focusMovementReduction;
		}

		// focus
		if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
			focusMovementReduction = 2;
		}else if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			focusMovementReduction = 1;
		}


		for (Iterator<Rectangle> iter = rockets.iterator(); iter.hasNext(); ) {
			Rectangle rocket = iter.next();
			rocket.y += 600 * Gdx.graphics.getDeltaTime();
			if(rocket.y > 600) iter.remove();
		}
        if (mothershipEntity.x >= 0) {
            if (Objects.equals(directionMS, "right") & (mothershipEntity. x <= 640 - mothershipEntity.width))
                mothershipEntity.x += 50 * Gdx.graphics.getDeltaTime();
            if (Objects.equals(directionMS, "left") & mothershipEntity.x >= 1)
                mothershipEntity.x -= 50 * Gdx.graphics.getDeltaTime();
            if (ship.x < 166)
                directionMS = "left";
			else if (ship.x > 445)
				directionMS = "right";
		}

		if (shotDelay > 0)
			shotDelay -= 1;

		if (scrollDist == -1920)
			scrollDist = 1920;
		if (scrollDist2 == -1920)
			scrollDist2 = 1920;

		scrollDist -= scrollSpeed;
		scrollDist2 -= scrollSpeed;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		bgImg1.dispose();
		bgImg2.dispose();
		rocketImg.dispose();
		shipImg.dispose();
		mothershipImg.dispose();
	}
}
