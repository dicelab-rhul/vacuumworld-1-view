package uk.ac.rhul.cs.dice.vacuumworld.view.session.requests;

import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequestsEnum;

public abstract class AbstractViewRequest implements ViewRequestInterface {
	private ViewRequestsEnum code;
	private Object data;
	
	public AbstractViewRequest(ViewRequestsEnum code, Object data) {
		this.code = code;
		this.data = data;
	}
	
	@Override
	public ViewRequestsEnum getCode() {
		return this.code;
	}
	
	@Override
	public Object getData() {
		return this.data;
	}
}