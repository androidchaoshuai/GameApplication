package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jicent.jetrun.data.BuffType;
import com.jicent.jetrun.data.DialogType;
import com.jicent.jetrun.data.GameState;
import com.jicent.jetrun.data.HeroState;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.extensions.ButtonEx;
import com.jicent.jetrun.extensions.ButtonEx.InputListenerEx;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.screen.FatherScreen;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.screen.ShopScreen;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.InfoToast;
import com.jicent.jetrun.utils.LayoutDebug;
import com.jicent.jetrun.utils.NextOperate;
import com.jicent.jetrun.utils.PayUtil.PayType;
import com.jicent.jetrun.utils.SPUtil;
import com.jicent.jetrun.utils.SoundUtil;

public class WindowDialog extends Group implements InputListenerEx{
	private FatherScreen screen;
	private DialogType type;
	private ButtonEx restartBtn,continueBtn,getBtn,exitBtn,reviveBtn,rankBtn;
	private CheckBox musicBtn,soundBtn;
	private float reviveTimer=3;
	private boolean isStartTimer=false;
	private boolean isShowGameOver=false;
	
	
	public WindowDialog(FatherScreen screen) {
		this.screen=screen;
	}
	
	public Group getDialog(DialogType type){
		
		SPUtil.commit(screen.main.getSp(), "coinNum", StaticVariable.coinNum);
		
		Group group=new Group();
		this.type=type;
		switch (type) { 
		case pause:
			((GameScreen)screen).hero.setState(HeroState.down);
			Image dialogBg=new Image(screen.getTexture("res/pauseBg.png"));
			group.setSize(dialogBg.getWidth(), dialogBg.getHeight());
			group.addActor(dialogBg);
			
			
			continueBtn=new ButtonEx(screen, screen.getTexture("res/backBtn.png"));
			continueBtn.setPosition(35,50);
			continueBtn.addListener(this);
			group.addActor(continueBtn);
			
			 
			restartBtn=new ButtonEx(screen, screen.getTexture("res/reBtn.png"));
			restartBtn.setPosition(189,50);
			restartBtn.addListener(this);
			group.addActor(restartBtn);
			
			
			exitBtn=new ButtonEx(screen, screen.getTexture("res/homeBtn.png"));
			exitBtn.setPosition(344,50);
			exitBtn.addListener(this);
			group.addActor(exitBtn);
			
			
			musicBtn=new CheckBox("", new CheckBoxStyle(new TextureRegionDrawable(new TextureRegion(screen.getTexture("res/offBtn.png"))), 
					new TextureRegionDrawable(new TextureRegion(screen.getTexture("res/onBtn.png"))), screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
			musicBtn.setPosition(257,228);
			musicBtn.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					StaticVariable.isMusicOn=!StaticVariable.isMusicOn;
					SPUtil.commit(screen.main.getSp(), "isMusicOn", StaticVariable.isMusicOn);
					if(StaticVariable.isMusicOn){
						SoundUtil.playMusic("gameMusic");
					}else{
						SoundUtil.stopMusic();
					}
				}
			});
			if(StaticVariable.isMusicOn){
				musicBtn.setChecked(true);
			}else{
				musicBtn.setChecked(false);
			}
			group.addActor(musicBtn);
			
			soundBtn=new CheckBox("", new CheckBoxStyle(new TextureRegionDrawable(new TextureRegion(screen.getTexture("res/offBtn.png"))), 
					new TextureRegionDrawable(new TextureRegion(screen.getTexture("res/onBtn.png"))), screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
			soundBtn.setPosition(257,150);
			soundBtn.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					StaticVariable.isSoundOn=!StaticVariable.isSoundOn;
					SPUtil.commit(screen.main.getSp(), "isSoundOn", StaticVariable.isSoundOn);
				}
			});
			if(StaticVariable.isSoundOn){
				soundBtn.setChecked(true);
			}else{
				soundBtn.setChecked(false);
			}
			group.addActor(soundBtn);
			
