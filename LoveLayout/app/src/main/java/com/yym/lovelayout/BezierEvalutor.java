package com.yym.lovelayout;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * @author mikyou
 * 自定义估值器
 * */
public class BezierEvalutor implements TypeEvaluator<PointF> {
	PointF p1;
	PointF p2;
	public BezierEvalutor(PointF p1, PointF p2) {
		super();
		this.p1 = p1;
		this.p2 = p2;
	}
	@Override
	public PointF evaluate(float t, PointF p0, PointF p3) {
		//时间因子t: 0~1
		PointF point=new PointF();
		//实现贝塞尔公式:
		point.x=p0.x*(1-t)*(1-t)*(1-t)+3*p1.x*t*(1-t)*(1-t)+3*p2.x*(1-t)*t*t+p3.x*t*t*t;//实时计算最新的点X坐标
		point.y=p0.y*(1-t)*(1-t)*(1-t)+3*p1.y*t*(1-t)*(1-t)+3*p2.y*(1-t)*t*t+p3.y*t*t*t;//实时计算最新的点Y坐标
		return point;//实时返回最新计算出的点对象
	}

}
