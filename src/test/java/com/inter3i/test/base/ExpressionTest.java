/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: Administrator
 * Created: 2017/06/07
 * Description:
 *
 */

package com.inter3i.test.base;

import com.alibaba.fastjson.JSONObject;
import com.inter3i.base.expression.ExpressionEngine;
import com.inter3i.base.expression.entity.ExpressionEntity;

import java.util.ArrayList;
import java.util.List;

public class ExpressionTest {
    public static void main(String[] args) {
        int x = 1, y = 1;
//        Object z = add.calculate(x, y);
//        System.out.println("result is :" + z);
        Character a = Character.valueOf('中');
        Character b = Character.valueOf('中');
        System.out.println(a == b ? "等于" : "不等于");
        System.out.println(a.equals(b) ? "等于" : "不等于");
        Long long1 = Long.valueOf(8);
        Long long2 = Long.valueOf(8);
        System.out.println(long1 == long2 ? "等于" : "不等于");

        JSONObject content = new JSONObject();

        JSONObject student = new JSONObject();
        content.put("student", student);
        student.put("name", "zhangsan");
        student.put("sex", "male");
        student.put("age", 1);

        // student.age ++ > 18 ? "chengnian":"weichengnian"

        // ==
        // 常量: "chengnian"

        //and
        //sex == male

        ExpressionEntity expressiontEntity = new ExpressionEntity();
        expressiontEntity.setType(ExpressionEntity.OPERATOR_TYPE_LOGIC);
        expressiontEntity.setOperator("and");
        List<ExpressionEntity> childExpression = new ArrayList<>(2);
        expressiontEntity.setChildExpressions(childExpression);

        ExpressionEntity firstExpressiontEntity = new ExpressionEntity();
        childExpression.add(firstExpressiontEntity);

        firstExpressiontEntity.setOperator("==");
        firstExpressiontEntity.setType(ExpressionEntity.OPERATOR_TYPE_BINARY);
        List<ExpressionEntity> childsOfFirst = new ArrayList<>(2);
        firstExpressiontEntity.setChildExpressions(childsOfFirst);

        ExpressionEntity sanMu = new ExpressionEntity();
        childsOfFirst.add(sanMu);
        sanMu.setType(ExpressionEntity.OPERATOR_TYPE_TERNARY);
        sanMu.setOperator("?");

        List<ExpressionEntity> childsOfSanmu = new ArrayList<>(3);
        sanMu.setChildExpressions(childsOfSanmu);


        //student.age ++
        ExpressionEntity danmuExpress = new ExpressionEntity();
        danmuExpress.setOperator("++");
        danmuExpress.setType(ExpressionEntity.OPERATOR_TYPE_UNARY);
        ExpressionEntity.ValueTermEntity valueTermEntity = new ExpressionEntity.ValueTermEntity();
        valueTermEntity.setDataType("int");
        valueTermEntity.setValueType("pathExpress");
        valueTermEntity.setValue("student.age");
        valueTermEntity.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);

        List<ExpressionEntity> childsOfDanmu = new ArrayList<>(1);
        childsOfDanmu.add(valueTermEntity);
        danmuExpress.setChildExpressions(childsOfDanmu);

        // x > 18
        ExpressionEntity moreThan18Ofsanmu = new ExpressionEntity();
        moreThan18Ofsanmu.setType(ExpressionEntity.OPERATOR_TYPE_BINARY);
        moreThan18Ofsanmu.setOperator(">");
        List<ExpressionEntity> childsOfLogic = new ArrayList<>(2);
        childsOfLogic.add(danmuExpress);
        moreThan18Ofsanmu.setChildExpressions(childsOfLogic);
        childsOfSanmu.add(moreThan18Ofsanmu);

        // 18 constants
        ExpressionEntity.ValueTermEntity isAdult = new ExpressionEntity.ValueTermEntity();
        isAdult.setDataType("int");
        isAdult.setValueType("constants");
        isAdult.setValue("18");
        isAdult.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfLogic.add(isAdult);


        //false value of danmu
        ExpressionEntity.ValueTermEntity falseValueOfDanMu = new ExpressionEntity.ValueTermEntity();
        falseValueOfDanMu.setDataType("String");
        falseValueOfDanMu.setValueType("constants");
        falseValueOfDanMu.setValue("weichengnian");
        falseValueOfDanMu.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfSanmu.add(falseValueOfDanMu);

        //true value of danmu
        ExpressionEntity.ValueTermEntity trueValueOfDanMu = new ExpressionEntity.ValueTermEntity();
        trueValueOfDanMu.setDataType("String");
        trueValueOfDanMu.setValueType("constants");
        trueValueOfDanMu.setValue("chengnian");
        trueValueOfDanMu.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfSanmu.add(trueValueOfDanMu);

        // == "chengnian"
        ExpressionEntity.ValueTermEntity chengnianBiaoZhun = new ExpressionEntity.ValueTermEntity();
        chengnianBiaoZhun.setDataType("String");
        chengnianBiaoZhun.setValueType("constants");
        chengnianBiaoZhun.setValue("chengnian");
        chengnianBiaoZhun.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfFirst.add(chengnianBiaoZhun);


        // **************** sex == male ***************/
        //sex == male
        ExpressionEntity second = new ExpressionEntity();
        second.setOperator("==");
        second.setType(ExpressionEntity.OPERATOR_TYPE_BINARY);
        List<ExpressionEntity> childsOfSecond = new ArrayList<>(2);
        second.setChildExpressions(childsOfSecond);
        childExpression.add(second);

        //sex
        ExpressionEntity.ValueTermEntity studentSex = new ExpressionEntity.ValueTermEntity();
        studentSex.setDataType("String");
        studentSex.setValueType("pathExpress");
        studentSex.setValue("student.sex");
        studentSex.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfSecond.add(studentSex);

        ExpressionEntity.ValueTermEntity constantsMale = new ExpressionEntity.ValueTermEntity();
        constantsMale.setDataType("String");
        constantsMale.setValueType("constants");
        constantsMale.setValue("male");
        constantsMale.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfSecond.add(constantsMale);
        // **************** sex == male ***************/

        ExpressionEngine.Expression expression = ExpressionEntity.conver2Expression(expressiontEntity);
        Object result = expression.evel(content);
        System.out.println("result is :[" + result + "].");
    }
    //(int x,int y) -> x+y;
}
