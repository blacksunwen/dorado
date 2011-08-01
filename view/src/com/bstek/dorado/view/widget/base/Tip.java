package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.FloatControl;
import com.bstek.dorado.view.widget.FloatControlAlign;
import com.bstek.dorado.view.widget.FloatControlAnimateType;
import com.bstek.dorado.view.widget.FloatControlShadowMode;
import com.bstek.dorado.view.widget.FloatControlVAlign;
import com.bstek.dorado.view.widget.ModalType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-15
 */
@Widget(name = "Tip", category = "Floatable", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.Tip", shortTypeName = "Tip")
@XmlNode(nodeName = "Tip")
public class Tip extends Control implements FloatControl {
	private FloatControlAnimateType animateType = FloatControlAnimateType.fade;
	private FloatControlAnimateType showAnimateType;
	private FloatControlAnimateType hideAnimateType;
	private String animateTarget;
	private String renderTo;
	private boolean center;
	private boolean modal;
	private ModalType modalType = ModalType.dark;
	private FloatControlShadowMode shadowMode = FloatControlShadowMode.drop;
	private boolean focusAfterShow = true;

	private String caption;
	private String text;
	private boolean closeable;
	private TipAnchor anchor;
	private boolean anchorOffset;
	private TipAnchorArrowAlign anchorArrowAlign = TipAnchorArrowAlign.center;
	private int showDuration;

	private boolean floating = true;
	private String floatingClassName;
	private int left;
	private int top;
	private int offsetLeft;
	private int offsetTop;
	private String anchorTarget;
	private FloatControlAlign align;
	private FloatControlVAlign vAlign;
	private boolean autoAdjustPosition = true;
	private boolean handleOverflow = true;
	
	public Tip() {
		setVisible(false);
	}

	@Override
	@ViewAttribute(defaultValue = "false")
	public boolean isVisible() {
		return super.isVisible();
	}

	@ViewAttribute(defaultValue = "fade")
	public FloatControlAnimateType getAnimateType() {
		return animateType;
	}

	public void setAnimateType(FloatControlAnimateType animateType) {
		this.animateType = animateType;
	}

	public FloatControlAnimateType getShowAnimateType() {
		return showAnimateType;
	}

	public void setShowAnimateType(FloatControlAnimateType showAnimateType) {
		this.showAnimateType = showAnimateType;
	}

	public FloatControlAnimateType getHideAnimateType() {
		return hideAnimateType;
	}

	public void setHideAnimateType(FloatControlAnimateType hideAnimateType) {
		this.hideAnimateType = hideAnimateType;
	}

	public String getAnimateTarget() {
		return animateTarget;
	}

	public void setAnimateTarget(String animateTarget) {
		this.animateTarget = animateTarget;
	}

	@Override
	public String getRenderTo() {
		return renderTo;
	}

	@Override
	public void setRenderTo(String renderTo) {
		this.renderTo = renderTo;
	}

	public boolean isCenter() {
		return center;
	}

	public void setCenter(boolean center) {
		this.center = center;
	}

	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	public ModalType getModalType() {
		return modalType;
	}

	public void setModalType(ModalType modalType) {
		this.modalType = modalType;
	}

	@ViewAttribute(defaultValue = "drop")
	public FloatControlShadowMode getShadowMode() {
		return shadowMode;
	}

	public void setShadowMode(FloatControlShadowMode shadowMode) {
		this.shadowMode = shadowMode;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isFocusAfterShow() {
		return focusAfterShow;
	}

	public void setFocusAfterShow(boolean focusAfterShow) {
		this.focusAfterShow = focusAfterShow;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isCloseable() {
		return closeable;
	}

	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}

	public TipAnchor getAnchor() {
		return anchor;
	}

	public void setAnchor(TipAnchor anchor) {
		this.anchor = anchor;
	}

	public boolean isAnchorOffset() {
		return anchorOffset;
	}

	public void setAnchorOffset(boolean anchorOffset) {
		this.anchorOffset = anchorOffset;
	}

	@ViewAttribute(defaultValue = "center")
	public TipAnchorArrowAlign getAnchorArrowAlign() {
		return anchorArrowAlign;
	}

	public void setAnchorArrowAlign(TipAnchorArrowAlign anchorArrowAlign) {
		this.anchorArrowAlign = anchorArrowAlign;
	}

	public int getShowDuration() {
		return showDuration;
	}

	public void setShowDuration(int showDuration) {
		this.showDuration = showDuration;
	}
	
	/**
	 * @return the floating
	 */
	@ViewAttribute(defaultValue = "true")
	public boolean isFloating() {
		return floating;
	}

	/**
	 * @param floating the floating to set
	 */
	public void setFloating(boolean floating) {
		this.floating = floating;
	}
	
	/**
	 * @return the floatingClassName
	 */
	public String getFloatingClassName() {
		return floatingClassName;
	}

	/**
	 * @param floatingClassName the floatingClassName to set
	 */
	public void setFloatingClassName(String floatingClassName) {
		this.floatingClassName = floatingClassName;
	}

	/**
	 * @return the left
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(int left) {
		this.left = left;
	}

	/**
	 * @return the top
	 */
	public int getTop() {
		return top;
	}

	/**
	 * @param top the top to set
	 */
	public void setTop(int top) {
		this.top = top;
	}

	/**
	 * @return the offsetLeft
	 */
	public int getOffsetLeft() {
		return offsetLeft;
	}

	/**
	 * @param offsetLeft the offsetLeft to set
	 */
	public void setOffsetLeft(int offsetLeft) {
		this.offsetLeft = offsetLeft;
	}

	/**
	 * @return the offsetTop
	 */
	public int getOffsetTop() {
		return offsetTop;
	}

	/**
	 * @param offsetTop the offsetTop to set
	 */
	public void setOffsetTop(int offsetTop) {
		this.offsetTop = offsetTop;
	}

	/**
	 * @return the anchorTarget
	 */
	public String getAnchorTarget() {
		return anchorTarget;
	}

	/**
	 * @param anchorTarget the anchorTarget to set
	 */
	public void setAnchorTarget(String anchorTarget) {
		this.anchorTarget = anchorTarget;
	}

	/**
	 * @return the align
	 */
	public FloatControlAlign getAlign() {
		return align;
	}

	/**
	 * @param align the align to set
	 */
	public void setAlign(FloatControlAlign align) {
		this.align = align;
	}

	/**
	 * @return the vAlign
	 */
	public FloatControlVAlign getvAlign() {
		return vAlign;
	}

	/**
	 * @param vAlign the vAlign to set
	 */
	public void setvAlign(FloatControlVAlign vAlign) {
		this.vAlign = vAlign;
	}

	/**
	 * @return the autoAdjustPosition
	 */
	@ViewAttribute(defaultValue = "true")
	public boolean isAutoAdjustPosition() {
		return autoAdjustPosition;
	}

	/**
	 * @param autoAdjustPosition the autoAdjustPosition to set
	 */
	public void setAutoAdjustPosition(boolean autoAdjustPosition) {
		this.autoAdjustPosition = autoAdjustPosition;
	}

	/**
	 * @return the handleOverflow
	 */
	@ViewAttribute(defaultValue = "true")
	public boolean isHandleOverflow() {
		return handleOverflow;
	}

	/**
	 * @param handleOverflow the handleOverflow to set
	 */
	public void setHandleOverflow(boolean handleOverflow) {
		this.handleOverflow = handleOverflow;
	}
	

}
