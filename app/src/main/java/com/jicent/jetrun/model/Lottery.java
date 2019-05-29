package com.jicent.jetrun.model;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jicent.jetrun.data.GameState;
import com.jicent.jetrun.data.HeroState;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.extensions.ButtonEx;
import com.jicent.jetrun.extensions.ButtonEx.InputListenerEx;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.AnimateFactory;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.InfoToast;
import com.jicent.jetrun.utils.NextOperate;
import com.jicent.jetrun.utils.PayUtil.PayType;
import com.jicent.jetrun.utils.SPUtil;
import com.jicent.jetrun.utils.SoundUtil;

/*
 * 抽奖界面
 */
public class Lottery extends Group implements InputListenerEx{
	public GameScreen screen;

	LinkedList<Card> cards=new LinkedList<Lottery.Card>();
	ButtonEx exitBtn,allGetBtn;
	
	enum CardType{
		speedUp,gun,motor,shield,attract,coin
	}
	
	public Lottery(final GameScreen screen){
		this.screen=screen;
		StaticVariable.rewardDistance+=500;
//		SPUtil.commit(screen.main.getSp(), "rewardDistance", StaticVariable.rewardDistance);
		
		Image img=new Image(screen.getTexture("res/cardBg.png"));
		img.setPosition(0, 0);
		this.setSize(img.getWidth(), img.getHeight());
		this.addActor(img);
		
		
		Vector2 p[]={new Vector2(77,165),new Vector2(202,165),new Vector2(324,165),
				new Vector2(77,38),new Vector2(202,38),new Vector2(324,38)};
		for (int i = 0; i < 6; i++) {
			Card c=new Card();
			c.setPosition(p[i].x,p[i].y);
			this.addActor(c);
			cards.add(c);
		}
		
		exitBtn=new ButtonEx(screen, screen.getTexture("res/exitBtn.png"));
		exitBtn.setPosition(420,290);
		exitBtn.addListener(new InputListenerEx() {
			@Override
			public void touchUp(Actor actor) {
				
			}
			@Override
			public boolean touchDown(Actor actor) {
				DialogUtil.dismiss(new NextOperate() {
					@Override
					public void nextDone() {
						screen.control.releaseTouchUp();
						GameScreen gameScreen=(GameScreen)screen;
						gameScreen.changeState(GameState.running);
					}
				});
				return false;
			}
		});
		this.addActor(exitBtn);
		
	}
	@Override
	public void act(float delta) {
		super.act(delta);
//		if(cards.size()==6){
//			//若全部牌已经翻开，添加一个全部领取按钮
//			boolean isAllOpen=true;
//			for (int i = 0; i < cards.size(); i++) {
//				Card c=cards.get(i);
//				if(!c.isOpen){
//					isAllOpen=false;
//				}
//			}
//			if(isAllOpen){
//				
//			}
//		}
	}
	
	public void getAllCardProp(){
		int speedUpSum=0,gunSum=0,motorSum=0,shieldSum=0,attractSum=0,coinSum=0;
		
		for (int i = 0; i < cards.size(); i++) {
			Card c=cards.get(i);
			switch (c.type) {
			case coin:
				coinSum+=c.num;
				break;
			case shield:
				shieldSum+=c.num;
				break;
			case speedUp:
				speedUpSum+=c.num;
				break;
			case motor:
				motorSum+=c.num;
				break;
			case gun:
				gunSum+=c.num;
				break;
			case attract:
				attractSum+=c.num;
				break;
			default:
				break;
			}
		}
		screen.widget.addCoinNum(coinSum);
		SPUtil.commit(screen.main.getSp(), "coinNum", StaticVariable.coinNum);
		StaticVariable.speedUpCount+=speedUpSum;
		SPUtil.commit(screen.main.getSp(), "speedUpCount", StaticVariable.speedUpCount);
		StaticVariable.gunCount+=gunSum;
		SPUtil.commit(screen.main.getSp(), "gunCount", StaticVariable.gunCount);
		StaticVariable.mountCount+=motorSum;
		SPUtil.commit(screen.main.getSp(), "mountCount", StaticVariable.mountCount);
		StaticVariable.shieldCount+=shieldSum;
		SPUtil.commit(screen.main.getSp(), "shieldCount", StaticVariable.shieldCount);
		StaticVariable.attractCount+=attractSum;
		SPUtil.commit(screen.main.getSp(), "attractCount", StaticVariable.attractCount);
	}
	
	public class Card extends Group{
		
		private CardType type;
		
		public boolean isOpen=false;//卡片是否翻开
		private boolean isOpening=false;//是否开始翻
		boolean isClick=false;//是不是点的卡片，不是点的就不得金币
		private Animation anim;
		private float timer=0;
		private Image bgImg;
		
		int num=0;
		
