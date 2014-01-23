package it.bioko.http.scenario;


import it.bioko.utils.domain.DomainEntity;
import it.bioko.utils.domain.EntityBuilder;


public class JSonRequestFactory<T extends DomainEntity> {

	private EntityBuilder<T> _entityBuilder= null;

	@SuppressWarnings("unchecked")
	public JSonRequestFactory<T> createEntity(Class<T> aClass) throws Exception {
		Object builder = Class.forName(aClass.getName() + "Builder").getConstructor().newInstance();

		_entityBuilder = (EntityBuilder<T>) builder;

		return this;
	}

	public JSonRequestFactory<T> setEntityParam(String builderMethodName, String parameterValue) throws Exception {
		_entityBuilder.set(builderMethodName, parameterValue);
		return this;
	}

	public JSonRequestFactory<T> setId(String parameterValue) throws Exception {
		_entityBuilder.setId(parameterValue);
		return this;
	}

	public String buildNewEntityAsJSON() {
		return buildNewEntity().toJSONString();

		//		DomainEntity input = buildNewEntity();
		//		String jsonString = input.toJSONString();
		//		String result = new String(jsonString.getBytes(), Charset.forName("UTF-8"));
		//		return result;
	}

	public T buildNewEntity() {
		T input = _entityBuilder.loadDefaultExample().build(false);
		return input;
	}

	public String buildExistingEntityAsJSON() {
		DomainEntity input = buildExistingEntity();
		return input.toJSONString();
	}

	public T buildExistingEntity() {
		T input = _entityBuilder.loadDefaultExample().build(true);
		return input;
	}
}