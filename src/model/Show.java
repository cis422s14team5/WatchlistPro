/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package watchlistpro.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Simple model class for the show table.
 * 
 * @author jasonjkeller
 */
public class Show {

	private final StringProperty showName;
	private final StringProperty watched;
//	private final StringProperty showName;

	public Show(String showName, String watched) {
		this.showName = new SimpleStringProperty(showName);
		this.watched = new SimpleStringProperty(watched);                
	}
	
	public String getShowName() {
		return showName.get();
	}

	public void setShowName(String showName) {
		this.showName.set(showName);
	}
	
	public StringProperty showNameProperty() {
		return showName;
	}
        
//        @Override
//	public String toString() {
//		return getShowName();
//	}
        
	public String getWatched() {
		return watched.get();
	}

	public void setWatched(String watched) {
		this.watched.set(watched);
	}
	
	public StringProperty watchedProperty() {
		return watched;
	}
}