			break;
		case RANK:
			dialogBg=new Image(screen.getTexture("res/rankBg.png"));
			group.setSize(dialogBg.getWidth(), dialogBg.getHeight());
			group.addActor(dialogBg);
			
			RankUi rankUi = new RankUi(screen);
			rankUi.setPosition(17,35);
			group.addActor(rankUi);
			
			exitBtn=new ButtonEx(screen, screen.getTexture("res/exitBtn.png"));
			exitBtn.setPosition(391,331);
			exitBtn.addListener(this);
			group.addActor(exitBtn);
			break;
		case revieve:
			
			isStartTimer=true;
			reviveTimer=3;
			
			reviveBtn=new ButtonEx(screen, screen.getTexture("res/revieveBtn.png"));
			group.setSize(reviveBtn.getWidth(), reviveBtn.getHeight());
			group.addActor(reviveBtn);
			reviveBtn.addListener(this);
			
			//价格label
			Label priceLabel=new Label("5000", new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
			priceLabel.setFontScale(0.8f);
			priceLabel.setPosition(60-priceLabel.getTextBounds().width/2,-21);
			priceLabel.setTouchable(Touchable.disabled);
			group.addActor(priceLabel);
//			new LayoutDebug(priceLabel);
			
			break;
		case gameover:
			//享受宠物加成
			GameScreen gameScreen=(GameScreen)screen;
			switch (StaticVariable.roleKind) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				if(StaticVariable.isBuyPet2){
					int addCoinNum=(int)(gameScreen.widget.coinNum*0.05);
					gameScreen.widget.addCoinNum(addCoinNum);
					InfoToast.show(screen,"宠物奖励，额外"+addCoinNum+"金币");
				}
				break;
			case 3:
				if(StaticVariable.isBuyPet3){
					int addCoinNum=(int)(gameScreen.widget.coinNum*0.10);
					gameScreen.widget.addCoinNum(addCoinNum);
					InfoToast.show(screen,"宠物奖励，额外"+addCoinNum+"金币");
				}
				break;
			case 4:
				if(StaticVariable.isBuyPet4){
					int addCoinNum=(int)(gameScreen.widget.coinNum*0.15);
					gameScreen.widget.addCoinNum(addCoinNum);
					InfoToast.show(screen,"宠物奖励，额外"+addCoinNum+"金币");
				}
				break;
			default:
				break;
			}
			
			
			dialogBg=new Image(screen.getTexture("res/loseBg.png"));
			group.setSize(dialogBg.getWidth(), dialogBg.getHeight());
			group.addActor(dialogBg);

			
			Label distanceLabel=new Label(((GameScreen)screen).widget.realDistance+"m", new LabelStyle(screen.getBitmapFont("font/scoreFont.fnt"), Color.WHITE));
			distanceLabel.setFontScale(1.2f);
			distanceLabel.setPosition(28,248);
			group.addActor(distanceLabel);
			
			
			distanceLabel=new Label(""+((GameScreen)screen).widget.coinNum, new LabelStyle(screen.getBitmapFont("font/scoreFont.fnt"), Color.WHITE));
			distanceLabel.setFontScale(0.8f);
			distanceLabel.setPosition(21,112);
			group.addActor(distanceLabel);
			
			int score=((GameScreen)screen).widget.realDistance+((GameScreen)screen).widget.coinNum;
			//宠物享受分数加成
			if(StaticVariable.roleKind==StaticVariable.role0&&StaticVariable.isBuyPet0){
				InfoToast.show(gameScreen, "享受宠物分数加成5%");
				score=(int) ((float)score*1.05f);
			}else if(StaticVariable.roleKind==StaticVariable.role1&&StaticVariable.isBuyPet1){
				InfoToast.show(gameScreen, "享受宠物分数加成5%");
				score=(int) ((float)score*1.05f);
			}else if(StaticVariable.roleKind==StaticVariable.role3&&StaticVariable.isBuyPet3){
				InfoToast.show(gameScreen, "享受宠物分数加成5%");
				score=(int) ((float)score*1.05f);
			}else if(StaticVariable.roleKind==StaticVariable.role4&&StaticVariable.isBuyPet4){
				InfoToast.show(gameScreen, "享受宠物分数加成5%");
				score=(int) ((float)score*1.05f);
			}
			
