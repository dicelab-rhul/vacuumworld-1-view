package uk.ac.rhul.cs.dice.vacuumworld.view.session.requests;

import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequest;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequestsEnum;

public class MoveAvatarRequest extends AbstractViewRequest {

	public MoveAvatarRequest(ViewRequestsEnum code, Object data) {
		super(code, data);
	}

	@Override
	public ViewRequest accept(ViewRequestsVisitorInterface visitor) {
		return visitor.generate(this);
	}
}