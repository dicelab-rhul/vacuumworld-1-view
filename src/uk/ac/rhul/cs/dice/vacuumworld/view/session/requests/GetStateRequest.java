package uk.ac.rhul.cs.dice.vacuumworld.view.session.requests;

import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequest;
import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequestsEnum;

public class GetStateRequest extends AbstractViewRequest {

	public GetStateRequest(ViewRequestsEnum code, Object data) {
		super(code, data);
	}

	@Override
	public ViewRequest accept(ViewRequestsVisitorInterface visitor) {
		return visitor.generate(this);
	}
}