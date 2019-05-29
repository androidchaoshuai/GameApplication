package com.jicent.jetrun.screen;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.jicent.jetrun.data.DialogType;
import com.jicent.jetrun.data.GameState;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.entry.GameMain;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.model.Background;
import com.jicent.jetrun.model.BangControl;
import com.jicent.jetrun.model.BuffInGame;
import com.jicent.jetrun.model.Bullet;
import com.jicent.jetrun.model.CoinControl;
import com.jicent.jetrun.model.DouRole;
import com.jicent.jetrun.model.ExplodeLight;
import com.jicent.jetrun.model.GameButton;
import com.jicent.jetrun.model.GameControl;
import com.jicent.jetrun.model.GameMap;
import com.jicent.jetrun.model.GameWidget;
import com.jicent.jetrun.model.Gun;
import com.jicent.jetrun.model.Hero;
import com.jicent.jetrun.model.HintLight;
import com.jicent.jetrun.model.Laser;
import com.jicent.jetrun.model.Missile;
import com.jicent.jetrun.model.Mount;
import com.jicent.jetrun.model.MountCrush;
import com.jicent.jetrun.model.Pet;
import com.jicent.jetrun.model.RotationBarrierControl;
import com.jicent.jetrun.model.Spikeweed;
import com.jicent.jetrun.model.StandBarrierControl;
import com.jicent.jetrun.model.WindowDialog;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.NextOperate;
import com.jicent.jetrun.utils.SoundUtil;


public class GameScreen extends FatherScreen{
	public Background bg;
	public CoinControl coinControl;
	public GameMap map;
	public StandBarrierControl standBarrierControl;
	public RotationBarrierControl rotationBarrierControl;
	public Hero hero;
	public Missile missile;
	public Laser laser;
	public Pet pet;
	public GameControl control;
	public Mount mount;
	public Gun gun;
	public Bullet bullet;
	public DouRole dou;
	public GameButton button;
	public GameWidget widget;
	public HintLight hintLight;
	public MountCrush mountCrush;
	public BangControl bangControl;
	
	private GameState gameState;
	
	public float speed;
	public ExplodeLight explodeLight;
	public Spikeweed spikeweed;
	public BuffInGame buffInGame;
	
	ShapeRenderer sr;
	
	public GameScreen(GameMain main, ProcessType type) {
		super(main, type);
		
	}
	@Override
	public void show() {
		super.show();
		
		
		actorInitalize();
		dialog=new WindowDialog(this);
		
		sr=new ShapeRenderer();
		sr.setProjectionMatrix(mainStage.getCamera().combined);
	}
	
	@Override
	public void render(float delta) {
//		long time=TimeUtils.millis();
		switch (gameState) {
		case running:
			mainStage.act();
			mainStage.draw();
			break;
		case pause:
			mainStage.draw();
			break;
		default:
			break;
		}
		dialog.act(delta);
		dialogStage.act();
		dialogStage.draw();
		toastStage.act();
		toastStage.draw();

		
		// 支付完成后的操作，在stage的act和draw后进行调用
		payDeal();

		// 屏幕切换，在stage都act和draw后进行调用
		if (isChangeScreen) {
			main.setScreen(nextScreen);
		}
		//玩家在结束框首次输入名字弹排行榜用
		if (StaticVariable.nameOk) {
			DialogUtil.dismiss(new NextOperate() {
				@Override
				public void nextDone() {
					DialogUtil.show(GameScreen.this, dialog.getDialog(DialogType.RANK), ProcessType.empty);	
				}
			});
			StaticVariable.nameOk=false;
		}
		
//		LogUtil.e(GameScreen.class.getSimpleName(), (TimeUtils.millis()-time)+"", true);
//		sr.begin(ShapeType.Line);
//		sr.rect(hero.getPolygon().x, hero.getPolygon().y, hero.getPolygon().width, hero.getPolygon().height);
//		sr.end();
		
	}

	public void reset() {
		mainStage.addAction(Actions.sequence(Actions.fadeOut(0.3f),Actions.run(new Runnable() {
			
			@Override
			public void run() {
//				clearTmpRes();
				coinControl.dispose();
				standBarrierControl.dispose();
				rotationBarrierControl.dispose();
				control.dispose();
				
				mainStage.clear();
				
				actorInitalize();
				
				mainStage.addAction(Actions.fadeIn(0));
			}
		})));
		
	}
	
	public void changeState(GameState state){
		gameState=state;
		switch (state) {
		case running:
			SoundUtil.resumeLoopSound();
			break;
		case pause:
			SoundUtil.pauseLoopSound();
			break;
		default:
			break;
		}
	}

	//资源清空时调用，系统在切屏的时候，会主动调用
	@Override
	public void hide() {
		super.hide();
		coinControl.dispose();
		standBarrierControl.dispose();
		rotationBarrierControl.dispose();
		control.dispose();
	}
	
	//游戏界面所有的元素初始化
	private void actorInitalize() {
		SoundUtil.playMusic("gameMusic");
		
		speed=7;
		gameState=GameState.running;
		
		bg=new Background(this);
		mainStage.addActor(bg);		
		
		coinControl=new CoinControl(this);
		mainStage.addActor(coinControl);		
		standBarrierControl =new StandBarrierControl(this);
		mainStage.addActor(standBarrierControl);		
		rotationBarrierControl = new RotationBarrierControl(this);
		mainStage.addActor(rotationBarrierControl);		
		
		map = new GameMap(this);
		mainStage.addActor(map);	
		
		spikeweed = new Spikeweed(this);
		mainStage.addActor(spikeweed);
		dou = new DouRole(this);
		mainStage.addActor(dou);
		
		laser=new Laser(this);
		mainStage.addActor(laser);
		
		missile=new Missile(this);
		mainStage.addActor(missile);
		
		hero=new Hero(this);
		
		switch (StaticVariable.roleKind) {
		case 0:
			if(StaticVariable.isBuyPet0){
				pet=new Pet(this);
				mainStage.addActor(pet);		
			}
			break;
		case 1:
			if(StaticVariable.isBuyPet1){
				pet=new Pet(this);
				mainStage.addActor(pet);		
			}
			break;
		case 2:
			if(StaticVariable.isBuyPet2){
				pet=new Pet(this);
				mainStage.addActor(pet);		
			}
			break;
		case 3:
			if(StaticVariable.isBuyPet3){
				pet=new Pet(this);
				mainStage.addActor(pet);		
			}
			break;
		case 4:
			if(StaticVariable.isBuyPet4){
				pet=new Pet(this);
				mainStage.addActor(pet);		
			}
			break;
		default:
			break;
		}
		
		
		mount=new Mount(this);
		mainStage.addActor(mount);
		gun=new Gun(this);
		mainStage.addActor(gun);
		
		bullet=new Bullet(this);
		mainStage.addActor(bullet);
		
		buffInGame = new BuffInGame(this);
		mainStage.addActor(buffInGame);
		
		explodeLight = new ExplodeLight(this);
		mainStage.addActor(explodeLight);
		
		control=new GameControl(this);
		bg.addActor(hero);
		mountCrush=new MountCrush(this);
		control.addActor(mountCrush);
		mainStage.addActor(control);
		
		bangControl=new BangControl(this);
		mainStage.addActor(bangControl);	
		
		hintLight=new HintLight(this);
		mainStage.addActor(hintLight);	
		
		
		widget=new GameWidget(this);
		mainStage.addActor(widget);
		
		button=new GameButton(this);
		mainStage.addActor(button);
		
		
	}
}
