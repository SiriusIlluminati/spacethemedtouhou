package com.siriusilluminati.spacethemedtouhou;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.Iterator;
import java.util.Objects;
import static com.siriusilluminati.spacethemedtouhou.shipHPIndicator.shipHbSpriteCalcer;

public class SpaceShooter extends ApplicationAdapter {
	private String directionMS;
	int invFrames;
	int msSpeed;
	int scrollSpeed;
	int scrollDist;
	int scrollDist2;
	int focusMovementReduction;
	int shotDelay;
	int score;
	int bossHP;
	int maxBossHP;
	float hpBarPercent;
	Sprite hpBarGreen;
	Sprite hpBarRed;

	TextureAtlas textureAtlasHBShip;
	int hbsDrain;
	int shipHP;
	int hbsFill;
	int hbsAnimCounter;

	TextureAtlas textureAtlasBlink;
	int blinkAnimCounter;
	int blinkAnimStage;
	int rayDurationRemaining;
	boolean blinkIndicatorActive;
	boolean rayFireable;

	String activeBoss;
	boolean hitboxMode;
	SpriteBatch batch;
	Texture bgImg1;
	Texture bgImg2;
	Texture mothershipImg;
	Texture shipImg;
	Texture shipDamaged;
	Sprite shipSprite;
	Texture rayImg;

	Texture hbShip;
	Texture hbMS;
	Texture hbRay;
	Texture hbRocketFriendly;

	BitmapFont font;
	Texture rocketImg;
	Rectangle ship;
	Rectangle mothershipEntity;
	Array<Rectangle> rockets;
	Array<Rectangle> rayProjectiles;
	OrthographicCamera screen;

	boolean obamaMode;
	Texture obamaShip;
	Texture eagleRay;
	Texture trumpMS;

	@Override
	public void create () {
		invFrames = 0;
		msSpeed = 50;
		scrollSpeed = 2;
		scrollDist = 0;
		scrollDist2 = 1920;
		hitboxMode = false;
		screen = new OrthographicCamera();
		screen.setToOrtho(false, 640, 640);
		batch = new SpriteBatch();
		bgImg1 = new Texture("bg.png");
		bgImg2 = bgImg1;

		rocketImg = new Texture("rocket.png");
		shipImg = new Texture("ship.png");
		shipDamaged = new Texture("shipDamaged.png");
		shipSprite = new Sprite(shipImg);
		mothershipImg = new Texture("mothership.png");
		rayImg = new Texture("ray.png");

		hbMS = new Texture("hbMS.png");
		hbRay = new Texture("hbRay.png");
		hbShip = new Texture("hbShip.png");
		hbRocketFriendly = new Texture("hbRocketFriendly.png");
		ship = new Rectangle();
		ship.x = 290;
		ship.y = 50;
		ship.height = 72;
		ship.width = 54;
		shipHP = 6;
		rockets = new Array<>();
		rayProjectiles = new Array<>();
		mothershipEntity = new Rectangle();
		mothershipEntity.x = -200;
		mothershipEntity.y = 300;
		mothershipEntity.height = 117;
		mothershipEntity.width = 126;
		directionMS = "left";
		shotDelay = 0;
		rayFireable = false;

		hpBarGreen = new Sprite(new Texture("hpFull.png"));
		hpBarRed = new Sprite(new Texture("hpEmpty.png"));
		textureAtlasHBShip = new TextureAtlas("hpbship.txt");
		textureAtlasBlink = new TextureAtlas("blink.txt");
		score = 0;
		font = new BitmapFont();
		obamaMode = false;
		obamaShip = new Texture("obamaShip.png");
		eagleRay = new Texture("eagleRay.png");
		trumpMS = new Texture("trumpMS.png");
	}

