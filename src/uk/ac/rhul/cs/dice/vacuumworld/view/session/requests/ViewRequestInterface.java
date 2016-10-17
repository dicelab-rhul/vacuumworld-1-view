package uk.ac.rhul.cs.dice.vacuumworld.view.session.requests;

import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequest;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequestsEnum;

public interface ViewRequestInterface {
	public abstract ViewRequest accept(ViewRequestsVisitorInterface visitor);
	public ViewRequestsEnum getCode();
	public abstract Object getData();
}