			distanceLabel=new Label(""+score, new LabelStyle(screen.getBitmapFont("font/scoreFont.fnt"), Color.WHITE));
			distanceLabel.setFontScale(0.8f);
			distanceLabel.setPosition(21,33);
			group.addActor(distanceLabel);
			
			//添加评价
			int level=0;
			
			if(score>3500){
				level=0;
			}else if(score>2400){
				level=1;
			}else if(score>1600){
				level=2;
			}else if(score>900){
				level=3;
			}else{
				level=4;
			}
			 
			
			Image img=new Image(screen.getTexture("res/level"+level+".png"));
			img.setPosition(270, 200);
			img.setOrigin(Align.center);
			img.addAction(Actions.sequence(Actions.delay(0.2f),Actions.scaleTo(1.7f, 1.7f),Actions.scaleTo(1, 1,0.5f)));
			group.addActor(img);
			
			//判断最好成绩的字
			if(score>StaticVariable.bestScore){
				img=new Image(screen.getTexture("res/bestTxt.png"));
				img.setPosition(31,202);
				img.setOrigin(Align.center);
				img.addAction(Actions.sequence(Actions.delay(0.2f),Actions.scaleTo(1.7f, 1.7f),Actions.scaleTo(1, 1,0.5f)));
				group.addActor(img);
//				new LayoutDebug(img);
				
				StaticVariable.bestScore=score;
				SPUtil.commit(screen.main.getSp(), "bestScore", StaticVariable.bestScore);
			}
			
			
			exitBtn=new ButtonEx(screen, screen.getTexture("res/homeBtn.png"));
			exitBtn.setPosition(248,143);
			exitBtn.addListener(this);
			group.addActor(exitBtn);
			
			restartBtn=new ButtonEx(screen, screen.getTexture("res/reBtn.png"));
			restartBtn.setPosition(248,80);
			restartBtn.addListener(this);
			group.addActor(restartBtn);
			
			rankBtn=new ButtonEx(screen, screen.getTexture("res/rankBtn1.png"));
			rankBtn.setPosition(248,18);
			rankBtn.addListener(this);
			group.addActor(rankBtn);
			break;
		case set:
			dialogBg=new Image(screen.getTexture("res/setBg.png"));
			group.setSize(dialogBg.getWidth(), dialogBg.getHeight());
			group.addActor(dialogBg);
			
			exitBtn=new ButtonEx(screen, screen.getTexture("res/exitBtn.png"));
			exitBtn.setPosition(497,291);
			exitBtn.addListener(this);
			group.addActor(exitBtn);
			
