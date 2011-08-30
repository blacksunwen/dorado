package com.bstek.dorado.view.widget.layout;

/**
 * 锚定布局条件。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jun 18, 2009
 */
public class AnchorLayoutConstraint extends LayoutConstraintSupport {
	private String left;
	private String top;
	private String right;
	private String bottom;
	private AnchorMode anchorLeft = AnchorMode.auto;
	private AnchorMode anchorTop = AnchorMode.auto;
	private AnchorMode anchorRight = AnchorMode.auto;
	private AnchorMode anchorBottom = AnchorMode.auto;

	/**
	 * 返回左边距。
	 */
	public String getLeft() {
		return left;
	}

	/**
	 * 设置左边距。
	 */
	public void setLeft(String left) {
		this.left = left;
	}

	/**
	 * 返回上边距。
	 */
	public String getTop() {
		return top;
	}

	/**
	 * 设置上边距。
	 */
	public void setTop(String top) {
		this.top = top;
	}

	/**
	 * @return the right
	 */
	public String getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(String right) {
		this.right = right;
	}

	/**
	 * @return the bottom
	 */
	public String getBottom() {
		return bottom;
	}

	/**
	 * @param bottom
	 *            the bottom to set
	 */
	public void setBottom(String bottom) {
		this.bottom = bottom;
	}

	public AnchorMode getAnchorLeft() {
		return anchorLeft;
	}

	public void setAnchorLeft(AnchorMode anchorLeft) {
		this.anchorLeft = anchorLeft;
	}

	public AnchorMode getAnchorTop() {
		return anchorTop;
	}

	public void setAnchorTop(AnchorMode anchorTop) {
		this.anchorTop = anchorTop;
	}

	public AnchorMode getAnchorRight() {
		return anchorRight;
	}

	public void setAnchorRight(AnchorMode anchorRight) {
		this.anchorRight = anchorRight;
	}

	public AnchorMode getAnchorBottom() {
		return anchorBottom;
	}

	public void setAnchorBottom(AnchorMode anchorBottom) {
		this.anchorBottom = anchorBottom;
	}
}
