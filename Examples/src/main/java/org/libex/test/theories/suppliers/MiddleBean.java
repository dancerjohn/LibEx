package org.libex.test.theories.suppliers;

import java.util.List;

public class MiddleBean {
	public static enum State {
		State1, State2, State3
	}

	private State state;
	private List<BottomBean> bottomBeans;

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public List<BottomBean> getBottomBeans() {
		return bottomBeans;
	}

	public void setBottomBeans(List<BottomBean> bottomBeans) {
		this.bottomBeans = bottomBeans;
	}
}
