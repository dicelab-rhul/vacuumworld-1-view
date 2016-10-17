package uk.ac.rhul.cs.dice.vacuumworld.view.session.requests;

import uk.ac.rhul.cs.dice.vacuumworld.wvcommon.ViewRequest;

public interface ViewRequestsVisitorInterface {
	public abstract ViewRequest generate(NewRequest request);
	public abstract ViewRequest generate(LoadTemplateRequest request);
	public abstract ViewRequest generate(LoadTemplateFromFileRequest request);
	public abstract ViewRequest generate(GetStateRequest request);
	public abstract ViewRequest generate(MoveObstacleRequest request);
	public abstract ViewRequest generate(MoveAvatarRequest request);
	public abstract ViewRequest generate(StopControllerRequest request);
	public abstract ViewRequest generate(StopForwardRequest request);
}