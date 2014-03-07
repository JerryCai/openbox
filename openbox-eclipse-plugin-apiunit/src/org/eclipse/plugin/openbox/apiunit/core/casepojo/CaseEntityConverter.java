package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

public final class CaseEntityConverter {

	public static CaseContent toCaseContent(Document caseEntity)
			throws Exception {
		if (null == caseEntity) {
			throw new Exception(
					"the caseEntity document is null, can't covert to case content");
		}
		try {
			Element caseElement = caseEntity.getRootElement();
			String caseId = caseElement.attributeValue(CaseElementConstants.ID);

			String className = caseElement
					.elementTextTrim(CaseElementConstants.CLASS_NAME);
			String methodName = caseElement
					.elementTextTrim(CaseElementConstants.METHOD_NAME);
			String caseName = caseElement
					.elementTextTrim(CaseElementConstants.NAME);
			String apiSourceCode = caseElement
					.elementText(CaseElementConstants.SOURCE_CODE);
			String description = caseElement
					.elementText(CaseElementConstants.DESCRIPTION);

			Element apiClassDatElement = caseElement
					.element(CaseElementConstants.EXPECTED_RETURN_OBJECT);
			ReturnObject apiClassData = toReturnObject(apiClassDatElement);

			Element paramsElement = caseElement
					.element(CaseElementConstants.API_INPUT_PARAMETERS);
			ApiInputParameters apiInputParameters = toApiInputParameters(paramsElement);

			Element apiCheckpointsElement = caseElement
					.element(CaseElementConstants.CHECKPOINTS);
			ApiCheckpoints apiCheckpoints = toApiCheckpoints(apiCheckpointsElement);

			CaseContent caseContent = new CaseContent(className, methodName,
					caseId, caseName);
			caseContent.setApiSourceCode(apiSourceCode);
			caseContent.setDescription(description);
			caseContent.setApiClassData(apiClassData);
			caseContent.setApiInputParameters(apiInputParameters);
			caseContent.setApiCheckpoints(apiCheckpoints);

			return caseContent;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"covert the caseEntity document to case content encouter error !",
					e);
		}
	}

	private static ApiInputParameter toConstructorParameters(
			Element constructorElement) {
		Element paramElement = constructorElement
				.element(CaseElementConstants.API_INPUT_PARAMETER);
		if (null == paramElement)
			return null;
		return toApiInputParameter(paramElement);
	}

	private static ApiInputParameters toApiInputParameters(Element paramsElement) {
		int num = Integer.parseInt(paramsElement
				.attributeValue(CaseElementConstants.NUM));
		ApiInputParameters apiInputParameters = new ApiInputParameters(num);

		@SuppressWarnings("unchecked")
		List<Element> paramElements = paramsElement
				.elements(CaseElementConstants.API_INPUT_PARAMETER);
		for (int i = 0; i < num; i++) {
			Element paramElement = paramElements.get(i);
			ApiInputParameter param = toApiInputParameter(paramElement);
			apiInputParameters.setParameter(param);
		}
		return apiInputParameters;
	}

	private static ApiInputParameter toApiInputParameter(Element paramElement) {
		int index = Integer.parseInt(paramElement
				.attributeValue(CaseElementConstants.INDEX));
		ApiInputParameter param = new ApiInputParameter(index);

		String typeId = paramElement.elementText(CaseElementConstants.TYPE_ID);
		param.setTypeId(typeId);
		boolean isBasicType = Boolean.parseBoolean(paramElement
				.elementTextTrim(CaseElementConstants.IS_BASIC_TYPE));
		param.setBasicType(isBasicType);
		if (isBasicType) {
			Element nameValuePairElement = paramElement
					.element(CaseElementConstants.VALUE);
			NameValuePair baseTypeInputParameter = toNameValuePair(nameValuePairElement);
			baseTypeInputParameter.setTypeIndex(typeId);
			param.setBaseTypeInputParameter(baseTypeInputParameter);
		} else {
			Element paramsElement = paramElement
					.element(CaseElementConstants.API_INPUT_PARAMETERS);
			ApiInputParameters nonBasicTypeInputParameters = toApiInputParameters(paramsElement);
			param.setNonBasicTypeInputParameters(nonBasicTypeInputParameters);

			Element objectElement = paramElement
					.element(CaseElementConstants.EXPECTED_RETURN_OBJECT);
			if (null != objectElement) {
				ReturnObject returnObject = toReturnObject(objectElement);
				param.setReturnObject(returnObject);
			}
		}
		return param;
	}

	private static ReturnObject toReturnObject(Element objectElement) {

		String typeId = objectElement
				.elementTextTrim(CaseElementConstants.TYPE_ID);
		ReturnObject object = new ReturnObject(typeId);

		Element constructorElement = objectElement
				.element(CaseElementConstants.API_CURRENT_CLASS_CONSTRUCTOR);
		ApiInputParameter constructor = toConstructorParameters(constructorElement);
		object.setConstructor(constructor);

		Element fieldsElement = objectElement
				.element(CaseElementConstants.FIELDS);
		Fields fields = toFields(fieldsElement);
		object.setFields(fields);
		return object;
	}

	private static Fields toFields(Element fieldsElement) {
		int num = Integer.parseInt(fieldsElement
				.attributeValue(CaseElementConstants.NUM));
		Fields fields = new Fields(num);
		@SuppressWarnings("unchecked")
		List<Element> fieldElements = fieldsElement
				.elements(CaseElementConstants.FIELD);
		for (int i = 0; i < num; i++) {
			Element fieldElement = fieldElements.get(i);
			InternField field = toInternField(fieldElement);
			fields.setField(field);

		}
		return fields;
	}

	private static InternField toInternField(Element fieldElement) {

		String typeId = fieldElement
				.elementTextTrim(CaseElementConstants.TYPE_ID);
		String name = fieldElement.elementTextTrim(CaseElementConstants.NAME);
		InternField field = new InternField(name,typeId);
		int index = Integer.parseInt(fieldElement
				.attributeValue(CaseElementConstants.INDEX));
		field.setIndex(index);
		boolean isBasicType = Boolean.parseBoolean(fieldElement
				.elementTextTrim(CaseElementConstants.IS_BASIC_TYPE));
		field.setBasicType(isBasicType);
		if (isBasicType) {
			Element nameValuePairElement = fieldElement
					.element(CaseElementConstants.VALUE);
			NameValuePair baseasicTypeReturnObject = toNameValuePair(nameValuePairElement);
			baseasicTypeReturnObject.setTypeIndex(typeId);
			field.setBaseasicTypeReturnObject(baseasicTypeReturnObject);
		} else {
			Element objectElement = fieldElement
					.element(CaseElementConstants.EXPECTED_RETURN_OBJECT);
			if (null != objectElement) {
				ReturnObject nonBasicTypeReturnObject = toReturnObject(objectElement);
				field.setNonBasicTypeReturnObject(nonBasicTypeReturnObject);
			}
		}
		return field;
	}

	private static NameValuePair toNameValuePair(Element nameValuePairElement) {
		String name = nameValuePairElement
				.attributeValue(CaseElementConstants.NAME);
		String value = nameValuePairElement.getTextTrim();
		NameValuePair nameValuePair = new NameValuePair(name);
		nameValuePair.setValue(value);
		return nameValuePair;
	}

	private static ApiCheckpoints toApiCheckpoints(Element apiCheckpointsElement) {
		ApiCheckpoints apiCheckpoints = new ApiCheckpoints();
		@SuppressWarnings("unchecked")
		List<Element> checkpointElements = apiCheckpointsElement
				.elements(CaseElementConstants.CHECKPOINT);
		for (int i = 0; i < checkpointElements.size(); i++) {
			Element checkpointElement = checkpointElements.get(i);
			int index = Integer.parseInt(checkpointElement
					.attributeValue(CaseElementConstants.INDEX));
			String modeValue = checkpointElement
					.elementTextTrim(CaseElementConstants.MODE);
			CheckpointMode checkpointMode = CheckpointMode.RETURN_OBJECT_MODE;
			if(null != modeValue){
				checkpointMode = CheckpointMode.parseMode(modeValue);
			}
			ApiCheckpoint apiCheckpoint = new ApiCheckpoint(index);
			Element returnObjectElement = checkpointElement
					.element(CaseElementConstants.EXPECTED_RETURN_OBJECT);
			ReturnObject returnObject = toReturnObject(returnObjectElement);
			apiCheckpoint.setReturnObject(returnObject);
			apiCheckpoint.setMode(checkpointMode);
			apiCheckpoints.appendApiCheckpoint(apiCheckpoint);
		}
		return apiCheckpoints;
	}
}
