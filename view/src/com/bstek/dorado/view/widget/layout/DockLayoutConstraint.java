package com.bstek.dorado.view.widget.layout;

/**
 * Border型布局的布局条件。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 5, 2008
 */
public class DockLayoutConstraint extends LayoutConstraintSupport {
	private DockMode type = DockMode.center;

	/**
	 * 设置该布局条件代表的区域。
	 * 
	 * @param region
	 *            区域
	 */
	public void setType(DockMode type) {
		this.type = type;
	}

	/**
	 * 返回该布局条件代表的区域。
	 */
	public DockMode getType() {
		return type;
	}
}
