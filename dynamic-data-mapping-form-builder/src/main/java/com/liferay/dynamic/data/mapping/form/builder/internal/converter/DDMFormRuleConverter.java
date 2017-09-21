/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.mapping.form.builder.internal.converter;

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionException;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.model.Expression;
import com.liferay.dynamic.data.mapping.form.builder.internal.converter.model.DDMFormRule;
import com.liferay.dynamic.data.mapping.form.builder.internal.converter.model.DDMFormRuleAction;
import com.liferay.dynamic.data.mapping.form.builder.internal.converter.model.DDMFormRuleCondition;
import com.liferay.dynamic.data.mapping.form.builder.internal.converter.serializer.DDMFormRuleSerializerContext;
import com.liferay.dynamic.data.mapping.form.builder.internal.converter.visitor.ActionExpressionVisitor;
import com.liferay.dynamic.data.mapping.form.builder.internal.converter.visitor.ConditionExpressionVisitor;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = DDMFormRuleConverter.class)
public class DDMFormRuleConverter {

	public List<DDMFormRule> convert(
		List<com.liferay.dynamic.data.mapping.model.DDMFormRule> ddmFormRules) {

		List<DDMFormRule> ddlFormRules = new ArrayList<>();

		for (com.liferay.dynamic.data.mapping.model.DDMFormRule ddmFormRule :
				ddmFormRules) {

			ddlFormRules.add(convertRule(ddmFormRule));
		}

		return ddlFormRules;
	}

	public List<com.liferay.dynamic.data.mapping.model.DDMFormRule> convert(
		List<DDMFormRule> ddlFormRules,
		DDMFormRuleSerializerContext ddlFormRuleSerializerContext) {

		Stream<DDMFormRule> stream = ddlFormRules.stream();

		Stream<com.liferay.dynamic.data.mapping.model.DDMFormRule>
			convertedFormRulesStream = stream.map(
				formRule -> convertRule(
					formRule, ddlFormRuleSerializerContext));

		return convertedFormRulesStream.collect(Collectors.toList());
	}

	protected DDMFormRuleAction convertAction(String actionExpressionString) {
		Expression actionExpression = createExpression(actionExpressionString);

		ActionExpressionVisitor actionExpressionVisitor =
			new ActionExpressionVisitor();

		return (DDMFormRuleAction)actionExpression.accept(
			actionExpressionVisitor);
	}

	protected String convertCondition(
		DDMFormRuleCondition ddlFormRuleCondition) {

		String operator = ddlFormRuleCondition.getOperator();

		String functionName = _operatorFunctionNameMap.get(operator);

		List<DDMFormRuleCondition.Operand> operands =
			ddlFormRuleCondition.getOperands();

		if (functionName == null) {
			return String.format(
				_comparisonExpressionFormat, convertOperand(operands.get(0)),
				_operatorMap.get(operator), convertOperand(operands.get(1)));
		}

		String condition = createCondition(functionName, operands);

		if (operator.startsWith("not")) {
			return String.format(_notExpressionFormat, condition);
		}

		return condition;
	}

	protected String convertConditions(
		String logicalOperator,
		List<DDMFormRuleCondition> ddlFormRuleConditions) {

		if (ddlFormRuleConditions.size() == 1) {
			return convertCondition(ddlFormRuleConditions.get(0));
		}

		StringBundler sb = new StringBundler(ddlFormRuleConditions.size() * 4);

		for (DDMFormRuleCondition ddlFormRuleCondition :
				ddlFormRuleConditions) {

			sb.append(convertCondition(ddlFormRuleCondition));
			sb.append(StringPool.SPACE);
			sb.append(logicalOperator);
			sb.append(StringPool.SPACE);
		}

		sb.setIndex(sb.index() - 3);

		return sb.toString();
	}

	protected String convertOperand(DDMFormRuleCondition.Operand operand) {
		if (Objects.equals("field", operand.getType())) {
			return String.format(
				_functionCallUnaryExpressionFormat, "getValue",
				StringUtil.quote(operand.getValue()));
		}

		String value = operand.getValue();

		if (isNumericConstant(operand.getType())) {
			return value;
		}

		String[] values = StringUtil.split(value);

		UnaryOperator<String> quoteOperation = StringUtil::quote;
		UnaryOperator<String> trimOperation = StringUtil::trim;

		Stream<String> valuesStream = Stream.of(values);

		Stream<String> valueStream = valuesStream.map(
			trimOperation.andThen(quoteOperation));

		return valueStream.collect(
			Collectors.joining(StringPool.COMMA_AND_SPACE));
	}

