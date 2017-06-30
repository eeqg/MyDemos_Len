package com.example.mydemos_len.activitys;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.mydemos_len.R;

public class PropertyAnimationActivity extends Activity {
	
	private ImageView mImage1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property_animation);
		
		initAnimationProperty();
		
		mImage1 = (ImageView) findViewById(R.id.propAnim_imageView1);
		findViewById(R.id.propAnim_button1).setOnClickListener(new OnClickListener() {
			AnimatorSet animator = null;
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//mImage1.setTranslationX(200);
				if (animator == null) {
					animator = (AnimatorSet) AnimatorInflater
							.loadAnimator(PropertyAnimationActivity.this, R.animator.set_rotate_scale);
					animator.setTarget(mImage1);
					animator.setDuration(1000);
					//animator.setInterpolator(new BounceInterpolator());//设置end时的弹跳插入器  
					//animator.setInterpolator(new AccelerateDecelerateInterpolator());// 
				}
				if (animator.isStarted()) {
					//animator.cancel();
					if (animator.isPaused()) {
						animator.resume();
					} else {
						animator.pause();
					}
				} else {
					animator.start();
				}
			}
		});
		
		findViewById(R.id.propAnim_button2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mImage1.setAlpha(0f);
				mImage1.animate()
						.alpha(1f)
						//				.setInterpolator(AnimationUtils.loadInterpolator(PropertyAnimationActivity.this, android.R.interpolator.linear_out_slow_in))
						.setDuration(1000)
				;
				
				//				mImage1.animate()
				//				.rotationX(180).scaleX(2).translationX(100).setDuration(5000);
			}
		});
		
	}
	
	private void initAnimationProperty() {
		// TODO Auto-generated method stub
		final View disView = findViewById(R.id.propAnim_display);
		
		findViewById(R.id.propAnim_alpha).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// alpha
				ObjectAnimator animator_alpha = ObjectAnimator.ofFloat(disView, "alpha", 1f, 0f, 1f);
				animator_alpha.setDuration(3000)
						.start();
			}
		});
		
		findViewById(R.id.propAnim_rotation).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// rotation
				ObjectAnimator animator_rotation = ObjectAnimator.ofFloat(disView, "rotation", 0f, 360f);
				//animator_rotation.setInterpolator(new LinearInterpolator());//匀速
				//animator_rotation.setRepeatCount(Animation.INFINITE); //Repeat the animation indefinitely.
				animator_rotation.setDuration(3000)
						.start();
			}
		});
		
		findViewById(R.id.propAnim_translaion).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// translation
				float currentTranslationX = disView.getTranslationX();
				ObjectAnimator animator_move = ObjectAnimator.ofFloat(disView, "translationX", currentTranslationX, -300f, currentTranslationX);
				animator_move.setDuration(3000)
						.start();
			}
		});
		
		findViewById(R.id.propAnim_scale).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// scale
				ObjectAnimator animator_scale = ObjectAnimator.ofFloat(disView, "scaleY", 1f, 3f, 1f);
				animator_scale.setDuration(3000)
						.start();
			}
		});
		
		findViewById(R.id.propAnim_set).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// set
				ObjectAnimator animator_alpha = ObjectAnimator.ofFloat(disView, "alpha", 1f, 0f, 1f);
				ObjectAnimator animator_rotation = ObjectAnimator.ofFloat(disView, "rotation", 0f, 360f);
				ObjectAnimator animator_move = ObjectAnimator.ofFloat(disView, "translationX", -300f, 0);
				ObjectAnimator animator_scale = ObjectAnimator.ofFloat(disView, "scaleY", 1f, 3f, 1f);
				AnimatorSet animatorSet = new AnimatorSet();
				animatorSet.play(animator_alpha)
						.before(animator_scale)
						.with(animator_rotation)
						.after(animator_move);
				animatorSet.setDuration(3000).start();
			}
		});
		
		
		// - - - - -- - - - --  -- - - -- - - - - -
		
		final View avatar = findViewById(R.id.avatar);
		
		{
			// rotation2
			final ObjectAnimator animator_rotation2 = ObjectAnimator.ofFloat(avatar, "rotation", 0f, 3600f);
			animator_rotation2.setInterpolator(new LinearInterpolator());//匀速
			animator_rotation2.setRepeatCount(Animation.INFINITE); //Repeat the animation indefinitely.
			animator_rotation2.setDuration(6000);
			
			findViewById(R.id.propAnim_rotation2).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(animator_rotation2.isRunning()){
						animator_rotation2.end();
						return;
					}
					animator_rotation2.start();
				}
			});
		}
		
		
		{
			//rotation_Y
			ObjectAnimator flipAnimator = ObjectAnimator.ofFloat(avatar, View.ROTATION_Y, 0, 90);
			flipAnimator.setRepeatMode(ValueAnimator.REVERSE);
			flipAnimator.setRepeatCount(ValueAnimator.INFINITE);
			
			ObjectAnimator recoveryAnimator = ObjectAnimator.ofFloat(avatar, View.ROTATION_Y, 90, 0);
			recoveryAnimator.setRepeatMode(ValueAnimator.REVERSE);
			recoveryAnimator.setRepeatCount(ValueAnimator.INFINITE);
			
			final AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.setDuration(400);
			animatorSet.setInterpolator(new LinearInterpolator());
			animatorSet.play(flipAnimator).before(recoveryAnimator);
			
			findViewById(R.id.propAnim_rotation_y).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (animatorSet.isRunning()) {
						animatorSet.end();
						return;
					}
					animatorSet.start();
				}
			});
		}
		
		
		findViewById(R.id.propAnim_clear).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Animation animation = disView.getAnimation();
				// android.util.Log.d("test_wp", "----clear()--animation="+animation);
				// if (animation != null) {
				// 	animation.cancel();
				// }
				// disView.clearAnimation();
				
				avatar.animate().cancel();
			}
		});
	}
	
	
}