	public void spawnRocket() {
		Rectangle rocket = new Rectangle();
		rocket.x = ship.x + 18;
		rocket.y = ship.y + 13;
		rocket.width = 18;
		rocket.height = 45;
		rockets.add(rocket);
	}
	public void spawnRay(){
		Rectangle ray = new Rectangle();
		ray.x = mothershipEntity.x + 47;
		ray.y = mothershipEntity.y + 50;
		ray.height = 50;
		ray.width = 32;
		rayProjectiles.add(ray);
	}

	public void shipHit(){
		invFrames = 20;
		shipHP -= 1;
		hbsDrain = 1;
	}
	public void shipHeal(){
		shipHP += 1;
		hbsFill = 1;
		hbsDrain = 0;
	}


	@SuppressWarnings("SuspiciousIndentation")
	// Hell yeah
    @Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		screen.update();

		// 20fps hb animations
		if (hbsAnimCounter == 2) {
			switch (hbsDrain) {
				case 1:
				case 2:
					hbsDrain += 1;
					break;
				case 3:
					hbsDrain = 0;
			}
			switch (hbsFill) {
				case 1:
				case 2:
					hbsFill += 1;
					break;
				case 3:
					hbsFill = 0;
			}
			hbsAnimCounter = 0;
		}else{
			hbsAnimCounter += 1;
		}

		batch.begin();
		if (obamaMode) {
			batch.draw(bgImg1, 0, scrollDist);
			batch.draw(bgImg2, 0, scrollDist2);
			for (Rectangle rocket : rockets) {
				batch.draw(rocketImg, rocket.x, rocket.y);
			}
			for (Rectangle ray : rayProjectiles) {
				batch.draw(eagleRay, ray.x, ray.y);
			}
			batch.draw(trumpMS, mothershipEntity.x, mothershipEntity.y);
			batch.draw(obamaShip, ship.x, ship.y);
		} else {
			batch.draw(bgImg1, 0, scrollDist);
			batch.draw(bgImg2, 0, scrollDist2);
			for (Rectangle rocket : rockets) {
				batch.draw(rocketImg, rocket.x, rocket.y);
			}
			for (Rectangle ray : rayProjectiles) {
				batch.draw(rayImg, ray.x, ray.y);
			}
			if (invFrames != 0) {
				batch.draw(shipDamaged, ship.x, ship.y);
			} else {
				batch.draw(shipSprite, ship.x, ship.y);
			}
			// 15fps blink animation
			if (blinkIndicatorActive) {
				if (blinkAnimCounter == 3) {
					if (blinkAnimStage == 8) {
						blinkAnimStage = 0;
						blinkIndicatorActive = false;
						rayFireable = true;
					} else {
						blinkAnimStage += 1;
						blinkAnimCounter = 0;
					}

				} else {
					blinkAnimCounter += 1;
				}
				batch.draw(textureAtlasBlink.createSprite("sprite_blink" + blinkAnimStage), mothershipEntity.x + 51, mothershipEntity.y - 12);
			}
			batch.draw(mothershipImg, mothershipEntity.x, mothershipEntity.y);
		}

		if (hitboxMode) {
			batch.draw(hbShip, ship.x, ship.y);
			batch.draw(hbMS, mothershipEntity.x, mothershipEntity.y);
			for (Rectangle ray : rayProjectiles) {
				batch.draw(hbRay, ray.x, ray.y);
			}
			for (Rectangle rocket : rockets) {
				batch.draw(hbRocketFriendly, rocket.x, rocket.y);
			}
		}
		font.draw(batch, "Current score: " + score, 20, 20);
		if (activeBoss != null) {
			batch.draw(hpBarRed, 225, 450);
			batch.draw(hpBarGreen, 225, 450, (200 * hpBarPercent), 20);
		}
		batch.draw(shipHbSpriteCalcer(textureAtlasHBShip, shipHP, hbsDrain, hbsFill), 580, 40);
		batch.end();