	protected String convertOperands(
		List<DDMFormRuleCondition.Operand> operands) {

		StringBundler sb = new StringBundler(operands.size());

		for (DDMFormRuleCondition.Operand operand : operands) {
			sb.append(convertOperand(operand));
			sb.append(StringPool.COMMA_AND_SPACE);
		}

		sb.setIndex(sb.index() - 1);

		return sb.toString();
	}

	protected DDMFormRule convertRule(
		com.liferay.dynamic.data.mapping.model.DDMFormRule ddmFormRule) {

		DDMFormRule ddlFormRule = new DDMFormRule();

		setDDLFormRuleConditions(ddlFormRule, ddmFormRule.getCondition());
		setDDLFormRuleActions(ddlFormRule, ddmFormRule.getActions());

		return ddlFormRule;
	}

	protected com.liferay.dynamic.data.mapping.model.DDMFormRule convertRule(
		DDMFormRule ddmFormRule,
		DDMFormRuleSerializerContext ddlFormRuleSerializerContext) {

		String condition = convertConditions(
			ddmFormRule.getLogicalOperator(),
			ddmFormRule.getDDLFormRuleConditions());

		List<String> actions = new ArrayList<>();

		for (DDMFormRuleAction ddlFormRuleAction :
				ddmFormRule.getDDLFormRuleActions()) {

			actions.add(
				ddlFormRuleAction.serialize(ddlFormRuleSerializerContext));
		}

		return new com.liferay.dynamic.data.mapping.model.DDMFormRule(
			condition, actions);
	}

	protected String createCondition(
		String functionName, List<DDMFormRuleCondition.Operand> operands) {

		if (Objects.equals(functionName, "belongsTo")) {
			operands.remove(0);
		}

		return String.format(
			_functionCallUnaryExpressionFormat, functionName,
			convertOperands(operands));
	}

	protected Expression createExpression(String expressionString) {
		try {
			DDMExpression<Boolean> ddmExpression =
				ddmExpressionFactory.createBooleanDDMExpression(
					expressionString);

			return ddmExpression.getModel();
		}
		catch (DDMExpressionException ddmee) {
			throw new IllegalStateException(
				String.format(
					"Unable to parse expression \"%s\"", expressionString),
				ddmee);
		}
	}

	protected boolean isNumericConstant(String operandType) {
		if (operandType.equals("integer") || operandType.equals("double")) {
			return true;
		}

		return false;
	}

	protected void setDDLFormRuleActions(
		DDMFormRule ddlFormRule, List<String> actions) {

		List<DDMFormRuleAction> ddlFormRuleActions = new ArrayList<>();

		for (String action : actions) {
			ddlFormRuleActions.add(convertAction(action));
		}

		ddlFormRule.setDDLFormRuleActions(ddlFormRuleActions);
	}

	protected void setDDLFormRuleConditions(
		DDMFormRule ddlFormRule, String conditionExpressionString) {

		Expression conditionExpression = createExpression(
			conditionExpressionString);

		ConditionExpressionVisitor conditionExpressionVisitor =
			new ConditionExpressionVisitor();

		conditionExpression.accept(conditionExpressionVisitor);

		ddlFormRule.setDDLFormRuleConditions(
			conditionExpressionVisitor.getConditions());
		ddlFormRule.setLogicalOperator(
			conditionExpressionVisitor.getLogicalOperator());
	}

	@Reference
	protected DDMExpressionFactory ddmExpressionFactory;

	private static final String _comparisonExpressionFormat = "%s %s %s";
	private static final String _functionCallUnaryExpressionFormat = "%s(%s)";
	private static final String _notExpressionFormat = "not(%s)";
	private static final Map<String, String> _operatorFunctionNameMap =
		new HashMap<>();
	private static final Map<String, String> _operatorMap = new HashMap<>();

	static {
		_operatorFunctionNameMap.put("belongs-to", "belongsTo");
		_operatorFunctionNameMap.put("contains", "contains");
		_operatorFunctionNameMap.put("equals-to", "equals");
		_operatorFunctionNameMap.put("is-empty", "isEmpty");
		_operatorFunctionNameMap.put("not-contains", "contains");
		_operatorFunctionNameMap.put("not-equals-to", "equals");
		_operatorFunctionNameMap.put("not-is-empty", "isEmpty");

		_operatorMap.put("greater-than", ">");
		_operatorMap.put("greater-than-equals", ">=");
		_operatorMap.put("less-than", "<");
		_operatorMap.put("less-than-equals", "<=");
	}

}