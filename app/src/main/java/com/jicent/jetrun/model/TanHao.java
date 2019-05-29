package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.SoundUtil;
/*
 * 火箭出来前的感叹号
 */
public class TanHao extends Actor {
	private GameScreen screen;
	private TextureRegion thTR;
	private boolean isEnd;

	public TanHao(final GameScreen screen) {
		this.screen = screen;
		thTR = new TextureRegion(screen.getTexture("res/tanhao.png"));
		setBounds(900, screen.hero.getY() + screen.hero.getHeight() / 2 - 35,
				28, 70);
		setOrigin(14, 35);
		this.addAction(Actions.sequence(
				Actions.repeat(3, Actions.sequence(
						Actions.scaleTo(0.7f, 0.7f, 0.1f),
						Actions.scaleTo(0.4f, 0.4f, 0.1f),
						Actions.scaleTo(0.7f, 0.7f, 0.1f),
						Actions.scaleTo(1f, 1f, 0.1f))),
				Actions.run(new Runnable() {

					@Override
					public void run() {
						isEnd = true;
						thTR = new TextureRegion(screen
								.getTexture("res/tanhaoEnd.png"));
						setBounds(888,
								screen.hero.getY() + screen.hero.getHeight()
										/ 2 - 52, 53, 104);
						setOrigin(26, 52);
						SoundUtil.playSound(screen.main.getManager(),
								"missileW");
					}
				}), Actions.repeat(8, Actions.sequence(
						Actions.moveBy(-1.5f, -1.5f, 0.025f),
						Actions.moveBy(1.5f, 1.5f, 0.025f))), Actions
						.run(new Runnable() {

							@Override
							public void run() {
								TanHao.this.remove();
								screen.missile.addMissile(TanHao.this
										.getY());
							}
						})));
//		Actions.sequence(
//				Actions.repeat(4, Actions.sequence(
//						Actions.moveBy(-1.5f, -1.5f, 0.025f),
//						Actions.moveBy(1.5f, 1.5f, 0.025f))),
//						Actions.run(new Runnable() {
//							
//							@Override
//							public void run() {
//								SoundUtil.playSound(screen.main.getManager(),
//										"missileW");
//							}
//						}), Actions.repeat(4, Actions.sequence(
//								Actions.moveBy(-1.5f, -1.5f, 0.025f),
//								Actions.moveBy(1.5f, 1.5f, 0.025f))), Actions
//								.run(new Runnable() {
//									
//									@Override
//									public void run() {
//										TanHao.this.remove();
//										screen.missile.addMissile(TanHao.this
//												.getY());
//									}
//								}))));
	}

	@Override
	public void act(float delta) {
		if (!screen.control.isStopGame) {
			super.act(delta);
			if (!isEnd) {
				setY(screen.hero.getY() + screen.hero.getHeight() / 2 - 35);
			}
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = this.getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		batch.draw(thTR, getX(), getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation());
	}

}
