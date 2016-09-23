package uk.ac.rhul.cs.dice.vacuumworld.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StateForView implements Serializable {
	private static final long serialVersionUID = 5675058262192956723L;
	private int width;
	private int height;
	private List<String> gridImagesPaths;
	
	public StateForView(int width, int height) {
		this.width = width;
		this.height = height;
		this.gridImagesPaths = new ArrayList<>();
	}
	
	public StateForView(int width, int height, List<String> imagesPaths) {
		this.width = width;
		this.height = height;
		this.gridImagesPaths = imagesPaths;
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

	public String[] getGridImagesPaths() {
		return this.gridImagesPaths.toArray(new String[this.gridImagesPaths.size()]);
	}
}