		if (shipHP > 0) {
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
				activeBoss = "MS";
				bossHP = 100;
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
			} else if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
				focusMovementReduction = 1;
			}


			for (Iterator<Rectangle> iter = rockets.iterator(); iter.hasNext(); ) {
				Rectangle rocket = iter.next();
				rocket.y += 600 * Gdx.graphics.getDeltaTime();
				if (rocket.overlaps(mothershipEntity)) {
					score += 1;
					bossHP -= 1;
					iter.remove();
				}
				if (rocket.y > 600)
					iter.remove();
			}
			for (Iterator<Rectangle> iter = rayProjectiles.iterator(); iter.hasNext(); ) {
				Rectangle ray = iter.next();
				ray.y -= 2800 * Gdx.graphics.getDeltaTime();
				if (ray.overlaps(ship) & invFrames == 0) {
					shipHit();
				}
				if (ray.y < -100) iter.remove();
			}


			if (Objects.equals(activeBoss, "MS")) {
				maxBossHP = 100;
				// movement
				if (Objects.equals(directionMS, "right") & (mothershipEntity.x <= 640 - mothershipEntity.width))
					mothershipEntity.x += msSpeed * Gdx.graphics.getDeltaTime();
				if (Objects.equals(directionMS, "left") & mothershipEntity.x >= 1)
					mothershipEntity.x -= msSpeed * Gdx.graphics.getDeltaTime();
				// movement ai
				if (ship.x <= 166 & Objects.equals(directionMS, "right"))
					directionMS = "left";
				else if (ship.x >= 445 & Objects.equals(directionMS, "left"))
					directionMS = "right";
				if (mothershipEntity.x == 640 - mothershipEntity.width)
					directionMS = "left";
				if (Gdx.input.isKeyPressed(Input.Keys.M) && rayDurationRemaining == 0) {
					blinkIndicatorActive = true;
				}
				if (rayFireable){
					rayDurationRemaining = 180;
					rayFireable = false;
				}
				if (rayDurationRemaining != 0) {
					spawnRay();
					msSpeed = 0;
					rayDurationRemaining -= 1;
				} else {
					msSpeed = 50;
				}
			}

			if (shotDelay > 0)
				shotDelay -= 1;
			if (invFrames != 0)
				invFrames -= 1;

			if (scrollDist == -1920)
				scrollDist = 1920;
			if (scrollDist2 == -1920)
				scrollDist2 = 1920;

			scrollDist -= scrollSpeed;
			scrollDist2 -= scrollSpeed;
			

		}

		// devtools
		if (Gdx.input.isKeyPressed(Input.Keys.F2))
			hitboxMode = true;
		if (Gdx.input.isKeyPressed(Input.Keys.F3))
			hitboxMode = false;
		if (Gdx.input.isKeyPressed(Input.Keys.F4)){
			System.out.println(Gdx.graphics.getFramesPerSecond());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F7)) {
			invFrames = 2147403562;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F8)) {
			System.out.println(" ");
			System.out.println(" ");
			System.out.println("Player X: " + ship.x);
			System.out.println("Player Y: " + ship.y);
			System.out.println(" ");
			System.out.println("Active boss: " + activeBoss);
			if (Objects.equals(activeBoss, "MS")) {
				System.out.println("MS X: " + mothershipEntity.x);
				System.out.println("MS Y: " + mothershipEntity.y);
				System.out.println("MS direction: " + directionMS);
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F12)){
			System.out.println("Obama mode activated");
			obamaMode = true;
		}

		// hp bar fiddling
		if (activeBoss != null) {
			hpBarPercent = (float) (Math.ceil(((double) bossHP / maxBossHP) * 100) /100);
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		bgImg1.dispose();
		bgImg2.dispose();
		rocketImg.dispose();
		shipImg.dispose();
		mothershipImg.dispose();
		rayImg.dispose();
		hbShip.dispose();
		hbMS.dispose();
		hbRocketFriendly.dispose();
		hbRay.dispose();
		eagleRay.dispose();
		obamaShip.dispose();
		trumpMS.dispose();
		textureAtlasHBShip.dispose();
	}
}
