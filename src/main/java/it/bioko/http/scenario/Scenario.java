package it.bioko.http.scenario;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

public class Scenario {

	private String _scenarioName;
	private ArrayList<Entry<String, ScenarioStep>> _scenarioSteps;

	public Scenario(String scenarioName) {
		_scenarioName = scenarioName;
		_scenarioSteps = new ArrayList<Entry<String,ScenarioStep>>();
	}

	public void addScenarioStep(String aScenarioStepName, ScenarioStep aScenarioStep) {
		_scenarioSteps.add(new SimpleEntry<String, ScenarioStep>(aScenarioStepName, aScenarioStep));
	}
	
	public void addScenario(Scenario aScenario) {
		_scenarioSteps.addAll(aScenario._scenarioSteps);
	}
	
	public ArrayList<Entry<String, ScenarioStep>> scenarioSteps() {
		return _scenarioSteps;
	}
	
	public String scenarioName() {
		return _scenarioName;
	}
}