			musicBtn=new CheckBox("", new CheckBoxStyle(new TextureRegionDrawable(new TextureRegion(screen.getTexture("res/offBtn.png"))), 
					new TextureRegionDrawable(new TextureRegion(screen.getTexture("res/onBtn.png"))), screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
			musicBtn.setPosition(123,180);
			musicBtn.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					StaticVariable.isMusicOn=!StaticVariable.isMusicOn;
					SPUtil.commit(screen.main.getSp(), "isMusicOn", StaticVariable.isMusicOn);
					if(StaticVariable.isMusicOn){
						SoundUtil.playMusic("gameMusic");
					}else{
						SoundUtil.stopMusic();
					}
				}
			});
			if(StaticVariable.isMusicOn){
				musicBtn.setChecked(true);
			}else{
				musicBtn.setChecked(false);
			}
			group.addActor(musicBtn);
			
			soundBtn=new CheckBox("", new CheckBoxStyle(new TextureRegionDrawable(new TextureRegion(screen.getTexture("res/offBtn.png"))), 
					new TextureRegionDrawable(new TextureRegion(screen.getTexture("res/onBtn.png"))), screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
			soundBtn.setPosition(123,100);
			soundBtn.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					StaticVariable.isSoundOn=!StaticVariable.isSoundOn;
					SPUtil.commit(screen.main.getSp(), "isSoundOn", StaticVariable.isSoundOn);
				}
			});
			if(StaticVariable.isSoundOn){
				soundBtn.setChecked(true);
			}else{
				soundBtn.setChecked(false);
			}
			group.addActor(soundBtn);
			
			GuideShow guideShow=new GuideShow();
			group.addActor(guideShow);
			
			break;
		case coinGift:
			dialogBg=new Image(screen.getTexture("res/jinbiBg.png"));
			group.setSize(dialogBg.getWidth(), dialogBg.getHeight());
			group.addActor(dialogBg);

			getBtn=new ButtonEx(screen, screen.getTexture("res/lingqu.png"));
			getBtn.setPosition(291,44);
			getBtn.addListener(this);
			group.addActor(getBtn);
			
			exitBtn=new ButtonEx(screen, screen.getTexture("res/exitBtn.png"));
			exitBtn.setPosition(632, 352);
			exitBtn.addListener(this);
			group.addActor(exitBtn);
			break;
		case propGift:
			dialogBg=new Image(screen.getTexture("res/daojuBg.png"));
			group.setSize(dialogBg.getWidth(), dialogBg.getHeight());
			group.addActor(dialogBg);
			
			getBtn=new ButtonEx(screen, screen.getTexture("res/lingqu.png"));
			getBtn.setPosition(282,37);
			getBtn.addListener(this);
			group.addActor(getBtn);
			
			exitBtn=new ButtonEx(screen, screen.getTexture("res/exitBtn.png"));
			exitBtn.setPosition(632, 352);
			exitBtn.addListener(this);
			group.addActor(exitBtn);
			break;
		default:
			break;
		}
		return group;
	}
	@Override
	public void act(float delta) {
		super.act(delta);
		if(isStartTimer){
			reviveTimer-=delta;
			if(reviveTimer<=0){
				isStartTimer=false;
				DialogUtil.dismiss(new NextOperate() {
					@Override
					public void nextDone() {
						isShowGameOver=true;
						DialogUtil.show(screen, screen.dialog.getDialog(DialogType.propGift), ProcessType.empty);
//						DialogUtil.show(screen, getDialog(DialogType.gameover), ProcessType.empty);
					}
				});
			}
		}
		
	}
	@Override
	public boolean touchDown(Actor actor) {
		SoundUtil.playSound(screen.main.getManager(), "button");
		return true;
	}

	@Override
	public void touchUp(Actor actor) {
		if (actor==continueBtn) {
			DialogUtil.dismiss(new NextOperate() {
				
				@Override
				public void nextDone() {
					GameScreen gameScreen=(GameScreen)screen;
					gameScreen.changeState(GameState.running);
				}
			});
		}else if (actor==restartBtn) {
			DialogUtil.dismiss(new NextOperate() {
				
				@Override
				public void nextDone() {
					GameScreen gameScreen=(GameScreen)screen;
					gameScreen.changeState(GameState.running);
					gameScreen.reset();
					if (type==DialogType.gameover) {
						gameScreen.hero.setDeath(false);
					}
				}
			});
		}else if (actor==exitBtn) {
			switch (type) {
			case gameover:
			case pause:
				DialogUtil.dismiss(new NextOperate() {
					
					@Override
					public void nextDone() {
						screen.changeScreen(true, new ShopScreen(screen.main, ProcessType.shopScreen));
						SoundUtil.stopMusic();
					}
				});
				break;
			case revieve:
				DialogUtil.dismiss(new NextOperate() {
					
					@Override
					public void nextDone() {
						DialogUtil.show(screen, getDialog(DialogType.gameover), ProcessType.empty);				
					}
				});
				break;
			case coinGift:
				if (screen instanceof GameScreen) {
					DialogUtil.dismiss(new NextOperate() {
						
						@Override
						public void nextDone() {
							isStartTimer=true;
							DialogUtil.show(screen, getDialog(DialogType.revieve), ProcessType.empty);	
						}
					});
				}else if(screen instanceof ShopScreen){
					DialogUtil.dismiss();
					if(((ShopScreen)screen).isShowExit){
						screen.main.showExitDialog();
						((ShopScreen)screen).isShowExit=false;
					}
				}
				break;
			case set:
				DialogUtil.dismiss();
				break;
			case propGift:
				if(isShowGameOver){
					isShowGameOver=false;
					DialogUtil.dismiss(new NextOperate() {
						@Override
						public void nextDone() {
							DialogUtil.show(screen, getDialog(DialogType.gameover), ProcessType.empty);
						}
					});
				}else{
					DialogUtil.dismiss();
					if(screen instanceof GameScreen)
						((GameScreen)screen).changeState(GameState.running);
				}
				break;
			case RANK:
				if(screen instanceof GameScreen){
					DialogUtil.dismiss(new NextOperate() {
						@Override
						public void nextDone() {
							DialogUtil.show(screen, getDialog(DialogType.gameover), ProcessType.empty);	
						}
					});
				}else{
					DialogUtil.dismiss();	
				}
				break;
			default:
				break;
			}
		}else if(actor==reviveBtn){
			if(StaticVariable.coinNum>=5000){
				isStartTimer=false;
				
				StaticVariable.coinNum-=5000;
				SPUtil.commit(screen.main.getSp(), "coinNum", StaticVariable.coinNum);
				
				DialogUtil.dismiss(new NextOperate() {
					@Override
					public void nextDone() {
						GameScreen gameScreen=(GameScreen)screen;
						gameScreen.changeState(GameState.running);
						gameScreen.speed=6;
						gameScreen.hero.setDeath(false);
						gameScreen.hero.skeleton.setSlotsToSetupPose();
						gameScreen.control.addBuff(BuffType.speedUp);		
					}
				});
				InfoToast.show(screen, "购买成功，消耗5000金币");
			}else{
				InfoToast.show(screen, "金币不足");
				isStartTimer=false;
				reviveTimer=3;
				//弹礼包
				DialogUtil.dismiss(new NextOperate() {
					@Override
					public void nextDone() {
						DialogUtil.show(screen, getDialog(DialogType.coinGift), ProcessType.empty);	
					}
				});
			}
		}else if(actor==getBtn){
			switch (type) {
			case coinGift:
				screen.setPay(PayType.coinGift);
				break;
			case propGift:
				screen.setPay(PayType.propGift);
				break;
			default:
				break;
			}
		}else if(actor==rankBtn){
			if (StaticVariable.name=="") {
				screen.main.getActivity().handler.sendEmptyMessage(0);
			}else {
				DialogUtil.dismiss(new NextOperate() {
					@Override
					public void nextDone() {
						DialogUtil.show(screen, screen.dialog.getDialog(DialogType.RANK), ProcessType.dismiss);		
					}
				});
			}
		}
	}
 
	class GuideShow extends Group{
		private int index=0;
		private Image img;
		public GuideShow(){
			setPosition(245, 26);
			img=new Image(screen.getTexture("res/guide0.png"));
			setSize(img.getWidth(), img.getHeight());
			this.addActor(img);
			
			addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					if(++index>3)index=0;
					img.setDrawable(new TextureRegionDrawable(new TextureRegion(screen.getTexture("res/guide"+index+".png"))));
				}
			}); 
		}
	}
}

