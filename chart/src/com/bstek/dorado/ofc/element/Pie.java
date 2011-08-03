/**
 * 
 */
package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.ofc.Element;
import com.bstek.dorado.ofc.binding.PieBindingConfig;

/**
 * @author bean
 *
 */
@ViewObject(prototype = "dorado.widget.ofc.Pie", shortTypeName = "ofc.Pie")
@XmlNode(nodeName = "Pie")
public class Pie extends Element{
	private Double alpha;
	private PieBindingConfig bindingConfig;
	private boolean animation;
	private Integer border;
	private boolean gradientFill;
	private String labelColor;
	private boolean noLabels;
	private Double radius;
	private Double startAngle;
	
	/**
	 * @return the alpha
	 */
	public Double getAlpha() {
		return alpha;
	}
	/**
	 * @param alpha the alpha to set
	 */
	public void setAlpha(Double alpha) {
		this.alpha = alpha;
	}
	/**
	 * @return the bindingConfig
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public PieBindingConfig getBindingConfig() {
		return bindingConfig;
	}
	/**
	 * @param bindingConfig the bindingConfig to set
	 */
	public void setBindingConfig(PieBindingConfig bindingConfig) {
		this.bindingConfig = bindingConfig;
	}
	/**
	 * @return the animation
	 */
	public boolean isAnimation() {
		return animation;
	}
	/**
	 * @param animation the animation to set
	 */
	public void setAnimation(boolean animation) {
		this.animation = animation;
	}
	/**
	 * @return the border
	 */
	public Integer getBorder() {
		return border;
	}
	/**
	 * @param border the border to set
	 */
	public void setBorder(Integer border) {
		this.border = border;
	}
	/**
	 * @return the gradientFill
	 */
	public boolean isGradientFill() {
		return gradientFill;
	}
	/**
	 * @param gradientFill the gradientFill to set
	 */
	public void setGradientFill(boolean gradientFill) {
		this.gradientFill = gradientFill;
	}
	/**
	 * @return the labelColor
	 */
	public String getLabelColor() {
		return labelColor;
	}
	/**
	 * @param labelColor the labelColor to set
	 */
	public void setLabelColor(String labelColor) {
		this.labelColor = labelColor;
	}
	/**
	 * @return the noLabels
	 */
	public boolean isNoLabels() {
		return noLabels;
	}
	/**
	 * @param noLabels the noLabels to set
	 */
	public void setNoLabels(boolean noLabels) {
		this.noLabels = noLabels;
	}
	/**
	 * @return the radius
	 */
	public Double getRadius() {
		return radius;
	}
	/**
	 * @param radius the radius to set
	 */
	public void setRadius(Double radius) {
		this.radius = radius;
	}
	/**
	 * @return the startAngle
	 */
	public Double getStartAngle() {
		return startAngle;
	}
	/**
	 * @param startAngle the startAngle to set
	 */
	public void setStartAngle(Double startAngle) {
		this.startAngle = startAngle;
	}
	
	
}
