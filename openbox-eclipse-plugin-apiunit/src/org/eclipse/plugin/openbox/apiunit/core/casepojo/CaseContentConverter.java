package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public final class CaseContentConverter {
	private static final Log logger = LogFactory
			.getLog(CaseContentConverter.class);

	public static Document toDocument(CaseContent caseContent) throws Exception {
		try {
			Element caseContentElement = DocumentHelper
					.createElement(CaseElementConstants.CASE);
			caseContentElement.addAttribute(CaseElementConstants.ID,
					caseContent.getId());

			Element nameElement = DocumentHelper
					.createElement(CaseElementConstants.NAME);
			nameElement.addText(caseContent.getName());
			caseContentElement.add(nameElement);

			Element classNameElement = DocumentHelper
					.createElement(CaseElementConstants.CLASS_NAME);
			classNameElement.addText(caseContent.getClassName());
			caseContentElement.add(classNameElement);

			Element methodNameElement = DocumentHelper
					.createElement(CaseElementConstants.METHOD_NAME);
			methodNameElement.addText(caseContent.getMethodName());
			caseContentElement.add(methodNameElement);

			Element apiSourceCodeElement = DocumentHelper
					.createElement(CaseElementConstants.SOURCE_CODE);
			String apiSourceCode = caseContent.getApiSourceCode();
			if (null != apiSourceCode) {
				CDATA cdata = DocumentHelper.createCDATA(apiSourceCode);
				apiSourceCodeElement.add(cdata);
			}
			caseContentElement.add(apiSourceCodeElement);

			Element descriptionElement = DocumentHelper
					.createElement(CaseElementConstants.DESCRIPTION);
			String description = caseContent.getDescription();
			if (null != description) {
				CDATA cdata = DocumentHelper.createCDATA(description);
				descriptionElement.add(cdata);
			}
			caseContentElement.add(descriptionElement);
			caseContentElement.add(toElement(caseContent.getApiClassData()));
			caseContentElement.add(toElement(caseContent.getApiInputParameters()));
			caseContentElement.add(toElement(caseContent.getApiCheckpoints()));
			return DocumentHelper.createDocument(caseContentElement);
		} catch (Exception e) {
			throw new Exception("convert case [" + caseContent.getName()
					+ "]'s content to case entity data failed !", e);
		}
	}

	public static InputStream toInputStream(CaseContent caseContent)
			throws Exception {
		Document document = toDocument(caseContent);
		XMLWriter output = null;
		Writer writer = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		try {
			output = new XMLWriter(writer, format);
			output.write(document);
			output.flush();
			writer.flush();
		} catch (IOException e) {
			String msg = "covert the case content document object to xml string stream failed !";
			logger.error(msg, e);
			throw new Exception(msg, e);
		}finally{
			if(null != output){
				output.close();
			}
		}
		return new ByteArrayInputStream(writer.toString().getBytes());
	}

	private static Element toConstructorElement(
			ApiInputParameter apiInputParamater) {
		Element constructorElement = DocumentHelper
				.createElement(CaseElementConstants.API_CURRENT_CLASS_CONSTRUCTOR);
		constructorElement.add(toElement(apiInputParamater));
		return constructorElement;
	}

	private static Element toElement(ApiInputParameters apiInputParameters) {
		int num = apiInputParameters.getNum();
		Element apiInputParametersElement = DocumentHelper
				.createElement(CaseElementConstants.API_INPUT_PARAMETERS);
		apiInputParametersElement.addAttribute(CaseElementConstants.NUM,
				String.valueOf(num));

		for (int i = 0; i < num; i++) {
			ApiInputParameter param = apiInputParameters.getParameter(i);
			apiInputParametersElement.add(toElement(param));
		}

		return apiInputParametersElement;
	}

	private static Element toElement(ApiInputParameter apiInputParamater) {
		Element apiInputParamaterElement = DocumentHelper
				.createElement(CaseElementConstants.API_INPUT_PARAMETER);
		apiInputParamaterElement.addAttribute(CaseElementConstants.INDEX,
				String.valueOf(apiInputParamater.getIndex()));

		Element typeIdElement = DocumentHelper
				.createElement(CaseElementConstants.TYPE_ID);
		typeIdElement.addText(apiInputParamater.getTypeId());
		apiInputParamaterElement.add(typeIdElement);

		boolean isBasicType = apiInputParamater.isBasicType();
		Element isBasicTypeElement = DocumentHelper
				.createElement(CaseElementConstants.IS_BASIC_TYPE);
		isBasicTypeElement.addText(String.valueOf(isBasicType));
		apiInputParamaterElement.add(isBasicTypeElement);

		if (isBasicType) {
			apiInputParamaterElement.add(toElement(apiInputParamater
					.getBaseTypeInputParameter()));
		} else {
			apiInputParamaterElement.add(toElement(apiInputParamater
					.getNonBasicTypeInputParameters()));
			ReturnObject returnObject = apiInputParamater.getReturnObject();
			if (null != returnObject) {
				apiInputParamaterElement.add(toElement(returnObject));
			}
		}

		return apiInputParamaterElement;
	}

	private static Element toElement(NameValuePair nameValuePair) {
		Element valueElement = DocumentHelper
				.createElement(CaseElementConstants.VALUE);
		valueElement.addAttribute(CaseElementConstants.NAME,
				nameValuePair.getName());
		valueElement.addText(nameValuePair.getValue());

		return valueElement;
	}

	private static Element toElement(ApiCheckpoints checkpoints) {
		Element checkpointsElement = DocumentHelper
				.createElement(CaseElementConstants.CHECKPOINTS);
		List<ApiCheckpoint> checkpointList = checkpoints.getCheckpoints();

		for (ApiCheckpoint checkpoint : checkpointList)
			checkpointsElement.add(toElement(checkpoint));

		return checkpointsElement;
	}

	private static Element toElement(ApiCheckpoint checkpoint) {
		Element checkpointElement = DocumentHelper
				.createElement(CaseElementConstants.CHECKPOINT);
		checkpointElement.addAttribute(CaseElementConstants.INDEX,
				String.valueOf(checkpoint.getIndex()));

		CheckpointMode mode = checkpoint.getMode();
		Element modeElement = DocumentHelper
				.createElement(CaseElementConstants.MODE);
		modeElement.addText(String.valueOf(mode.toValue()));
		checkpointElement.add(modeElement);

		switch (mode) {
		case RETURN_OBJECT_MODE:
			ReturnObject returnObject = checkpoint.getReturnObject();
			if (null != returnObject) {
				checkpointElement.add(toElement(returnObject));
			}
			break;
		case CHECKPOINT_API_WITHOUTINPUT_MODE:
			checkpointElement.add(toElement(checkpoint.getCheckpointApiInfo()));
			break;
		case CHECKPOINT_API_WITHINPUT_MODE:
			checkpointElement.add(toElement(checkpoint.getCheckpointApiInfo()));
			break;
		default:
			break;
		}
		return checkpointElement;
	}

	private static Element toElement(CheckpointApiInfo checkpintApiInfo) {
		Element checkpintApiInfoElement = DocumentHelper
				.createElement(CaseElementConstants.CHECK_API);
		checkpintApiInfoElement.addText(checkpintApiInfo.getApiId());

		return checkpintApiInfoElement;
	}

	private static Element toElement(ReturnObject returnObject) {
		Element returnObjectElement = DocumentHelper
				.createElement(CaseElementConstants.EXPECTED_RETURN_OBJECT);

		Element typeIdElement = DocumentHelper
				.createElement(CaseElementConstants.TYPE_ID);
		typeIdElement.addText(returnObject.getTypeId());
		returnObjectElement.add(typeIdElement);
		returnObjectElement.add(toConstructorElement(returnObject.getConstructor()));
		returnObjectElement.add(toElement(returnObject.getFields()));

		return returnObjectElement;
	}

	private static Element toElement(Fields fields) {
		Element fieldsElement = DocumentHelper
				.createElement(CaseElementConstants.FIELDS);
		fieldsElement.addAttribute(CaseElementConstants.NUM,
				String.valueOf(fields.getNum()));

		for (InternField field : fields.getFields())
			fieldsElement.add(toElement(field));

		return fieldsElement;

	}

	private static Element toElement(InternField field) {
		Element fieldsElement = DocumentHelper
				.createElement(CaseElementConstants.FIELD);
		fieldsElement.addAttribute(CaseElementConstants.INDEX,
				String.valueOf(field.getIndex()));

		Element nameElement = DocumentHelper
				.createElement(CaseElementConstants.NAME);
		nameElement.addText(field.getFieldName());
		fieldsElement.add(nameElement);
		
		Element typeIdElement = DocumentHelper
				.createElement(CaseElementConstants.TYPE_ID);
		typeIdElement.addText(field.getTypeId());
		fieldsElement.add(typeIdElement);

		boolean isBasicType = field.isBasicType();
		Element isBasicTypeElement = DocumentHelper
				.createElement(CaseElementConstants.IS_BASIC_TYPE);
		isBasicTypeElement.addText(String.valueOf(isBasicType));
		fieldsElement.add(isBasicTypeElement);

		if (isBasicType) {
			fieldsElement.add(toElement(field.getBaseasicTypeReturnObject()));
		} else {
			ReturnObject returnObject = field.getNonBasicTypeReturnObject();
			if (null != returnObject) {
				fieldsElement.add(toElement(returnObject));
			}
		}
		return fieldsElement;
	}
}