		public Card(){
			type=getRandomType();
			System.out.println(type);
			setSize(80,112);
			anim=AnimateFactory.getAnimate(screen, "res/card.png", 80, 112, 0.1f, PlayMode.NORMAL);
			//卡
			bgImg=new Image();
			bgImg.setSize(80, 112);
			this.addActor(bgImg);
			//事件
			addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					isClick=true;
					isOpening=true;
					clearListeners();
				}
			});
		}
		@Override
		public void act(float delta) {
			super.act(delta);
			if(isOpening){
				timer+=delta;
				if(anim.isAnimationFinished(timer)){//翻牌完成
					isOpen=true;
					addCardInner();
					isOpening=false;
					//翻开其他牌
					for (int i = 0; i < cards.size(); i++) {
						Card c=cards.get(i);
						if(!c.isOpen){
							c.clearListeners();
							c.isOpening=true;
						}
					}
					if(isClick){
						//添加一个全部领取按钮
						allGetBtn=new ButtonEx(screen, screen.getTexture("res/allGetBtn.png"));
						allGetBtn.setPosition(186,-21);
						allGetBtn.addListener(Lottery.this);
						Lottery.this.addActor(allGetBtn);
						//全部领取提示
						Label label=new Label("购买全部卡片，仅需10元", new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
						label.setFontScale(0.6f);
						label.setPosition(480/2-label.getTextBounds().width/2,-40);
						Lottery.this.addActor(label);
					}
				}
			}
			bgImg.setDrawable(new TextureRegionDrawable(anim.getKeyFrame(timer)));
		}
		private void addCardInner() {
			//道具图片
			Image img=null;;
			System.out.println(type);
			switch (type) {
			case coin:
				img=new Image(screen.getTexture("res/coin2.png"));
				break;
			case speedUp:
				img=new Image(screen.getTexture("res/speedUpIcon.png"));
				break;
			case gun:
				img=new Image(screen.getTexture("res/gunIcon.png"));
				break;
			case motor:
				img=new Image(screen.getTexture("res/motorIcon.png"));
				break;
			case shield:
				img=new Image(screen.getTexture("res/shieldIcon.png"));
				break;
			case attract:
				img=new Image(screen.getTexture("res/attractIcon.png"));
				break;
			default:
				break;
			}
			
			img.setSize(60, 60);
			img.setPosition(10,26);
			this.addActor(img);
			//随机数量
			Label label=new Label("", new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
			label.setFontScale(0.6f);
			//你点的卡片很少，你没选的总是很多
			num=getRandomNum(type,isClick);
			label.setText(num+"");
			label.setPosition(40-label.getTextBounds().width/2,10);
			label.setTouchable(Touchable.disabled);
			this.addActor(label);
			if(isClick){//如果是点击的卡片，则获得这些道具
				get(type);
			}
		}
		private void get(CardType type) {
			if(num>0){
				switch (type) {
				case coin:
					InfoToast.show(screen, "恭喜获得"+num+"金币");
					screen.widget.addCoinNum(num);
					SPUtil.commit(screen.main.getSp(), "coinNum", StaticVariable.coinNum);
					break;
				case speedUp:
					InfoToast.show(screen, "恭喜获得"+num+"个加速道具");
					StaticVariable.speedUpCount+=num;
					SPUtil.commit(screen.main.getSp(), "speedUpCount", StaticVariable.speedUpCount);
					break;
				case gun:
					InfoToast.show(screen, "恭喜获得"+num+"个枪道具");
					StaticVariable.gunCount+=num;
					SPUtil.commit(screen.main.getSp(), "gunCount", StaticVariable.gunCount);
					break;
				case motor:
					InfoToast.show(screen, "恭喜获得"+num+"个摩托道具");
					StaticVariable.mountCount+=num;
					SPUtil.commit(screen.main.getSp(), "mountCount", StaticVariable.mountCount);
					break;
				case shield:
					InfoToast.show(screen, "恭喜获得"+num+"个保护道具");
					StaticVariable.shieldCount+=num;
					SPUtil.commit(screen.main.getSp(), "shieldCount", StaticVariable.shieldCount);
					break;
				case attract:
					InfoToast.show(screen, "恭喜获得"+num+"个吸金道具");
					StaticVariable.attractCount+=num;
					SPUtil.commit(screen.main.getSp(), "attractCount", StaticVariable.attractCount);
					break;
				default:
					break;
				}
			}else{
				InfoToast.show(screen, "很遗憾，运气不好");	
			}
		}
		
		
	}
	public CardType getRandomType(){
		switch (MathUtils.random(6)) {
		case 0:
			return CardType.speedUp;
		case 1:
			return CardType.gun;
		case 2:
			return CardType.motor;
		case 3:
			return CardType.shield;
		case 4:
			return CardType.attract;
		case 5:
			return CardType.coin;
		case 6:
			return CardType.coin;
		default:
			break;
		}
		return CardType.coin;
	}
	public int getRandomNum(CardType type,boolean isClick){
		switch (type) {
		case coin:
			if(isClick){
				return MathUtils.random(100, 2000);	
			}else{
				return MathUtils.random(10000, 30000);
			}
		case speedUp:
			if(isClick){
				return MathUtils.random(0,1);	
			}else{
				return MathUtils.random(5,10);
			}
		case gun:
			if(isClick){
				return MathUtils.random(0,5);	
			}else{
				return MathUtils.random(5,12);
			}
		case motor:
			if(isClick){
				return MathUtils.random(0,3);	
			}else{
				return MathUtils.random(4,8);
			}
		case shield:
			if(isClick){
				return MathUtils.random(0,4);	
			}else{
				return MathUtils.random(4,12);
			}
		case attract:
			if(isClick){
				return MathUtils.random(0,6);	
			}else{
				return MathUtils.random(6,15);
			}
		default:
			break;
		}
		return 0;
	}
	@Override
	public boolean touchDown(Actor actor) {
		SoundUtil.playSound(screen.main.getManager(), "button");
		return true;
	}
	@Override
	public void touchUp(Actor actor) {
		screen.control.releaseTouchUp();
		screen.setPay(PayType.getAllCard);
	}
}
