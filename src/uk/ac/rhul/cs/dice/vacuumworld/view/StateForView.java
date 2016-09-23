package uk.ac.rhul.cs.dice.vacuumworld.view;

import java.util.ArrayList;
import java.util.List;

public class StateForView {
	private int width;
	private int height;
	private List<String> gridImagesPaths;
	
	public StateForView(int width, int height) {
		this.width = width;
		this.height = height;
		this.gridImagesPaths = new ArrayList<>();
	}
	
	public void addImage(String imagePath) {
		this.gridImagesPaths.add(imagePath);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public List<String> getGridImagesPaths() {
		return this.gridImagesPaths;
	}
}